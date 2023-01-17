package tech.lisza.gitlabtelegrambot.properties

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties(prefix = "gitlab")
data class GitlabProperties(
    val groupName: String,
    val url: String,
    val token: String
)