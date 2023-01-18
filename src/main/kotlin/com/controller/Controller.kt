package com.controller

import com.ErrorResponseException
import com.helper.DBHelper
import com.helper.withBaseUrl
import com.model.DocumentModel
import com.model.UserModel
import com.plugins.userSession
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class Controller(protected val call: ApplicationCall) : CoroutineScope {
    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    protected val db
        get() = DBHelper.instance.db

    suspend fun success(data: Any? = null, msg: String? = null, extra: Any? = null) {
        call.respond(Response(true, msg, data ?: "", extra))
    }

    suspend fun response(success: Boolean, data: Any? = null, msg: String? = null, extra: Any? = null) {
        call.respond(Response(success, msg, data, extra))
    }

    @Throws(ErrorResponseException::class)
    fun error(msg: String? = null, data: Any? = null): Nothing = throw ErrorResponseException(msg, data)

    data class Response(val success: Boolean, val msg: String? = null, val data: Any? = null, val extra: Any? = null)

    suspend fun pageRedirect(): Boolean {
        val userSession = call.userSession
        val user = UserModel.getUser(userSession.mobile)

        when (user?.status) {
            UserModel.NEW_USER -> {
                call.respondRedirect("/identification".withBaseUrl())
                return true
            }
            UserModel.INE_OR_PASS_UPLOADED -> {
                call.respondRedirect("/upload/document?type=a".withBaseUrl())
                return true
            }
            UserModel.DOCUMENT_COMPLETE -> {
                call.respondRedirect("/person_info".withBaseUrl())
                return true
            }
            UserModel.SUMMARY_1_COMPLETE -> {
                if (userSession.isReturning) {
                    call.respondRedirect("/person_info".withBaseUrl())
                } else {
                    call.respondRedirect("/pre_clarification".withBaseUrl())
                }
                return true
            }
            UserModel.TC41_API_COMPLETE -> {
                call.respondRedirect("/pre_clarification".withBaseUrl())
                return true
            }
            UserModel.AUTH_CODE_TC42_ACCEPTED -> {
                call.respondRedirect("/supplementaryData".withBaseUrl())
                return true
            }
            UserModel.SUMMARY_2_COMPLETE -> {
                call.respondRedirect("/mobileData".withBaseUrl())
                return true
            }
            UserModel.MOBILE_DATA_COMPLETE -> {
                call.respondRedirect("/declaration".withBaseUrl())
                return true
            }
            UserModel.TC43_API_COMPLETE -> {
                call.respondRedirect("/declaration".withBaseUrl())
                return true
            }
            UserModel.TC44_API_COMPLETE -> {
                call.respondRedirect("/goodBye".withBaseUrl())
                return true
            }
            UserModel.SHIPPING_API_COMPLETE -> {
                call.respondRedirect("/goodBye".withBaseUrl())
                return true
            }
            else -> return false
        }
    }

}