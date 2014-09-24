package com.zanthrash.services

import com.zanthrash.Application
import com.zanthrash.config.TestConfig
import com.zanthrash.utils.CacheNames
import com.zanthrash.utils.PullRequestTestDataBuilder
import com.zanthrash.utils.RepoTestDataBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.cache.ehcache.EhCacheCache
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.context.request.async.DeferredResult
import spock.lang.Specification

@ContextConfiguration(classes = [Application.class, TestConfig.class ], loader = SpringApplicationContextLoader.class)
class ReactiveServiceCacheHitSpec extends Specification {

    @Autowired
    ReactiveService reactiveService

    @Autowired
    RepoTestDataBuilder repoTestDataBuilder

    @Autowired
    PullRequestTestDataBuilder pullRequestTestDataBuilder

    @Autowired
    CacheManager cacheManager


    def cleanup() {
        cacheManager.getCache(CacheNames.ORG_REPOS_BY_PULL_REQUEST.name).clear()
    }

    def "the first request should add the results to the 'orgReposByPullRequests' cache"() {
        given: "have a DeferredResponse to handle the async return value"
            DeferredResult<List> deferredResult = new DeferredResult<List>()

        and: "a valid org name"
            String orgName = 'netflix'

        and: "mock out 10 repos for a client"
            repoTestDataBuilder
                .number(10)
                .orgName(orgName)
                .buildRepoJson()

        and: "mock out pull requests"
            pullRequestTestDataBuilder
                .number(1)
                .orgName(orgName)
                .buildJson()

        when:
            reactiveService.getTopPullRequests(orgName, deferredResult)

        then: "check the deferred result"
            deferredResult.hasResult()

        and: "verify the list has the top 5 records"
            List results = deferredResult.result
            results.size() == 5

        and: "the cache should have all 10 repo records that where initially returned"
            Cache cache = cacheManager.getCache(CacheNames.ORG_REPOS_BY_PULL_REQUEST.name)
            List cachedResults = cache.get(orgName).get()
            cachedResults.size() == 10

        and: "all the cached results are indeed for the organization"
            cachedResults.each { repo ->
                assert repo.owner.login == orgName
            }
    }
}
