package tech.lisza.gitlabtelegrambot

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import tech.lisza.gitlabtelegrambot.properties.GitlabProperties
import tech.lisza.gitlabtelegrambot.properties.TelegramBotProperties


@ComponentScan
@AutoConfiguration
@EnableConfigurationProperties(GitlabProperties::class, TelegramBotProperties::class)
class AppConfig