package com.zanthrash.utils

import com.zanthrash.Application
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader.class)
class EndpointFactorySpec extends Specification {

    @Autowired
    EndpointFactory endpointFactory

    def "create the github uri to fetch an organizations repos"() {
        when: 'create the endpoint'
            URI endpoint = endpointFactory.organizationRepoURL('netflix')
        then:
             endpoint.toString() == 'https://api.github.com/orgs/netflix/repos'
    }

    def "create the github uri to fetch pull requests for a organizations repo"() {
        when: "create the endpoint"
            URI endpoint = endpointFactory.pullRequestsForRepo('netflix', 'testRepo' )
        then:
            endpoint.toString() == 'https://api.github.com/repos/netflix/testRepo/pulls'
    }

}