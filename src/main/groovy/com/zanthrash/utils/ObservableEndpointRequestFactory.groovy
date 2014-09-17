package com.zanthrash.utils

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient
import org.apache.http.nio.client.methods.HttpAsyncMethods
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import rx.Observable
import rx.apache.http.ObservableHttp

@Component
class ObservableEndpointRequestFactory implements EndpointRequestFactory{

    @Autowired
    CloseableHttpAsyncClient closeableHttpAsyncClient

    @Override
    public Observable createGetRequestToFetchRepos(URI endpoint) {
        createObservableAsyncGetRequest(endpoint)
    }

    @Override
    public Observable createGetRequestToFetchPullRequests(URI endpoint) {
        createObservableAsyncGetRequest(endpoint)
    }

    private Observable createObservableAsyncGetRequest(URI endpoint) {
        return ObservableHttp
                .createRequest( HttpAsyncMethods.createGet(endpoint), closeableHttpAsyncClient )
                .toObservable()

    }
}
