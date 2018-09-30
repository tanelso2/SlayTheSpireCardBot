package com.tanelso2.slaythespirecardbot.processors

import com.tanelso2.slaythespirecardbot.CardPattern.getCards
import com.tanelso2.slaythespirecardbot.db.CommentStore
import com.tanelso2.slaythespirecardbot.providers.WikiaProvider
import com.tanelso2.slaythespirecardbot.providers.footer
import net.dean.jraw.RedditClient
import net.dean.jraw.models.Comment
import net.dean.jraw.references.SubredditReference

class CommentProcessor(private val commentStore: CommentStore,
                       private val client: RedditClient,
                       private val commentLimit: Int = 100,
                       private val commentingAllowed: Boolean = false) {

    private val user = client.me().username

    fun processSubreddit(subreddit: String) {
        val subredditRef = client.subreddit(subreddit)
        processSubreddit(subredditRef)
    }

    fun processSubreddit(subredditRef: SubredditReference) {
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

    private fun isCommentProcessed(comment: Comment): Boolean {
        // TODO: See if this user has already replied to comment
        return !commentStore.isCommentProcessed(comment)
    }


    private fun processComment(comment: Comment) {
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

    private fun postReply(comment: Comment, reply: String) {
        comment.toReference(client).reply(reply)
    }
}