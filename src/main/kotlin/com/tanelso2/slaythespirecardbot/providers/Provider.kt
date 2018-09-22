package com.tanelso2.slaythespirecardbot.providers

interface Provider {
    fun getMessage(cardName: String): String
}