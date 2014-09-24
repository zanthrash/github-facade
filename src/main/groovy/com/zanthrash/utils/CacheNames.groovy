package com.zanthrash.utils

/**
 * Created by zanthrash on 9/24/14.
 */
public enum CacheNames {

    ORG_REPOS_BY_PULL_REQUEST('orgReposByPullRequests')

    public String name

    public CacheNames(String name) {
        this.name = name
    }

}