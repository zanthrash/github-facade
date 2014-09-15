package com.zanthrash.services

import com.zanthrash.Application
import com.zanthrash.config.DefaultConfig
import com.zanthrash.domain.Owner
import com.zanthrash.domain.Repo
import groovy.json.JsonBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader.class)
class OrganizationServiceSpec extends Specification {

    @Autowired
    RestTemplate restTemplate

    @Autowired
    OrganizationService service

    MockRestServiceServer mockGitHub

    def setup() {
        mockGitHub = MockRestServiceServer.createServer(restTemplate)
    }

    def "fetch a list of repos for a given organization and marshal the JSON to objects "() {

        given: "have a organizaton we want to search for"
            String orginazationName = 'netflix'

        and: "create our fake response of 5 repos"
            List repos = []
            5.times { repos << [name: "repo_$it", owner:[login: orginazationName]]}
            def expectedJson = new JsonBuilder(repos)


        and: "wire up the fake backend service"
            mockGitHub
                    .expect(requestTo("https://api.github.com/orgs/$orginazationName/repos"))
                    .andRespond(withSuccess(expectedJson.toString(), MediaType.APPLICATION_JSON))

        when:
            List results = service.getRepos(orginazationName)

        then: "the expected number of records come back"
            results.size() == 5

        and: "ensure the service marshals the data into the correct object"
            results.every { it instanceof Repo }

        and: "every repo is for has the correct owner"
            results.every { Repo repo ->
                repo.owner.login == orginazationName
            }

    }
}
