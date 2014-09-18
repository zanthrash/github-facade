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
            Observable observable = service.getRepos(orgName)

        and: "to get the results of the observable we must call the subscribe method"
            List results = []
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

    def "zero repos are returned for a organization"() {
        given: "valid org name"
            String orgName = 'netflix'

        and: "set up the responce to return zero repos"
            repoTestDataBuilder
                .number(0)
                .orgName(orgName)
                .buildRepoJson()

        when: "the service is called we get and Observable object"
            Observable observable = service.getRepos(orgName)

        and: "subsribe to the observable to get the results"
            List results = []
            observable
                .subscribe({Map processedRepo ->
                    results << processedRepo
                })

        then: "assert the list is empty"
            results.isEmpty()

    }


}
