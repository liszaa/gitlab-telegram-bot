package tech.lisza.gitlabtelegrambot.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "telegram.bot")
data class TelegramBotProperties(
    val name: String,
    val token: String,
    val allowedChats: List<Long>,
    val accessDeniedMessage: String
)