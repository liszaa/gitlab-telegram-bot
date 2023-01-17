package tech.lisza.gitlabtelegrambot.telegram.handler

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update

interface CommandHandler {

    val command: String

    fun handle(update: Update): SendMessage

    fun buildMessage(update: Update, text: String): SendMessage {
        return SendMessage.builder()
            .chatId(update.message.chatId.toString())
            .text(text)
            .build()
    }
}