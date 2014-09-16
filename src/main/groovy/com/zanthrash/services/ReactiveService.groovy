package com.zanthrash.services

import com.zanthrash.domain.PullRequest
import com.zanthrash.domain.Repo
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.context.request.async.DeferredResult
import rx.Observable

import java.util.concurrent.TimeUnit

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


//    def Observable getTopPullRequests(String orgName) {
//        return organizationService
//            .getObservableRepos(orgName)
//            .flatMap({Repo repo ->
//                pullRequestService
//                    .fetchObservablePullRequstsForRepo(repo)
//                    .flatMap({ List pr ->
//                        repo.pullRequests = pr
//                        Observable.just(repo)
//                    })
//            })
//            .toSortedList({ Repo a, Repo b ->
//                b.pullRequests.size() <=> a.pullRequests.size()
//            })
//
//    }

    def void getTopPullRequests(String orgName, DeferredResult<List> deferredResult) {
//            pullRequestService.fetchObservablePullRequstsForRepo('netflix', 'asgard').subscribe({List pulls ->
//                deferredResult.setResult(pulls)
//            })

        organizationService
                .getObservableRepos(orgName)
                .flatMap({Map repo ->
                    pullRequestService.fetchObservablePullRequstsForRepo(repo.owner.login, repo.name)
                        .flatMap({List pulls ->
                            repo['pull_requests'] = pulls
                            log.info "Suckit : {}", repo
                            return Observable.from(repo)
                        })
                })
                .toList()
                .subscribe({ List repo ->
                    deferredResult.setResult(repo)
                })

    }
}
