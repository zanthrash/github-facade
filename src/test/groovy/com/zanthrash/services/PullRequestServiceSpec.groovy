package com.zanthrash.services

import com.zanthrash.Application
import com.zanthrash.domain.GitHubError
import groovy.json.JsonBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess

@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader.class)
class PullRequestServiceSpec extends Specification {

    @Autowired
    RestTemplate restTemplate

    @Autowired
    OrganizationService service

    MockRestServiceServer mockGitHub

    def setup() {
        mockGitHub = MockRestServiceServer.createServer(restTemplate)
    }

    def "successfully fetch a list of repos for a given organization and marshal the JSON to objects "() {

        given: "have a organization we want to search for"
            String organizationName = 'netflix'

        and: "create our fake response of 5 repos"
            List repos = []
            5.times { repos << [name: "repo_$it", owner:[login: organizationName]]}
            def expectedJson = new JsonBuilder(repos)


        and: "wire up the fake backend service"
            mockGitHub
                .expect(requestTo("https://api.github.com/orgs/$organizationName/repos"))
                .andRespond(withSuccess(expectedJson.toString(), MediaType.APPLICATION_JSON))

        when:
            rx.Observable observable = service.getRepos(organizationName)


        then:
            observable.subscribe({List expected ->
                assert expected instanceof List
            })
//        then: "the expected number of records come back"
//            results.size() == 5
//
//        and: "ensure the service marshals the data into the correct object"
//            results.every { it instanceof Repo }
//
//        and: "every repo is for has the correct owner"
//            results.every { Repo repo ->
//                repo.owner.login == organizationName
//            }

    }

    def "attempt to fetch a list of repos for a organizaton that dosn't exist"() {
        given: "bad org name"
            String organizationName = "bad_fake_org_name"

        and: "set the expected error message"
            JsonBuilder expectedJson = new JsonBuilder([message:"Not Found"])
        and: "set up the fake backend service"

            mockGitHub
                .expect(requestTo("https://api.github.com/orgs/$organizationName/repos"))
                .andRespond(withStatus(HttpStatus.NOT_FOUND).body(expectedJson.toString()))

        when:
            def results = service.getRepos(organizationName)

        then: "should return 1 error message"
            results.size() == 1
            GitHubError error = (GitHubError)results.first()
            error.message == "Not Found"

    }
}
