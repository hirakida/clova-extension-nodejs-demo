package com.example

import com.linecorp.clova.extension.client.clovaClient
import com.linecorp.clova.extension.client.intentHandler
import com.linecorp.clova.extension.client.launchHandler
import com.linecorp.clova.extension.client.sessionEndedHandler
import com.linecorp.clova.extension.converter.jackson.JacksonObjectMapper
import com.linecorp.clova.extension.model.util.simpleResponse
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class ClovaHandler(clovaProperties: ClovaProperties) {
    private val client = clovaClient(applicationId = clovaProperties.applicationId) {
        objectMapper = JacksonObjectMapper()
        launchHandler { _, _ -> simpleResponse(message = "起動しました") }
        intentHandler { request, _ ->
            val intentName = request.intent.name
            when (intentName) {
                "Clova.GuideIntent" -> simpleResponse(message = "これはデモ用のスキルです")
                "Clova.YesIntent" -> simpleResponse(message = "はい")
                "Clova.NoIntent" -> simpleResponse(message = "いいえ")
                "Clova.CancelIntent" -> simpleResponse(message = "キャンセル")
                else -> simpleResponse(message = "もう一度、お願いします")
            }
        }
        sessionEndedHandler { _, _ -> simpleResponse(message = "bye") }
    }

    fun handleRequest(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(String::class.java)
            .flatMap { requestBody ->
                val response = client.handleClovaRequest(requestBody, request.headers().asHttpHeaders())
                ServerResponse.ok().body(BodyInserters.fromObject(response))
            }
    }
}
