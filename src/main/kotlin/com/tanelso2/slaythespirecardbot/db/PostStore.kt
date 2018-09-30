package com.tanelso2.slaythespirecardbot.db

import net.dean.jraw.models.Submission

interface PostStore {
    fun isPostProcessed(postId: String): Boolean
    fun isPostProcessed(post: Submission): Boolean = isPostProcessed(post.id)
    fun storePost(postId: String)
    fun storePost(post: Submission) = storePost(post.id)
}