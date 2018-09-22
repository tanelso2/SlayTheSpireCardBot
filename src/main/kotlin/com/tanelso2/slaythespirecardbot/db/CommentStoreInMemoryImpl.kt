package com.tanelso2.slaythespirecardbot.db

import net.dean.jraw.models.Comment
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap

class CommentStoreInMemoryImpl: CommentStore {
    val set: MutableSet<String> = Collections.newSetFromMap(ConcurrentHashMap())

    override fun storeComment(commentId: String) {
        set.add(commentId)
    }

    override fun isCommentProcessed(commentId: String): Boolean {
        return set.contains(commentId)
    }
}