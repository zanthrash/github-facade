package com.zanthrash.services

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
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

    def void getTopPullRequests(String orgName, DeferredResult<List> deferredResult) {

        organizationService
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
                Observable.from(repos)
            })
            .take(5)
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
}
