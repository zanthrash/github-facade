package com.zanthrash.utils

import groovy.json.JsonBuilder
import org.springframework.stereotype.Component

@Component
class PullRequestTestDataBuilder {

    String json = ""

    int pullCount = 5
    String orgName = 'netflix'

    public PullRequestTestDataBuilder number(int count) {
        pullCount = count
        return this
    }

    public PullRequestTestDataBuilder orgName(String orgName) {
        this.orgName = orgName
        return this
    }

    public String buildJson() {
        List pulls = []
        pullCount.times {
            pulls << createPullRequest(it, orgName)
        }
        def expectedJson = new JsonBuilder(pulls)
        expectedJson.toString()
        json = expectedJson.toString()
        json
    }

    private Map createPullRequest(int id, String orgName ) {
        [
                title: "pull_title_$id",
                state: "open",
                number: "$id",
                html_url: "http://$orgName/pull/$id",
                base:
                        [repo: createRepo(1, orgName)]
        ]
    }

    private Map createRepo(int id, String orgName) {
        [name: "repo_$id", owner:[login: orgName]]
    }
}
