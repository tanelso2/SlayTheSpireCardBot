package com.tanelso2.slaythespirecardbot.processors

import com.tanelso2.slaythespirecardbot.CardPattern.getCards
import com.tanelso2.slaythespirecardbot.db.PostStore
import com.tanelso2.slaythespirecardbot.providers.WikiaProvider
import com.tanelso2.slaythespirecardbot.providers.footer
import net.dean.jraw.RedditClient
import net.dean.jraw.models.Submission
import net.dean.jraw.models.SubredditSort
import net.dean.jraw.references.SubredditReference

class PostProcessor(private val postStore: PostStore,
                    private val client: RedditClient,
                    private val postLimit: Int = 100,
                    private val commentingAllowed: Boolean = false) {

    private val user = client.me().username

    fun processSubreddit(subreddit: String) {
        val subredditRef = client.subreddit(subreddit)
        processSubreddit(subredditRef)
    }

    fun processSubreddit(subredditRef: SubredditReference) {
        subredditRef.posts()
                .limit(postLimit)
                .sorting(SubredditSort.NEW)
                .build()
                .forEach {
                    it.parallelStream()
                            .filter { it.author != user }
                            .filter { isPostProcessed(it) }
                            .forEach { processPost(it) }
                }
    }

    private fun isPostProcessed(post: Submission): Boolean {
        // TODO: See if this user has already replied to comment
        return !postStore.isPostProcessed(post)
    }

    private fun processPost(post: Submission) {
        //println("Processing post ${post.id}")
        var cards = getCards(post.title)
        val body = post.selfText
        if (body != null) {
            val bodyCards = getCards(body)
            cards += bodyCards
        }
        if (cards.isNotEmpty()) {
            println(cards)
            val reply = cards
                    .map { WikiaProvider.getMessage(it) }
                    .joinToString("\n\n", postfix = footer)
            if (commentingAllowed) {
                postReply(post, reply)
                postStore.storePost(post)
            }
        }
    }

    private fun postReply(post: Submission, reply: String) {
        post.toReference(client).reply(reply)
    }
}