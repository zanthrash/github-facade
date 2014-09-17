package com.zanthrash.services

import com.zanthrash.utils.EndpointFactory
import com.zanthrash.utils.EndpointRequestFactory
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import rx.Observable
import rx.apache.http.ObservableHttpResponse

@Service
@Slf4j
class OrganizationService {

    @Autowired
    EndpointFactory endpointFactory

    @Autowired
    EndpointRequestFactory endpointRequestFactory

    Observable getRepos(String organizationName) {
        URI endpoint = endpointFactory.organizationRepoURL(organizationName)

        return endpointRequestFactory.createGetRequestToFetchRepos(endpoint)
                .flatMap({ ObservableHttpResponse response ->
                    log.info("Rate Limit Remaining: {}",response?.getResponse()?.getHeaders('X-RateLimit-Remaining'))
                    return response.getContent().map({ body ->
                        String bodyAsString = new String(body)
                        log.info "Body: {}", bodyAsString
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
