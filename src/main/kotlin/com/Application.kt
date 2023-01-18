package com

import com.api.KarumApi
import com.api.KarumResponse
import com.google.gson.Gson
import io.ktor.server.application.*
import com.plugins.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun main(args: Array<String>): Unit =
    io.ktor.server.jetty.EngineMain.main(args)

var mActiveRoute: String? = null

data class ServerSession(
    val email: String,
    val userId: Int,
    val username: String
) : Principal

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    Env.init(this.environment.config)

    configureExceptionHandling()
    configureSerialization()
    configureSecurity()
    configureRouting()
}

class ErrorResponseException(msg: String?, var data: Any? = null) : Exception(msg)

class AuthorizationException(override val message: String?) : Throwable()

fun Application.configureExceptionHandling() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            when (cause) {
                is AuthorizationException -> {
                    call.respond(
                        mapOf(
                            "success" to false,
                            "msg" to "Your session expired. Please logout and login again.",
                            "status" to HttpStatusCode.Unauthorized
                        )
                    )
                }

                is ErrorResponseException -> {
                    call.respond(mapOf("success" to false, "msg" to cause.message))
                }

                is Exception -> {
                    cause.printStackTrace()
                    call.respondText(
                        Gson().toJson(mapOf("success" to false, "msg" to cause.message)),
                        status = HttpStatusCode.InternalServerError,
                        contentType = ContentType.parse("application/json")
                    )
                }

                else -> {
                    cause.printStackTrace()
                    call.respondText(
                        Gson().toJson(mapOf("success" to false, "msg" to cause.message)),
                        status = HttpStatusCode.InternalServerError,
                        contentType = ContentType.parse("application/json")
                    )
                }
            }
        }
    }
}
