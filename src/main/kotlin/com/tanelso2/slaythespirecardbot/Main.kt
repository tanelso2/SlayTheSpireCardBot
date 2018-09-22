package com.tanelso2.slaythespirecardbot

import com.tanelso2.slaythespirecardbot.CardPattern.getCards
import com.tanelso2.slaythespirecardbot.config.getConfig
import com.tanelso2.slaythespirecardbot.db.CommentStore
import com.tanelso2.slaythespirecardbot.db.CommentStoreInMemoryImpl
import com.tanelso2.slaythespirecardbot.providers.WikiaProvider
import net.dean.jraw.RedditClient
import net.dean.jraw.http.OkHttpNetworkAdapter
import net.dean.jraw.http.UserAgent
import net.dean.jraw.models.Comment
import net.dean.jraw.oauth.Credentials
import net.dean.jraw.oauth.OAuthHelper

fun main(args: Array<String>) {
    println("Hello World")
    val bot = SlayTheSpireCardBot()

    while (true) {
        println("Processing comments")
        bot.processComments()
        Thread.sleep(15000)
    }
}

class SlayTheSpireCardBot {
    private val config = getConfig()
    private val client: RedditClient
    private val user: String
    private val subreddits = config.subreddits
    private val commentStore: CommentStore = CommentStoreInMemoryImpl()
    init {
        val redditApiConfig = config.reddit
        user = redditApiConfig.username
        val creds = Credentials.script(
                username = redditApiConfig.username,
                password = redditApiConfig.password,
                clientId = redditApiConfig.clientId,
                clientSecret = redditApiConfig.clientSecret)
        val userAgent = UserAgent("java/com.tanelso2.slaythespirebot:v0.1")
        client = OAuthHelper.automatic(OkHttpNetworkAdapter(userAgent), creds)
    }

    fun postReply(parent: Comment, body: String) {
        parent.toReference(client).reply(body)
    }

    fun processComments() {
        subreddits.forEach { processSubreddit(it) }
    }

    fun processSubreddit(subreddit: String) {
        val subredditRef = client.subreddit(subreddit)
        val builder = subredditRef.comments()
        val built = builder.build()
        built.forEach {
            val stream = it.parallelStream()
            stream
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
        println("Processing comment ${comment.id}")
        println(comment)
        val cards = getCards(comment.body)
        if (cards.isNotEmpty()) {
            println(cards)
            val reply = cards
                    .map { WikiaProvider.getMessage(it) }
                    .joinToString("\n\n")
            postReply(comment, reply)
        }
        commentStore.storeComment(comment)
    }
}