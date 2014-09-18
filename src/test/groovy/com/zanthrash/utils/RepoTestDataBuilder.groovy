package com.zanthrash.utils

import groovy.json.JsonBuilder
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component
class RepoTestDataBuilder {

    String repoJson = ""

    int repoCount = 8
    String orgName = 'netflix'

    public RepoTestDataBuilder number(int count) {
        repoCount = count
        return this
    }

    public RepoTestDataBuilder orgName(String orgName) {
        this.orgName = orgName
        return this
    }

    public String buildRepoJson() {
        List repos = []
        repoCount.times { repos << createRepo(it, orgName)}
        def expectedJson = new JsonBuilder(repos)
        repoJson = expectedJson.toString()
        repoJson
    }

    private Map createRepo(int id, String orgName) {
        [name: "repo_$id", owner:[login: orgName]]
    }
}
