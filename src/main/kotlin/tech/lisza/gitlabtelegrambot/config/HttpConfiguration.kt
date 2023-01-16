package tech.lisza.gitlabtelegrambot.config

import tech.lisza.gitlabtelegrambot.properties.GitlabProperties
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.client.HttpGraphQlClient
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.time.Duration

import java.util.concurrent.TimeUnit

@AutoConfiguration
open class HttpConfiguration { //после того как применишь spring плагин тебе идея подсветит что все open избыточные

    @Bean //нужен ли нам этот бин? может просто приватную функцию сделать?
    open fun httpClient(): HttpClient {
        val httpClient: HttpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .responseTimeout(Duration.ofMillis(5000))
            .doOnConnected { conn ->
                conn.addHandlerLast(ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                    .addHandlerLast(WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS))
            }
        return httpClient
    }


    @Bean
    open fun webClient(properties: GitlabProperties): WebClient {
        return WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(httpClient()))
            .baseUrl(properties.url)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader("Authorization", "Bearer ${properties.token}")
            .build()
    }

    @Bean
    open fun httpGraphQlClient(properties: GitlabProperties): HttpGraphQlClient {
        return HttpGraphQlClient.create(webClient(properties))
    }

}