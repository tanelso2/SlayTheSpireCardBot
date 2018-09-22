package com.tanelso2.slaythespirecardbot.db

import net.dean.jraw.models.Comment
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap

class CommentStoreInMemoryImpl: CommentStore {
    val set: MutableSet<String> = Collections.newSetFromMap(ConcurrentHashMap())

    override fun storeComment(comment: Comment) {
        set.add(comment.id)
    }

    override fun isCommentProcessed(comment: Comment): Boolean {
        return set.contains(comment.id)
    }
}