package com.tanelso2.slaythespirecardbot.db

import net.dean.jraw.models.Comment

interface CommentStore {
    fun isCommentProcessed(comment: Comment): Boolean
    fun storeComment(comment: Comment)
}