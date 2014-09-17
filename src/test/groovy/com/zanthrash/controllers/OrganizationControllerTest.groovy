package com.zanthrash.controllers

import com.zanthrash.Application
import com.zanthrash.services.ReactiveService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.client.RestTemplate
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.context.request.async.DeferredResult
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader.class)
@WebAppConfiguration
class OrganizationControllerTest extends Specification {

    ReactiveService mockReactiveService

    MockMvc mockMvc

    RestTemplate restTemplate

    @Autowired
    OrganizationController organizationController

    @Autowired
    WebApplicationContext webApplicationContext

    def setup() {
        restTemplate = new RestTemplate()
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
        mockReactiveService = Mock(ReactiveService)
        organizationController.reactiveService = mockReactiveService
    }

    def "test the controllers calls with collaborators"() {
        given: "we have an valid org name"
            String orgName = 'netfilx'

        when:
            DeferredResult<List> result = organizationController.reposRankedByPullRequest(orgName)

        then:
            1 * mockReactiveService.getTopPullRequests(orgName, _ as DeferredResult)
            result != null
    }


    def "call the resful endpoint successfully"() {
        expect:
           mockMvc.perform(get("/org/netfilx/repos"))
                .andExpect(status().isOk())
    }
}
