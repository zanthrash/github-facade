package com.zanthrash.config

import com.zanthrash.utils.EndpointRequestFactory
import com.zanthrash.utils.MockEndpointRequestFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TestConfig {

    @Bean
    public EndpointRequestFactory endpointRequestFactory() {
        new MockEndpointRequestFactory()
    }


}
