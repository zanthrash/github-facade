package com.zanthrash.services

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.zanthrash.domain.GitHubError
import com.zanthrash.domain.PullRequest
import com.zanthrash.domain.Repo
import com.zanthrash.utils.EndpointFactory
import com.zanthrash.utils.RestUtil
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.client.RestTemplate
import rx.Observable
/**
 * Created by zanthrash on 9/15/14.
 */
@Service
@Slf4j
class PullRequestService {

    @Autowired
    RestTemplate restTemplate

    @Autowired
    EndpointFactory endpointFactory

    @Autowired
    ObjectMapper objectMapper

    public List<Repo> fetchPullRequestForRepos(List<Repo> repos) {

        for(Repo repo in repos) {
            URI endpoint = endpointFactory.pullRequestsForRepo(repo)

            ResponseEntity<String> response = restTemplate.getForEntity(
                    endpoint,
                    String.class
            )

            mapResponeEntityToObjects(response, repo)
        }

        repos

    }

    void mapResponeEntityToObjects(ResponseEntity<String> response, Repo repo) {
        String body = response.getBody()
        try {
            if(RestUtil.hasError(response.statusCode)) {
                GitHubError error = objectMapper.readValue(body, GitHubError.class)
                log.error("GitHub error: fetching pull requests for org: {}, repo: {}", repo.owner.login, repo.name)
            } else {
                PullRequest[] pullRequests = objectMapper.readValue(body, PullRequest[].class)
                repo.pullRequests = pullRequests.toList()
            }
        } catch (IOException | JsonParseException | JsonMappingException ex) {
            log.warn('Issue marshaling JSON to Objects', ex)
        }
    }

}
