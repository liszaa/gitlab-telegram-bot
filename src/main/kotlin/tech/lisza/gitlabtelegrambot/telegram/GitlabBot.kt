package tech.lisza.gitlabtelegrambot.telegram

import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import tech.lisza.gitlabtelegrambot.NOT_EXIST_COMMAND_HANDLER_KEY
import tech.lisza.gitlabtelegrambot.properties.TelegramBotProperties
import tech.lisza.gitlabtelegrambot.telegram.handler.CommandHandler

@Component
class GitlabBot(
    private val properties: TelegramBotProperties,
    rawHandlers: List<CommandHandler>
) : TelegramLongPollingBot() {

    val handlers: Map<String, CommandHandler> = rawHandlers.associateBy { it.command }

    override fun getBotToken(): String {
        return properties.token
    }

    override fun getBotUsername(): String {
        return properties.name
    }


    override fun onUpdateReceived(update: Update) {

        if (isAccessDenied(update)) {
            val response = SendMessage.builder()
                .chatId(update.message.chatId.toString())
                .text(properties.accessDeniedMessage)
                .build()
            execute(response)
            return
        }
        val handler = findHandler(update)
         handler.handle(update)
             .doOnSuccess { execute(it) }
             .doOnError {
                        val response = SendMessage.builder()
                                       .chatId(update.message.chatId.toString())
                                       .text(it.stackTrace.toString())
                                       .build()
                        execute(response)
                        }
             .subscribe()
    }

    private fun isAccessDenied(update: Update): Boolean {
        val chatId = update.message.chatId
        return chatId == null || !properties.allowedChats.contains(chatId)
    }

    private fun findHandler(update: Update): CommandHandler {
        val command = update.message.entities.find { it.type == "bot_command" }?.text ?: NOT_EXIST_COMMAND_HANDLER_KEY
        return handlers[command] ?: handlers[NOT_EXIST_COMMAND_HANDLER_KEY]!!
    }

    @PostConstruct
    fun run() {
        TelegramBotsApi(DefaultBotSession::class.java).registerBot(this)
    }

}