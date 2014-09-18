package com.zanthrash.services

import com.zanthrash.utils.EndpointFactory
import com.zanthrash.utils.EndpointRequestFactory
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient
import org.apache.http.nio.client.methods.HttpAsyncMethods
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import rx.Observable
import rx.apache.http.ObservableHttp
import rx.apache.http.ObservableHttpResponse

@Service
@Slf4j
class PullRequestService {

    @Autowired
    EndpointFactory endpointFactory

    @Autowired
    EndpointRequestFactory endpointRequestFactory

    public Observable fetchPullRequestsForOrganizationAndRepo(String orgName, String repoName) {

        URI endpoint = endpointFactory.pullRequestsForRepo(orgName, repoName)

        return endpointRequestFactory.createGetRequestToFetchPullRequests(endpoint)
            .flatMap({ ObservableHttpResponse response ->
                return response.getContent().map({ body ->
                    String bodyAsString = new String(body)
                    List pullRequests = new JsonSlurper().parseText(bodyAsString)
                    return pullRequests.size() > 0 ? pullRequests : [[:]] //Hack for dealing w/ Rx and empty lists
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
    }


}
