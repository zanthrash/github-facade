package com.zanthrash.utils

import groovy.json.JsonBuilder
import rx.Observable
import rx.apache.http.ObservableHttpResponse

/**
 * Created by zanthrash on 9/17/14.
 */
class MockEndpointRequestFactory implements EndpointRequestFactory{
    @Override
    public Observable createGetRequestToFetchRepos(URI endpoint) {
        byte[] content = getRepoJson('netflix').getBytes()
        Observable observableContent = Observable.just(content)
        ObservableHttpResponse observableHttpResponse = new ObservableHttpResponse(null, observableContent)
        Observable.just( observableHttpResponse )
    }

    @Override
    Observable createGetRequestToFetchPullRequests(URI endpoint) {
        byte[] content = getPullRequestJson('netflix').getBytes()
        Observable observableContent = Observable.just(content)
        ObservableHttpResponse observableHttpResponse = new ObservableHttpResponse(null, observableContent)
        Observable.just( observableHttpResponse )
    }

    private String getPullRequestJson(String orgName) {
        List pulls = []
        5.times {
            pulls << createPullRequest(it, orgName)
        }
        def expectedJson = new JsonBuilder(pulls)
        expectedJson.toString()
    }


    private String getRepoJson(String organizationName) {
            List repos = []
            5.times { repos << createRepo(it, organizationName)}
            def expectedJson = new JsonBuilder(repos)
            expectedJson.toString()
    }

    private Map createRepo(int id, String orgName) {
        [name: "repo_$id", owner:[login: orgName]]
    }

    private Map createPullRequest(int id, String orgName ) {
        [
                title: "pull_title_$id",
                state: "open",
                number: "$id",
                html_url: "http://$orgName/pull/$id",
                base:
                        [repo: createRepo(1, orgName)]
        ]
    }


}
