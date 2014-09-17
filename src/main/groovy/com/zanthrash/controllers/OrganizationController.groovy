package com.zanthrash.controllers

import com.zanthrash.services.ReactiveService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.async.DeferredResult

@RestController
@Slf4j
class OrganizationController {

    @Autowired
    ReactiveService reactiveService

    @RequestMapping('/org/{organization_name}/repos')
    def reposRankedByPullRequest( @PathVariable('organization_name') String organizationName ) {
        final DeferredResult<List> deferredResult = new DeferredResult<>()
        reactiveService.getTopPullRequests(organizationName, deferredResult)
        deferredResult
    }

}
