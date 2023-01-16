package tech.lisza.gitlabtelegrambot.telegram

import tech.lisza.gitlabtelegrambot.NOT_EXIST_COMMAND_HANDLER_KEY
import tech.lisza.gitlabtelegrambot.properties.TelegramBotProperties
import tech.lisza.gitlabtelegrambot.telegram.handler.CommandHandler
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

//я бы не делал это как бин через @Bean в конфигурации, а прям тут собрал бы все хендлеры по интерфейсу. Тогда тебе и аннотация явно будет не нужна, она лишняя. Спринг любезно все соберет по интерфейсу. Я ниже подредактирую как я сделал бы

@Component
class GitlabBot(
    private val properties: TelegramBotProperties,
    rawHandlers: List<CommandHandler>,
) : TelegramLongPollingBot() {

    val handlers: Map<String, CommandHandler> = rawHandlers.associateBy { it.command }


    @PostConstruct
    fun run() {
        TelegramBotsApi(DefaultBotSession::class.java).registerBot(this)
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