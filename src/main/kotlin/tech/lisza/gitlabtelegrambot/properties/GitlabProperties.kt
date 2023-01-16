package tech.lisza.gitlabtelegrambot.properties

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class GitlabProperties {

    @Value("\${gitlab.group.name}")
    lateinit var groupName: String

    @Value("\${gitlab.graphql_url}")
    lateinit var url: String

    @Value("\${gitlab.group.token}")
    lateinit var token: String

}