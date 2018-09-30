package com.tanelso2.slaythespirecardbot.db

import com.tanelso2.slaythespirecardbot.config.PostgresConfig
import com.tanelso2.slaythespirecardbot.config.getConfig
import java.sql.DriverManager


class DBPostgresImpl(private val config: PostgresConfig): CommentStore, PostStore {
    private val url = "jdbc:postgresql://${config.host}:${config.port}/${config.database}"
    private val conn = DriverManager.getConnection(url, config.username, config.password)

    companion object {
        private val COMMENT_IS_PROCESSED_STMT = "SELECT COUNT(*) FROM comments WHERE comment_id = ?"
        private val STORE_COMMENT_STATEMENT = "INSERT INTO comments (comment_id) VALUES (?)"
        private val POST_IS_PROCESSED_STMT = "SELECT COUNT(*) FROM posts WHERE post_id = ?"
        private val STORE_POST_STATEMENT = "INSERT INTO posts (post_id) VALUES (?)"

        private val initStatements = listOf(
            "CREATE TABLE IF NOT EXISTS comments(comment_id VARCHAR(32) NOT NULL PRIMARY KEY)",
            "CREATE TABLE IF NOT EXISTS posts(post_id VARCHAR(32) NOT NULL PRIMARY KEY)"
        )
    }

    init {
        conn.createStatement().use { stmt ->
            for (initStatment in initStatements) {
                stmt.execute(initStatment)
            }
        }
    }

    override fun isCommentProcessed(commentId: String): Boolean {
        conn.prepareStatement(COMMENT_IS_PROCESSED_STMT).use { stmt ->
            stmt.setString(1, commentId)
            val result = stmt.executeQuery()
            result.next()
            val count = result.getInt(1)
            return count != 0
        }
    }

    override fun storeComment(commentId: String) {
        conn.prepareStatement(STORE_COMMENT_STATEMENT).use { stmt ->
            stmt.setString(1, commentId)
            stmt.executeUpdate()
        }
    }

    override fun isPostProcessed(postId: String): Boolean {
        conn.prepareStatement(POST_IS_PROCESSED_STMT).use { stmt ->
            stmt.setString(1, postId)
            val result = stmt.executeQuery()
            result.next()
            val count = result.getInt(1)
            return count != 0
        }
    }

    override fun storePost(postId: String) {
        conn.prepareStatement(STORE_POST_STATEMENT).use { stmt ->
            stmt.setString(1, postId)
            stmt.executeUpdate()
        }
    }


}

fun main(args: Array<String>) {
    val config = getConfig()
    val commentStore: CommentStore = DBPostgresImpl(config.postgres)
    val commentId = "testing3"
    assert(!commentStore.isCommentProcessed(commentId))
    commentStore.storeComment(commentId)
    assert(commentStore.isCommentProcessed(commentId))
}