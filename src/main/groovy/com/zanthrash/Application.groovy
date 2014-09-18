package com.zanthrash

import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync

@Configuration
@ComponentScan
@EnableAsync
@EnableAutoConfiguration
@Slf4j
class Application {

    static void main(String[] args) {
        SpringApplication.run Application, args

        log.info("Welcome to github-facade")
        log.info("To test the app goto:")
        log.info("http://localhost:8080/org/netflix/repos")

    }
}
