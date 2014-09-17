package com.zanthrash.utils

import rx.Observable

interface EndpointRequestFactory {

    public Observable createGetRequestToFetchRepos(URI endpoint)

    public Observable createGetRequestToFetchPullRequests(URI endpoint)
}
