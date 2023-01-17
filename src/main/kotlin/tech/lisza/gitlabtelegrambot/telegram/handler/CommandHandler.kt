package tech.lisza.gitlabtelegrambot.telegram.handler

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import reactor.core.publisher.Mono

interface CommandHandler {

    val command: String

    fun handle(update: Update): Mono<SendMessage>

    fun buildMessage(update: Update, text: String): SendMessage {
        return SendMessage.builder()
            .chatId(update.message.chatId.toString())
            .text(text)
            .build()
    }
}