package com.zanthrash.services

import com.zanthrash.Application
import com.zanthrash.config.TestConfig
import com.zanthrash.utils.PullRequestTestDataBuilder
import com.zanthrash.utils.RepoTestDataBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.context.request.async.DeferredResult
import spock.lang.Specification

@ContextConfiguration(classes = [Application.class, TestConfig.class ], loader = SpringApplicationContextLoader.class)
class ReactiveServiceSpec extends Specification {

    @Autowired
    ReactiveService reactiveService

    @Autowired
    RepoTestDataBuilder repoTestDataBuilder

    @Autowired
    PullRequestTestDataBuilder pullRequestTestDataBuilder

    def "github returns 10 repos we only want 5"() {
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

        and: "verify the list has 5 records"
            List results = deferredResult.result
            results.size() == 5

    }


    def "github returns 3 repos we ask for top 5: should just get the 3"() {
        given: "have a DeferredResponse to handle the async return value"
            DeferredResult<List> deferredResult = new DeferredResult<List>()

        and: "a valid org name"
            String orgName = 'netflix'

        and: "mock out 10 repos for a client"
            repoTestDataBuilder
                    .number(3)
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

        and: "verify the list has 5 records"
            List results = deferredResult.result
            results.size() == 3

    }
}
