package com.zanthrash.utils

import rx.Observable

interface EndpointRequestFactory {

    public Observable createGetRequstToFetchRepos(URI endpoint)
}
