package tech.lisza.gitlabtelegrambot.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tech.lisza.gitlabtelegrambot.gitlab.GitlabConnector
import tech.lisza.gitlabtelegrambot.properties.GitlabProperties

@Service
class GitlabBranchFinder(
    private val gitlabConnector: GitlabConnector,
    private val gitlabProperties: GitlabProperties
) {


    fun findProjectsWithListBranches(branches: List<String>): Mono<Map<String, List<String>>> {
        return Flux.fromIterable(branches)
            .flatMap { findProjectsWithBranch(it) }
            .collectList()
            .map { buildCorrectMap(it) }
    }


    fun findProjectsWithBranch(branch: String): Mono<Map<String, List<String>>> {
        return gitlabConnector.getGroupProjects().flatMap { projects ->
            Flux
                .fromIterable(projects)
                .flatMap { project ->
                    gitlabConnector.getSuitableBranches(
                        "*$branch*",
                        fullPath = "${gitlabProperties.groupName}/${project.name}"
                    )
                        .map { project.name to it.branchNames }
                }
                .filter { it.second.isNotEmpty() }
                .collectList()
                .map { it.toMap() }
        }

    }


    private fun buildCorrectMap(maps: List<Map<String, List<String>>>): Map<String, List<String>> {
        val result = HashMap<String, List<String>>()
        val pairs: List<Pair<String, List<String>>> = maps.flatMap { map ->
            map.map { (key, value) ->
                Pair(key, value)
            }
        }
        pairs.forEach { pair ->
            val value = result.putIfAbsent(pair.first, pair.second)
            if (value != null) {
                result[pair.first] = result[pair.first]!! + pair.second
            }
        }
        return result
    }

}