package lizsa.gitlabtelegrambot.telegram

import lizsa.gitlabtelegrambot.NOT_EXIST_COMMAND_HANDLER_KEY
import lizsa.gitlabtelegrambot.properties.TelegramBotProperties
import lizsa.gitlabtelegrambot.telegram.handler.CommandHandler
import jakarta.annotation.PostConstruct
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession


class GitlabBot(
    private val properties: TelegramBotProperties, var handlers: Map<String, CommandHandler>
) : TelegramLongPollingBot() {

    @PostConstruct
    fun run() {
        val botsApi = TelegramBotsApi(DefaultBotSession::class.java)
        botsApi.registerBot(this)
    }


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
        val response = handler.handle(update)
        execute(response)
    }

    private fun isAccessDenied(update: Update): Boolean {
        val chatId = update.message.chatId
        return chatId == null || !properties.allowedChatId.contains(chatId)
    }

    private fun findHandler(update: Update): CommandHandler {
        val command = update.message.entities.find { it.type == "bot_command" }?.text ?: NOT_EXIST_COMMAND_HANDLER_KEY
        return handlers[command] ?: handlers[NOT_EXIST_COMMAND_HANDLER_KEY]!!
    }


}