package com.zanthrash.services

import com.zanthrash.Application
import com.zanthrash.config.TestConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.context.request.async.DeferredResult
import spock.lang.Specification

@ContextConfiguration(classes = [Application.class, TestConfig.class ], loader = SpringApplicationContextLoader.class)
class ReactiveServiceTest extends Specification {

    @Autowired
    ReactiveService reactiveService

    def "get the top 5 reps for an org orderd by number of pull requests"() {
        given: "have a DeferredResponse to handle the async return value"
            DeferredResult<List> deferredResult = new DeferredResult<List>()
        and: "a valid org name"
            String orgName = 'netflix'
        when:
            reactiveService.getTopPullRequests(orgName, deferredResult)
        then:
            deferredResult.hasResult()
            List results = deferredResult.result
            results.size() == 5
            results.collect {it.name} == ['foo']
    }
}
