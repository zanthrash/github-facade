package com.zanthrash.services

import com.zanthrash.utils.CacheNames
import groovy.util.logging.Slf4j
import net.sf.ehcache.Element
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.cache.ehcache.EhCacheCache
import org.springframework.cache.support.SimpleValueWrapper
import org.springframework.stereotype.Service
import org.springframework.web.context.request.async.DeferredResult
import rx.Observable

/**
 * Created by zanthrash on 9/15/14.
 */
@Service
@Slf4j
class ReactiveService {

    @Autowired
    OrganizationService organizationService

    @Autowired
    PullRequestService pullRequestService

    @Autowired
    CacheManager cacheManager

    def void getTopPullRequests(String orgName, DeferredResult<List> deferredResult, Integer top = 5) {
            createObserableFromCacheOrRequest(orgName)
            .take(top)
            .toList()
            .subscribe({ List repo ->
                if(repo) {
                    deferredResult.setResult(repo)
                } else {
                    List message = [[message: "No results found for organization: $orgName".toString()]]
                    deferredResult.setResult(message)
                }

            })

    }


    private Observable createObserableFromCacheOrRequest(String orgName) {
        Cache cache = cacheManager.getCache(CacheNames.ORG_REPOS_BY_PULL_REQUEST.name)
        def element = cache.get(orgName)

        if(element) {
            log.info "Cache Hit: [orgReposByPullRequests] for {}", orgName
            return Observable.from(element.get())
        } else {
            return organizationService
                .getRepos(orgName)
                .onErrorResumeNext(Observable.empty())
                .flatMap({Map repo ->
                    pullRequestService.fetchPullRequestsForOrganizationAndRepo(repo.owner.login, repo.name)
                        .onErrorResumeNext(Observable.empty())
                        .flatMap({List pulls ->
                        repo['pull_requests'] = pulls
                        return Observable.from(repo)
                    })
            })
            .toSortedList({ Map a, Map b ->
                b.pull_requests?.size() <=> a.pull_requests?.size()
            })
            .flatMap({List repos ->
                log.info "Cache Miss: [{}] for {}", CacheNames.ORG_REPOS_BY_PULL_REQUEST.name ,orgName
                cache.put(orgName, repos)
                Observable.from(repos)
            })
        }
    }
}
