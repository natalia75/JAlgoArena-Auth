package com.jalgoarena

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.context.annotation.Bean


import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@EnableEurekaClient
@EnableConfigurationProperties
@EnableSwagger2
open class JAlgoArenaAuthServer

fun main(args: Array<String>) {
    SpringApplication.run(JAlgoArenaAuthServer::class.java, *args)
}

@Bean                 
fun api(): Docket = Docket(DocumentationType.SWAGGER_2)
        .select()     
        .apis(RequestHandlerSelectors.basePackage("com.jalgoarena.web"))
        .paths(PathSelectors.any())
        .build()      