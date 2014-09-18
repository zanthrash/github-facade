package com.zanthrash.services

import com.zanthrash.Application
import com.zanthrash.config.TestConfig
import com.zanthrash.utils.RepoTestDataBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration
import rx.Observable
import spock.lang.Specification

@ContextConfiguration(classes = [Application.class, TestConfig.class ], loader = SpringApplicationContextLoader.class)
class OrganizationServiceSpec extends Specification {

    @Autowired
    OrganizationService service

    @Autowired
    RepoTestDataBuilder repoTestDataBuilder

    def "get a list of repos for a valid organization"() {

        given: "a valid organization name"
            String orgName = 'netflix'
            repoTestDataBuilder
                    .number(5)
                    .orgName(orgName)
                    .buildRepoJson()

        when: "the service is called an Observable object is returned"
            List results = []
            Observable observable = service.getRepos(orgName)

        and: "to get the results of the observable we must call the subscribe method"
            observable
                .subscribe({Map processedRepo ->
                    results << processedRepo
                })

        then: 'size should be 5 (per the whats setup in MockEndpointRequestFactory)'
            results.size() == 5
            results.each { Map repo ->
               assert repo.owner.login == orgName
            }

    }


}
