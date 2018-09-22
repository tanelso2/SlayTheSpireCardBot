package com.tanelso2.slaythespirecardbot.db

import net.dean.jraw.models.Comment

interface CommentStore {
    fun isCommentProcessed(commentId: String): Boolean
    fun isCommentProcessed(comment: Comment): Boolean = isCommentProcessed(comment.id)
    fun storeComment(commentId: String)
    fun storeComment(comment: Comment) = storeComment(comment.id)
}