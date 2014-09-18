package com.zanthrash.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import groovy.transform.ToString

@JsonIgnoreProperties(ignoreUnknown = true)
@ToString(includeNames = true)
class PullRequest {
    String url
    String state
}
