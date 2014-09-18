package com.zanthrash.controllers

import com.zanthrash.services.ReactiveService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.async.DeferredResult

@RestController
@Slf4j
@RequestMapping(value = "/org")
class OrganizationController {

    @Autowired
    ReactiveService reactiveService

    @RequestMapping(
        value = '/{organization_name}/repos',
        method = RequestMethod.GET
    )
    def reposRankedByPullRequest(
            @PathVariable('organization_name') String organizationName,
            @RequestParam(value = 'top', defaultValue = '5', required = false ) Integer top
    )
    {
        final DeferredResult<List> deferredResult = new DeferredResult<>()
        reactiveService.getTopPullRequests(organizationName, deferredResult, top)
        deferredResult
    }

}
