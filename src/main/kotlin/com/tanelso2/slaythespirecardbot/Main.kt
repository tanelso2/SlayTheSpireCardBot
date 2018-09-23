package com.tanelso2.slaythespirecardbot

import com.tanelso2.slaythespirecardbot.CardPattern.getCards
import com.tanelso2.slaythespirecardbot.config.getConfig
import com.tanelso2.slaythespirecardbot.db.CommentStore
import com.tanelso2.slaythespirecardbot.db.CommentStorePostgresImpl
import com.tanelso2.slaythespirecardbot.providers.WikiaProvider
import net.dean.jraw.RedditClient
import net.dean.jraw.http.OkHttpNetworkAdapter
import net.dean.jraw.http.UserAgent
import net.dean.jraw.models.Comment
import net.dean.jraw.oauth.Credentials
import net.dean.jraw.oauth.OAuthHelper

private val config = getConfig()

// TODO: This file could really use some cleanup
fun main(args: Array<String>) {
    println("Hello World")
    val bot = SlayTheSpireCardBot()

    while (true) {
        println("Processing comments")
        bot.processComments()
        println("Done processing. Going to bed")
        Thread.sleep(1000 * config.sleepCycleSeconds.toLong())
    }
}

class SlayTheSpireCardBot {
    private val client: RedditClient
    private val user: String = config.reddit.username
    private val subreddits = config.subreddits
    private val commentingAllowed: Boolean = config.reddit.commentingAllowed
    private val commentLimit = config.reddit.commentLimit
    private val commentStore: CommentStore = CommentStorePostgresImpl(config.postgres)
    private val footer = "Please report bugs at /r/stscardbottest"
            .split(" ")
            .joinToString(" ^^^", prefix = "\n\n^^^")

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

    fun postReply(parent: Comment, body: String) {
        println("Posting reply to ${parent.id}")
        parent.toReference(client).reply(body)
    }

    fun processComments() {
        subreddits.parallelStream().forEach {
            processSubreddit(it)
        }
    }

    fun processSubreddit(subreddit: String) {
        val subredditRef = client.subreddit(subreddit)
        subredditRef.comments()
                .limit(commentLimit)
                .build()
                .forEach {
                    it.parallelStream()
                            .filter { it.author != user }
                            .filter { isCommentProcessed(it) }
                            .forEach { processComment(it) }
                }
    }

    fun isCommentProcessed(comment: Comment): Boolean {
        // TODO: See if this user has already replied to comment
        return !commentStore.isCommentProcessed(comment)
    }

    fun processComment(comment: Comment) {
        //println("Processing comment ${comment.id}")
        val cards = getCards(comment.body)
        if (cards.isNotEmpty()) {
            println(cards)
            val reply = cards
                    .map { WikiaProvider.getMessage(it) }
                    .joinToString("\n\n", postfix = footer)
            if (commentingAllowed) {
                postReply(comment, reply)
                commentStore.storeComment(comment)
            }
        }
    }
}