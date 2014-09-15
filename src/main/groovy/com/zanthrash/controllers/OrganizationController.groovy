package com.zanthrash.controllers

import com.zanthrash.config.GitHubProperties
import com.zanthrash.services.OrganizationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class OrganizationController {

    @Autowired
    OrganizationService organizationService

    @RequestMapping('/org/{organization_name}/repos')
    def reposRankedByPullRequest( @PathVariable('organization_name') String organizationName ) {
        organizationService.getRepos(organizationName)
    }
}
