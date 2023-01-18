package com.plugins

import com.ErrorResponseException
import com.ServerSession
import com.frontend.controller.ApiController
import com.helper.withBaseUrl
import io.ktor.server.auth.*
import io.ktor.server.sessions.*
import io.ktor.server.application.*
import kotlinx.html.I

fun Application.configureSecurity() {

    install(Sessions) {
        cookie<ServerSession>("web_session") {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 6660
        }

        cookie<UserSession>("usersession")
        cookie<ApiController.OtpSession>("otp")

        cookie<CurpSession>("curp")
        cookie<ProductSession>("product")
//        cookie<IneSummaryData>("summary")
//        cookie<UploadDocumentsSession>("uploaddocuments")
    }

    authentication {
        /*   basic(name = "mobile-auth") {
               realm = "Krum Card Application"
               validate { credentials ->
                   val id = credentials.name.toIntOrNull() ?: 0
                   val auth = credentials.password
                   val user = if (id == 1) UserModel.getById(id) else UserModel.getUserByIdAndToken(id, auth)
                   if (user != null) {
                       UserSession(user)
                   } else {
                       null
                   }
               }
           }*/

        session<UserSession>("customerAuth") {
            challenge("/splash".withBaseUrl())
            validate {
                if (it.userId > 0) {
                    it
                } else {
                    null
                }
            }
        }
    }
}


data class UserSession(
    val userId: Int,
    val mobile: String,
    val authToken: String,
    val isHanddOff: Boolean,
    val isReturning: Boolean = false
) : Principal

data class CurpSession(val curp: String, val ine: String)

data class ProductSession(val productId: String, val price: String)

//data class UploadDocumentsSession(var ine: Boolean, var passport: Boolean, var address: Boolean)

val ApplicationCall.userSession
    get() = principal<UserSession>() ?: throw ErrorResponseException("user is not valid")
val ApplicationCall.user
    get() = userSession


