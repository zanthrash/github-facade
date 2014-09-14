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
    public RestTemplate restTemplate

    List getRepos(String organizationName) {
        URI endpoint = createUriForOrganizatoinsRepos(organizationName)

        ResponseEntity<Repo[]> response = restTemplate.getForEntity(
                endpoint,
                Repo[].class,
        )

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


}
