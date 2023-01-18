package com.helper


import com.Env
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jooq.Configuration
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.jooq.impl.DefaultConfiguration
import java.sql.SQLException

class DBHelper {
    companion object {
        val instance by lazy {
            DBHelper()
        }
        val jdbcUrl =
            "jdbc:mysql://${Env.Database.HOST}:${Env.Database.PORT}/${Env.Database.DATABASE}?serverTimezone=America/Chicago&characterEncoding=utf8"
    }

    val db: DSLContext
    private val mysqlDataSource by lazy { buildDataSource() }

    init {
        try {
//            db = DSL.using(jdbcUrl, Env.Database.USERNAME, Env.Database.PASSWORD)
            db = DSL.using(
                buildDataSource(),
                SQLDialect.MYSQL
            )
        } catch (e: Exception) {
            throw SQLException("Database connection error")
            println("DBHelper: dbInitError: ${e.message}")
        }

    }

    private fun buildDataSource(): HikariDataSource {
        val config = HikariConfig()
        config.jdbcUrl = jdbcUrl
        config.driverClassName = "com.mysql.cj.jdbc.Driver"
        config.username = Env.Database.USERNAME
        config.password = Env.Database.PASSWORD
        config.maximumPoolSize = 20
        config.maxLifetime = 60 * 60 * 60 * 24
        config.addDataSourceProperty("cachePrepStmts", "true")
        config.addDataSourceProperty("prepStmtCacheSize", "250")
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")

        return HikariDataSource(config)
    }

    fun getDaoConfiguration(): Configuration {
        return DefaultConfiguration().set(mysqlDataSource).set(SQLDialect.MYSQL)
    }
}