package com.tanelso2.slaythespirecardbot.db

import com.tanelso2.slaythespirecardbot.config.PostgresConfig
import net.dean.jraw.models.Comment
import java.sql.DriverManager
import java.util.Properties

// TODO: Figure this out
class CommentStorePostgresImpl(private val config: PostgresConfig): CommentStore {
    init {
        val p = Properties()
        val url = "jdbc:postgresql://${config.host}:${config.port}/${config.database}"
        val c = DriverManager.getConnection(url, config.username, config.password)
    }
    override fun isCommentProcessed(comment: Comment): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun storeComment(comment: Comment) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}