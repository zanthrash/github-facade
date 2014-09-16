package com.zanthrash.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.http.Header
import org.apache.http.HttpRequestInterceptor
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.protocol.RequestDefaultHeaders
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder
import org.apache.http.impl.nio.client.HttpAsyncClients
import org.apache.http.message.BasicHeader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate

import java.util.concurrent.ExecutorService
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

@Configuration
class DefaultConfig {

    @Autowired
    GitHubAuthenticationInterceptor gitHubAuthenticationInterceptor

    @Autowired
    GitHubErrorHandler gitHubErrorHandler

    @Autowired
    GitHubProperties gitHubProperties

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
    public CloseableHttpAsyncClient closeableHttpAsyncClient() {

        final RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(6000)
                .setConnectTimeout(6000)
                .build()

        final CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setDefaultHeaders([new BasicHeader("Authorization", "token ${gitHubProperties.token}")])
                .setMaxConnPerRoute(20)
                .setMaxConnTotal(50)
                .build();
        httpclient.start();
        httpclient
    }


    @Bean HttpRequestInterceptor createHttpRequestInterceptor() {
        new RequestDefaultHeaders(
                [new BasicHeader("Authorization", "token ${gitHubProperties.token}")]
        )
    }

    @Bean
    public ObjectMapper objectMapper() {
        new ObjectMapper()
    }

    @Bean
    public ExecutorService executorService() {
        new ThreadPoolExecutor(4, 4, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>())
    }
}
