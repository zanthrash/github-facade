package com.zanthrash.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.zanthrash.config.GitHubProperties
import com.zanthrash.domain.GitHubError
import com.zanthrash.domain.Repo
import com.zanthrash.utils.EndpointFactory
import com.zanthrash.utils.RestUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponents
import org.springframework.web.util.UriComponentsBuilder

@Service
class OrganizationService {

    @Autowired
    RestTemplate restTemplate

    @Autowired
    EndpointFactory endpointFactory

    @Autowired
    ObjectMapper objectMapper

    List getRepos(String organizationName) {
        URI endpoint = endpointFactory.organizationRepoURL(organizationName)

        ResponseEntity<String> response = restTemplate.getForEntity(
                endpoint,
                String.class,
        )

        return mapResponeEntityToObjects(response)

    }

    List mapResponeEntityToObjects(ResponseEntity<String> response) {
        String body = response.getBody()
        try {
            if(RestUtil.hasError(response.statusCode)) {
                GitHubError error = objectMapper.readValue(body, GitHubError.class)
                return [error]
            } else {
                Repo[] repos = objectMapper.readValue(body, Repo[].class)
                return repos.toList()
            }
        } catch (IOException ex) {
            throw new RuntimeException()
        }
    }


}
