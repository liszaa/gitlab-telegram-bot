package tech.lisza.gitlabtelegrambot.properties

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
//а через @ConfigurationProperties не получается это сделать?
class GitlabProperties {

    @Value("\${gitlab.group.name}")
    lateinit var groupName: String

    @Value("\${gitlab.graphql_url}")
    lateinit var url: String

    @Value("\${gitlab.group.token}")
    lateinit var token: String

}