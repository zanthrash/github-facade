package com.zanthrash.services

import com.zanthrash.Application
import com.zanthrash.config.TestConfig
import com.zanthrash.utils.PullRequestTestDataBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import rx.Observable

@ContextConfiguration(classes = [Application.class, TestConfig.class ], loader = SpringApplicationContextLoader.class)
class PullRequestServiceSpec extends Specification {

    @Autowired
    PullRequestService service

    @Autowired
    PullRequestTestDataBuilder pullRequestTestDataBuilder

    def "get a list of pull requests for a valid organization repo"() {
        given: "a valid organization name"
            String orgName = 'netfilx'
            String repoName = 'asgard'

        and: "set up the expected json results"
            pullRequestTestDataBuilder
                .number(5)
                .orgName(orgName)
                .buildJson()

        when: "the service is called an Observable object is returned"
            Observable observable = service.fetchPullRequestsForOrganizationAndRepo(orgName, repoName)

        and: "to get the results of the observable we must call the subscribe method"
            List results = []
            observable
                .subscribe({ List processedPulls ->
                    results = processedPulls
                })

        then: 'size should be 5 (per the whats setup in MockEndpointRequestFactory)'
            results.size() == 5

        and: "each map should contain these keys"
            results.each { Map repo ->
                assert repo.containsKey('title')
                assert repo.containsKey('state')
                assert repo.containsKey('number')
                assert repo.containsKey('html_url')
                assert repo.containsKey('repo_name')
            }

        and: "only these keys"
            results.each {Map repo ->
                assert repo.every {entity -> ['title', 'state', 'number', 'html_url', 'repo_name'].contains(entity.key)}
            }
    }


    def "zero pull request are found for an orgs repo"() {
        given: "a valid organization name"
            String orgName = 'netfilx'
            String repoName = 'asgard'

        and: "set up the expected json results"
            pullRequestTestDataBuilder
                    .number(0)
                    .orgName(orgName)
                    .buildJson()

        when: "the service is called an Observable object is returned"
            Observable observable = service.fetchPullRequestsForOrganizationAndRepo(orgName, repoName)

        and: "to get the results of the observable we must call the subscribe method"
            List results = []
            observable
                    .subscribe({ List processedPulls ->
                results = processedPulls
            })

        then: 'size should be 5 (per the whats setup in MockEndpointRequestFactory)'
            results.size() == 1

        and: "the only result in the list should be null"
            results.first() == null

    }
}
