package com.tanelso2.slaythespirecardbot

import com.tanelso2.slaythespirecardbot.config.getConfig
import com.tanelso2.slaythespirecardbot.db.CommentStore
import com.tanelso2.slaythespirecardbot.db.DBPostgresImpl
import com.tanelso2.slaythespirecardbot.db.PostStore
import com.tanelso2.slaythespirecardbot.processors.CommentProcessor
import com.tanelso2.slaythespirecardbot.processors.PostProcessor
import net.dean.jraw.RedditClient
import net.dean.jraw.http.OkHttpNetworkAdapter
import net.dean.jraw.http.UserAgent
import net.dean.jraw.oauth.Credentials
import net.dean.jraw.oauth.OAuthHelper

private val config = getConfig()

// TODO: This file could really use some cleanup
fun main(args: Array<String>) {
    println("Hello World")
    val bot = SlayTheSpireCardBot()

    while (true) {
        println("Processing subreddits")
        bot.processSubreddits()
        println("Done processing. Going to bed")
        Thread.sleep(1000 * config.sleepCycleSeconds.toLong())
    }
}

class SlayTheSpireCardBot {
    private val client: RedditClient
    private val subreddits = config.subreddits
    private val commentingAllowed: Boolean = config.reddit.commentingAllowed
    private val commentLimit = config.reddit.commentLimit
    private val postLimit = config.reddit.postLimit
    private val db = DBPostgresImpl(config.postgres)
    private val commentStore: CommentStore = db
    private val postStore: PostStore = db

    init {
        println(if (commentingAllowed) "Commenting Allowed!" else "Commenting not allowed")
        val redditApiConfig = config.reddit
        val creds = Credentials.script(
                username = redditApiConfig.username,
                password = redditApiConfig.password,
                clientId = redditApiConfig.clientId,
                clientSecret = redditApiConfig.clientSecret)
        val userAgent = UserAgent("java/com.tanelso2.slaythespirebot:v0.1")
        client = OAuthHelper.automatic(OkHttpNetworkAdapter(userAgent), creds)
    }

    private val commentProcessor = CommentProcessor(
            commentStore,
            client,
            commentLimit = commentLimit,
            commentingAllowed = commentingAllowed)
    private val postProcessor = PostProcessor(
            postStore,
            client,
            postLimit = postLimit,
            commentingAllowed = commentingAllowed)

    fun processSubreddits() {
        subreddits.parallelStream().forEach {
            processSubreddit(it)
        }
    }

    fun processSubreddit(subreddit: String) {
        val subredditRef = client.subreddit(subreddit)
        // TODO: Parallelize this?
        commentProcessor.processSubreddit(subredditRef)
        postProcessor.processSubreddit(subredditRef)
    }

}