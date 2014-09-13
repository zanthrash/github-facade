package com.zanthrash.services

import com.zanthrash.config.GitHubProperties
import com.zanthrash.domain.Repo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponents
import org.springframework.web.util.UriComponentsBuilder

@Service
class OrganizationService {

    @Autowired
    GitHubProperties gitHubProperties

    List getRepos(String organizationName) {
        RestTemplate restTemplate = new RestTemplate()
        HttpEntity requestEntity = getRequestEntity(getRequestHeaders())
        URI endpoint = createUriForOrganizatoinsRepos(organizationName)

        ResponseEntity<Repo[]> response = restTemplate.exchange(
                endpoint,
                HttpMethod.GET,
                requestEntity,
                Repo[].class,
        )

       response.body.toList().each {
           println it.toString()
       }

       response.body.toList()
    }

    URI createUriForOrganizatoinsRepos(String organizationName) {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
            .scheme("https")
            .host("api.github.com")
            .path("orgs/{orgName}/repos")
            .buildAndExpand(organizationName)

        URI uri = uriComponents.toUri()
        uri
    }


    HttpEntity getRequestEntity(HttpHeaders requestHeaders) {
        HttpEntity requestEntity = new HttpEntity(requestHeaders)
        requestEntity
    }

    HttpHeaders getRequestHeaders() {
        HttpHeaders requestHeaders = new HttpHeaders()
        requestHeaders.set("Authorization", "token ${gitHubProperties.token}")
        requestHeaders
    }



}
