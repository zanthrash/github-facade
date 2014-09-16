package com.zanthrash.controllers

import com.zanthrash.config.GitHubProperties
import com.zanthrash.domain.Repo
import com.zanthrash.services.OrganizationService
import com.zanthrash.services.PullRequestService
import com.zanthrash.services.ReactiveService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.async.DeferredResult

import java.util.concurrent.Callable

@RestController
@Slf4j
class OrganizationController {

    @Autowired
    OrganizationService organizationService

    @Autowired
    PullRequestService pullRequestService

    @Autowired
    ReactiveService reactiveService

    @RequestMapping('/foo')
    def foo() {
        List repos = organizationService.getRepos(organizationName)
        pullRequestService.fetchPullRequestForRepos(repos)
    }

    @RequestMapping('/org/{organization_name}/repos')
    def reposRankedByPullRequest( @PathVariable('organization_name') String organizationName ) {
        final DeferredResult<List> deferredResult = new DeferredResult<>()
        reactiveService.getTopPullRequests(organizationName, deferredResult)
        deferredResult
    }

}
