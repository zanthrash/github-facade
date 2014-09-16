package com.zanthrash.services

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.zanthrash.domain.GitHubError
import com.zanthrash.domain.PullRequest
import com.zanthrash.domain.Repo
import com.zanthrash.utils.EndpointFactory
import com.zanthrash.utils.RestUtil
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient
import org.apache.http.nio.client.methods.HttpAsyncMethods
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.client.RestTemplate
import rx.Observable
import rx.apache.http.ObservableHttp
import rx.apache.http.ObservableHttpResponse

import java.util.concurrent.ExecutorService

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

    @Autowired
    CloseableHttpAsyncClient closeableHttpAsyncClient

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

    public List<PullRequest> fetchPullRequestsForRepo(Repo repo) {
        URI endpoint = endpointFactory.pullRequestsForRepo(repo)

        ResponseEntity<PullRequest[]> response = restTemplate.getForEntity(
                endpoint,
                PullRequest[].class
        )

        response.body.toList()
    }

    public Observable fetchObservablePullRequstsForRepo(String orgName, String repoName) {
        URI endpoint = endpointFactory.pullRequestsForRepo(orgName, repoName)
        log.info "EndPoint: {}", endpoint.toString()
        return ObservableHttp
                .createRequest(HttpAsyncMethods.createGet(endpoint), closeableHttpAsyncClient)
                .toObservable()
                .flatMap({ ObservableHttpResponse response ->
                    return response.getContent().map({ body ->
                        String bodyAsString = new String(body)
                        log.info "PR response body: {}", bodyAsString
//                        PullRequest[] pullRequests = objectMapper.readValue(bodyAsString, PullRequest[].class)
                        List pullRequests = new JsonSlurper().parseText(bodyAsString)
                        return pullRequests.size() > 0 ? pullRequests : [[:]]
                    })
                })
                .flatMap({ List pullRequests ->
                    Observable.from(pullRequests)
                })
                .map({Map pull ->
                    if(pull.keySet().size() > 0) {
                        Map base = pull.base
                        Map repo = base.repo
                        Map sub = pull.subMap('title', 'state', 'number', 'html_url')
                        sub['repo_name'] = repo.name
                        return sub
                    }
                })
                .toList()

//        return Observable.create({ subscriber ->
//            executorService.execute(new Runnable() {
//                @Override
//                void run() {
//                    if (subscriber.isUnsubscribed()) {return}

//                    log.info("Fetching Pull Requsts for repo: {}", repo.name)
//                    List prList = fetchPullRequestsForRepo(repo)
//                    subscriber.onNext(prList)
//
//                    if (!subscriber.isUnsubscribed()) {
//                        subscriber.onCompleted()
//                    }
//                }
//            })
//        })
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

    Observable fetchPR(Repo repo) {
        return Observable.create({ observer ->
            observer.onNext(new PullRequest(url:"http://${repo.name}"))
            observer.onCompleted()
        })
    }
}
