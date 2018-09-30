package com.tanelso2.slaythespirecardbot.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.nio.file.Files
import java.nio.file.Paths

data class PostgresConfig(
        val host: String,
        val port: Int = 5432,
        val username: String,
        val password: String,
        val database: String)

data class RedditApiConfig(
        val username: String,
        val password: String,
        val clientId: String,
        val clientSecret: String,
        val commentingAllowed: Boolean = false,
        val commentLimit: Int = 1000,
        val postLimit: Int = 100)

data class Config(
        val postgres: PostgresConfig,
        val reddit: RedditApiConfig,
        val sleepCycleSeconds: Int = 15,
        val subreddits: List<String>)

fun getConfig(): Config {
    val mapper = ObjectMapper(YAMLFactory()) // Enable YAML parsing
    mapper.registerModule(KotlinModule()) // Enable Kotlin support
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    return Files.newBufferedReader(Paths.get("config.yaml")).use {
        mapper.readValue(it, Config::class.java)
    }
}

fun main(args: Array<String>) {
    println(getConfig())
}