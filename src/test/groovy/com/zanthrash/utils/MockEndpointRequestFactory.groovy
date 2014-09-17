package com.zanthrash.utils

import groovy.json.JsonBuilder
import rx.Observable
import rx.apache.http.ObservableHttpResponse

/**
 * Created by zanthrash on 9/17/14.
 */
class MockEndpointRequestFactory implements EndpointRequestFactory{
    @Override
    public Observable createGetRequstToFetchRepos(URI endpoint) {
        byte[] content = getRepoJson('netflix').getBytes()
        Observable observableContent = Observable.just(content)
        ObservableHttpResponse observableHttpResponse = new ObservableHttpResponse(null, observableContent)
        Observable.just( observableHttpResponse )
    }


    private String getRepoJson(String organizationName) {
            List repos = []
            5.times { repos << [name: "repo_$it", owner:[login: organizationName]]}
            def expectedJson = new JsonBuilder(repos)
            expectedJson.toString()
    }
}
