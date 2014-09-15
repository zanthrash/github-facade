package com.zanthrash.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate

@Configuration
class DefaultConfig {

    @Autowired
    GitHubAuthenticationInterceptor gitHubAuthenticationInterceptor

    @Autowired
    GitHubErrorHandler gitHubErrorHandler

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate template = new RestTemplate(clientHttpRequestFactory())
        template.setInterceptors([gitHubAuthenticationInterceptor])
        template.setErrorHandler(gitHubErrorHandler)
        template
    }

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        ClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory()
        factory.setReadTimeout(5000)
        factory.setConnectTimeout(5000)
        factory
    }

    @Bean
    public ObjectMapper objectMapper() {
        new ObjectMapper()
    }
}
