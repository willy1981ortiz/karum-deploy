package com.plugins

import com.helper.DBHelper
import io.ktor.server.routing.*
import io.ktor.server.http.content.*
import io.ktor.server.application.*
import io.ktor.server.response.*

fun Application.configureRouting() {


    routing {
        // Static plugin. Try to access `/static/index.html`
        static("/assets") {
            resources("assets")
            files("resources/assets")
        }

        frontendRoutes()
        backendPageApis()

        get("healthz") {
            call.respondText("1")
        }

        get("healthz2") {
            DBHelper.instance.db.query("SElECT 1").execute()
            call.respondText("1")
        }
    }
}
