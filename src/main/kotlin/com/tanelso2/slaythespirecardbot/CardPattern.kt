package com.tanelso2.slaythespirecardbot

object CardPattern {
    private val regex = Regex("\\[\\[([a-zA-Z ]*)\\]\\]")
    fun getCards(body: String): List<String> {
        return regex.findAll(body)
                .map { it.groups[1]?.value }
                .filterNotNull()
                .toList()
    }
}