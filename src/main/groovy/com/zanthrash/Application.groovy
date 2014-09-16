package com.zanthrash

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync

@Configuration
@ComponentScan
@EnableAsync
@EnableAutoConfiguration
class Application {

    static void main(String[] args) {
        SpringApplication.run Application, args

        println("Dig it")
    }
}
