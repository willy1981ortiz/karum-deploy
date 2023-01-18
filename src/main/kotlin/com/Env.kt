package com

import io.ktor.server.config.*

object Env {

    private lateinit var appConfig: ApplicationConfig
    val tempFolder: String by lazy { System.getProperty("java.io.tmpdir") }
    fun init(config: ApplicationConfig) {
        appConfig = config
    }

    val ENVIRONMENT by lazy { getKtorStringProperty("environment") }
    val DEBUG: Boolean by lazy { ENVIRONMENT != "prod" }
    val BASE_URL by lazy { getKtorStringProperty("baseUrl") }
    val KARUM_API by lazy { getKtorStringProperty("karumApiHost") }

    val TWILIO_ENABLED by lazy { getKtorStringProperty("twilioEnabled").lowercase() == "true" }
    val TWILIO_SID by lazy { getKtorStringProperty("twilioSID") }
    val TWILIO_TOKEN by lazy { getKtorStringProperty("twilioToken") }
    val TWILIO_SENDER by lazy { getKtorStringProperty("twilioSender") }

    private fun getKtorStringProperty(key: String): String = appConfig.property("ktor.$key").getString()
    private fun getEnvStringProperty(key: String) = appConfig.property("ktor.$ENVIRONMENT.$key").getString()

    object Database {
        val HOST = getEnvStringProperty("db.host")
        val USERNAME = getEnvStringProperty("db.username")
        val PASSWORD = getEnvStringProperty("db.password")
        val DATABASE = getEnvStringProperty("db.database")
        val PORT = getEnvStringProperty("db.port").toInt()
    }

    object S3 {
        val HOST = getEnvStringProperty("s3.host")
        val REGION = getEnvStringProperty("s3.region")
        val BUCKET = getEnvStringProperty("s3.bucket")
        val PATH = getEnvStringProperty("s3.path")
        val ACCESS_KEY = getEnvStringProperty("s3.accessKey")
        val SECRET_KEY = getEnvStringProperty("s3.secretKey")
    }

    object Data {
        val TITLE = getEnvStringProperty("data.title")
        val SUB_TITLE = getEnvStringProperty("data.subtitle")
        val SHORT_TITLE = getEnvStringProperty("data.shortTitle")
    }
}