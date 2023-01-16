package tech.lisza.gitlabtelegrambot.config

import tech.lisza.gitlabtelegrambot.Handler
import tech.lisza.gitlabtelegrambot.properties.TelegramBotProperties
import tech.lisza.gitlabtelegrambot.telegram.GitlabBot
import tech.lisza.gitlabtelegrambot.telegram.handler.CommandHandler
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class AppConfig {

    @Bean
    fun gitlabBot(properties: TelegramBotProperties, applicationContext: ConfigurableApplicationContext): GitlabBot {
        val beans = applicationContext.getBeansWithAnnotation(Handler::class.java) as Map<String, CommandHandler>
        val handlers = beans.map { it.value.command to it.value }.toMap()
        return GitlabBot(properties, handlers)
    }

    // тест-кейсы
    // 1. Пингануть гитлаб, чтобы проверить, что все креды в норме
    // 2.
}