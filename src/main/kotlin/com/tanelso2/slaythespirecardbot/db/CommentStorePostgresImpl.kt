package com.tanelso2.slaythespirecardbot.db

import com.tanelso2.slaythespirecardbot.config.PostgresConfig
import com.tanelso2.slaythespirecardbot.config.getConfig
import net.dean.jraw.models.*
import java.sql.DriverManager
import java.util.*


class CommentStorePostgresImpl(private val config: PostgresConfig): CommentStore {

    private val url = "jdbc:postgresql://${config.host}:${config.port}/${config.database}"
    private val conn = DriverManager.getConnection(url, config.username, config.password)
    private val IS_PROCESSED_STMT = "SELECT COUNT(*) FROM comments WHERE id = ?"
    private val STORE_COMMENT_STATEMENT = "INSERT INTO comments (id) VALUES (?)"

    override fun isCommentProcessed(commentId: String): Boolean {
        conn.prepareStatement(IS_PROCESSED_STMT).use { stmt ->
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

}

fun main(args: Array<String>) {
    val config = getConfig()
    val commentStore: CommentStore = CommentStorePostgresImpl(config.postgres)
    val commentId = "testing2"
    assert(!commentStore.isCommentProcessed(commentId))
    commentStore.storeComment(commentId)
    assert(commentStore.isCommentProcessed(commentId))
}