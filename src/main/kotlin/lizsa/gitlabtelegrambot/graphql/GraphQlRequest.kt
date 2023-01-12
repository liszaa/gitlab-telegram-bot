package lizsa.gitlabtelegrambot.graphql

import org.springframework.graphql.client.GraphQlClient
import org.springframework.graphql.client.HttpGraphQlClient
import reactor.core.publisher.Mono


class GraphQlRequest(
    val path: String,
    val query: String,
    val variables: Map<String, String>? = null
) {

    ////////////////////
    // Query Builders //
    ////////////////////

    fun <T> listQuery(
        client: HttpGraphQlClient,
        entityClass: Class<T>
    ): Mono<List<T>> {
        return querySpec(client, this)
            .toEntityList(entityClass)
    }

    fun <T> singleQuery(
        client: HttpGraphQlClient,
        entityClass: Class<T>
    ): Mono<T> {
        return querySpec(client, this)
            .toEntity(entityClass)
    }
}

private fun querySpec(
    client: HttpGraphQlClient,
    request: GraphQlRequest
): GraphQlClient.RetrieveSpec {
    return if (request.variables != null && request.variables.isNotEmpty()) {
        client
            .documentName(request.query)
            .variables(request.variables)
            .retrieve(request.path)
    } else {
        client
            .documentName(request.query)
            .retrieve(request.path)
    }
}