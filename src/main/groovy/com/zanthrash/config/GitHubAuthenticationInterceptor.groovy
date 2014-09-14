package com.zanthrash.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.stereotype.Component

@Component
class GitHubAuthenticationInterceptor implements ClientHttpRequestInterceptor{

    @Autowired
    GitHubProperties gitHubProperties

    @Override
    ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders()
        headers.set("Authorization", "token ${gitHubProperties.token}")

        execution.execute(request, body)
    }
}
