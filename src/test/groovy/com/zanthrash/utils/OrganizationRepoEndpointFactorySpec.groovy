package com.zanthrash.utils

import com.zanthrash.Application
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification


@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader.class)
class OrganizationRepoEndpointFactorySpec extends Specification {

    @Autowired
    EndpointFactory endpointFactory

    def "test"() {
        when: 'create the github endpoint for the organizations list of repos '
            URI endpoint = endpointFactory.organizationRepoURL('netflix')
        then:
             endpoint.toString() == 'https://api.github.com/orgs/netflix/repos'

    }
}