package com.zanthrash.services

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.zanthrash.domain.GitHubError
import com.zanthrash.domain.Repo
import com.zanthrash.utils.EndpointFactory
import com.zanthrash.utils.RestUtil
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient
import org.apache.http.impl.nio.client.DefaultHttpAsyncClient
import org.apache.http.nio.client.methods.HttpAsyncMethods
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import rx.Observable
import rx.apache.http.ObservableHttp
import rx.apache.http.ObservableHttpResponse

import java.util.concurrent.TimeUnit

@Service
@Slf4j
class OrganizationService {

    @Autowired
    EndpointFactory endpointFactory

    @Autowired
    CloseableHttpAsyncClient closeableHttpAsyncClient

    Observable getRepos(String organizationName) {
        URI endpoint = endpointFactory.organizationRepoURL(organizationName)

        return ObservableHttp
                .createRequest( HttpAsyncMethods.createGet(endpoint), closeableHttpAsyncClient )
                .toObservable()
                .flatMap({ ObservableHttpResponse response ->
                    return response.getContent().map({ body ->
                        String bodyAsString = new String(body)
                        List repos = new JsonSlurper().parseText(bodyAsString)
                        return repos
                    })
                })
                .flatMap({ List repos ->
                    Observable.from(repos)
                })
                .map({ Map repo ->
                    repo.subMap('name', 'owner')
                })

    }


}
