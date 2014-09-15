package com.zanthrash.services

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.zanthrash.domain.GitHubError
import com.zanthrash.domain.Repo
import com.zanthrash.utils.EndpointFactory
import com.zanthrash.utils.RestUtil
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
@Slf4j
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
        } catch (IOException | JsonParseException | JsonMappingException ex) {
            log.warn('Issue marshaling JSON to Objects', ex)
        }
    }


}
