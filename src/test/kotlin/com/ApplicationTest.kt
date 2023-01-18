package com

import com.frontend.controller.ApiController
import com.google.gson.Gson
import com.plugins.configureRouting
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun testLogin() = testApplication {
        application {
            configureRouting()
        }

        routing {
            get("/login-test") {
                call.sessions.set(ApiController.OtpSession("+526457567567","123456"))
            }
        }

        val response = client.post("api/otp") {
            header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            setBody(listOf("phone" to "6457567567", "country" to "52").formUrlEncode())

        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testVerifyOTP() = testApplication {

        application {
            configureRouting()
        }

        val testHttpClient = createClient {
            install(HttpCookies)
        }

        routing {
            get("/login-test") {
                call.sessions.set(ApiController.OtpSession("+526457567567","123456"))
            }
        }

        val response = testHttpClient.post("api/verify/otp") {

            contentType(ContentType.Application.Json)
            setBody(listOf(
                "first" to "1",
                "second" to "2",
                "third" to "3",
                "fourth" to "4",
                "fifth" to "5",
                "sixth" to "6"
            ).formUrlEncode())
        }

        val loginResponse = testHttpClient.get("/login-test")
        val apiResponse = Gson().fromJson(response.bodyAsText(), ApiResponse::class.java)
        print(apiResponse)

        /*assertEquals(ApiResponse(), apiResponse)*/
        assertEquals(HttpStatusCode.OK, response.status)
    }


    data class ApiResponse(var success: Boolean? = false, var msg: String? = "")


}