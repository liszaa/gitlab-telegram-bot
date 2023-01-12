package lizsa.gitlabtelegrambot.gitlab

import lizsa.gitlabtelegrambot.GROUP_PROJECT_PATH
import lizsa.gitlabtelegrambot.PROJECT_BRANCH_NAMES
import lizsa.gitlabtelegrambot.graphql.GraphQlRequest
import lizsa.gitlabtelegrambot.properties.GitlabProperties
import org.springframework.graphql.client.HttpGraphQlClient
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono


/** класс-клиент к Gitlab **/


@Service
class GitlabConnector(
    private val httpGraphQlClient: HttpGraphQlClient,
    private val gitlabProperties: GitlabProperties
) {

    fun getGroupProjects(): Mono<List<Project>> {
        return GraphQlRequest(
            path = GROUP_PROJECT_PATH,
            query = "groupProjects",
            variables = mapOf("fullPath" to gitlabProperties.groupName)
        )
            .listQuery(
                client = httpGraphQlClient,
                entityClass = Project::class.java
            )
    }

    fun getSuitableBranches(searchPattern: String, fullPath: String): Mono<ProjectBranches> {
        return GraphQlRequest(
            path = PROJECT_BRANCH_NAMES,
            query = "suitableBranches",
            variables = mapOf("fullPath" to fullPath, "searchPattern" to searchPattern)
        )
            .singleQuery(
                client = httpGraphQlClient,
                entityClass = ProjectBranches::class.java
            )
    }

}
