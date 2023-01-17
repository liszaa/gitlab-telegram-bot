package tech.lisza.gitlabtelegrambot.telegram.handler

import org.awaitility.Awaitility.await
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import reactor.core.publisher.Mono
import reactor.core.publisher.MonoSink
import tech.lisza.gitlabtelegrambot.service.GitlabBranchFinder
import java.time.Duration

@Component
class BranchesHandler(private val gitlabBranchFinder: GitlabBranchFinder) : CommandHandler {

    override val command: String = "/branches"


    override fun handle(update: Update): Mono<SendMessage> {

        val branches = parseBranchPattern(update.message.text)
        return gitlabBranchFinder
            .findProjectsWithListBranches(branches)
            .map {
                    val result = buildMessage(update, buildMarkdown(it))
                        result.enableMarkdown(true)
                        result
            }
    }

    private fun parseBranchPattern(message: String): List<String> {
        return message.removePrefix(command).trim().split(",").map { it.trim() }
    }

    private fun buildMarkdown(branches: Map<String, List<String>>): String {
        return branches
            .map { pair ->
                "*${pair.key}*${pair.value.joinToString(separator = "\n", prefix = "\n", postfix = "\n") { it }}"
            }
            .joinToString(separator = "\n", postfix = "\n") { it }
    }

}
