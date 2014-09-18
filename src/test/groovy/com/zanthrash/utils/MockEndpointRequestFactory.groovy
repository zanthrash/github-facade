package com.zanthrash.utils

import groovy.json.JsonBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import rx.Observable
import rx.apache.http.ObservableHttpResponse

@Component
class MockEndpointRequestFactory implements EndpointRequestFactory{

    @Autowired
    RepoTestDataBuilder repoTestDataBuilder

    @Autowired
    PullRequestTestDataBuilder pullRequestTestDataBuilder

    @Override
    public Observable createGetRequestToFetchRepos(URI endpoint) {
        byte[] content = repoTestDataBuilder.repoJson.getBytes()
        Observable observableContent = Observable.just(content)
        ObservableHttpResponse observableHttpResponse = new ObservableHttpResponse(null, observableContent)
        Observable.just( observableHttpResponse )
    }

    @Override
    Observable createGetRequestToFetchPullRequests(URI endpoint) {
        byte[] content = pullRequestTestDataBuilder.json.getBytes()
        Observable observableContent = Observable.just(content)
        ObservableHttpResponse observableHttpResponse = new ObservableHttpResponse(null, observableContent)
        Observable.just( observableHttpResponse )
    }

}
