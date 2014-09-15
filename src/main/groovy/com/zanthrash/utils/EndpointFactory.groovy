package com.zanthrash.utils

import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponents
import org.springframework.web.util.UriComponentsBuilder

@Component
class EndpointFactory {

    public URI organizationRepoURL(String organizationName) {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("api.github.com")
                .path("orgs/{orgName}/repos")
                .buildAndExpand(organizationName)

        URI uri = uriComponents.toUri()
        uri
    }
}
