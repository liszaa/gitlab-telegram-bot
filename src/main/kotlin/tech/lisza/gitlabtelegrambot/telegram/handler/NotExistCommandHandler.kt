package tech.lisza.gitlabtelegrambot.telegram.handler

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import tech.lisza.gitlabtelegrambot.NOT_EXIST_COMMAND_HANDLER_KEY
import tech.lisza.gitlabtelegrambot.NOT_EXIST_COMMAND_MESSAGE

@Component
class NotExistCommandHandler : CommandHandler {

    override val command: String = NOT_EXIST_COMMAND_HANDLER_KEY

    override fun handle(update: Update): SendMessage {
        return buildMessage(update, NOT_EXIST_COMMAND_MESSAGE)
    }

}