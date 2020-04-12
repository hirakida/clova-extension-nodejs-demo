package com.example

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.router

@SpringBootApplication
class Application {
    @Bean
    fun route(clovaHandler: ClovaHandler) = router {
        accept(MediaType.APPLICATION_JSON)
        POST("/", clovaHandler::handleRequest)
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
