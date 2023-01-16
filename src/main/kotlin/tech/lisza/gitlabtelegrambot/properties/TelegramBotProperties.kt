package tech.lisza.gitlabtelegrambot.properties

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
//а через @ConfigurationProperties не получается это сделать?
class TelegramBotProperties {

    @Value("\${telegram.bot.name}")
    lateinit var name: String

    @Value("\${telegram.bot.token}")
    lateinit var token: String

    @Value("\${telegram.bot.allowed_chats}")
    lateinit var allowedChatId: List<Long>

    @Value("\${telegram.bot.access_denied_message}")
    lateinit var accessDeniedMessage: String

}