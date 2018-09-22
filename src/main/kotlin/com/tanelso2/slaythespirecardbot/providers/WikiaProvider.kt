package com.tanelso2.slaythespirecardbot.providers

object WikiaProvider: Provider {
    private const val BASE_URL = "http://slay-the-spire.wikia.com/wiki/"

    override fun getMessage(cardName: String): String {
        return "[$cardName](${getFullUrl(cardName)})"
    }

    fun getFullUrl(cardName: String): String {
        // TODO: Verify this URL leads to an actual page
        return "${BASE_URL}${getCardNameTitle(cardName)}"
    }

    fun getCardNameTitle(cardName: String): String {
        return cardName
                .split(' ')
                .filter { it.isNotEmpty() }
                .map {"${it.substring(0, 1).toUpperCase()}${it.substring(1).toLowerCase()}" }
                .joinToString(separator = "_")

    }
}