package com.zanthrash.utils

import com.zanthrash.Application
import com.zanthrash.config.TestConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import rx.Observable

@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader.class)
class ObservableEndpointRequestFactorySpec extends Specification {

    @Autowired
    ObservableEndpointRequestFactory observableEndpointRequestFactory

    def "create an observable asyn enpoint request for repos"() {

        when:
            URI uri = new URI("https://api.github.com/repo")
            Observable result = observableEndpointRequestFactory.createGetRequestToFetchRepos(uri)
        then:
            result != null
            result instanceof Observable
    }

    def "create an observable asyn enpoint request for pull requests"() {

        when:
            URI uri = new URI("https://api.github.com/pull")
            Observable result = observableEndpointRequestFactory.createGetRequestToFetchPullRequests(uri)
        then:
            result != null
            result instanceof Observable
    }
}

