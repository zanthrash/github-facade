package com.zanthrash.config

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

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate template = new RestTemplate(clientHttpRequestFactory())
        template.setInterceptors([gitHubAuthenticationInterceptor])
        template
    }

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        ClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory()
        factory.setReadTimeout(5000)
        factory.setConnectTimeout(5000)
        factory
    }
}
