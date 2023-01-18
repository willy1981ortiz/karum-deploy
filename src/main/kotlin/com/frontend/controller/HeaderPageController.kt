package com.frontend.controller

import com.Env
import com.controller.Controller
import io.ktor.server.application.*
import kotlinx.html.*

open class HeaderPageController(call: ApplicationCall) : Controller(call) {

    fun HTML.infoHeaderFile() {
        head {

            meta(charset = "utf-8")
            title("Karum Application")
            meta(name = "description", content = "Approva System")
            meta(name = "viewport", content = "width=device-width, initial-scale=1, shrink-to-fit=no")

            //<!--begin::Fonts -->
            link(
                rel = "stylesheet",
                href = "https://fonts.googleapis.com/css?family=Poppins:300,400,500,600,700|Asap+Condensed:500"
            )

            css("assets/css/info-style.css")
            css("assets/css/document-mobile.css")
            css(href = "https://use.fontawesome.com/releases/v5.15.4/css/all.css")
            css("https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css")


            jscript(src = "https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js")

            jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js")
            jscript(src = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js")
            jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.2/jquery.form.min.js")
            jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.19.1/jquery.validate.min.js")
            jscript(src = "assets/js/curp.js")

            script {
                unsafe {
                    +"""const mGBaseUrl = '${Env.BASE_URL}';"""
                }
            }
        }
    }

    fun HTML.headerLoginFile() {
        head {
            meta(charset = "utf-8")
            title("Karum Application")
            meta(name = "description", content = "Karum Application")
            meta(name = "viewport", content = "width=device-width, initial-scale=1, shrink-to-fit=no")

            //<!--begin::Fonts -->
            link(
                rel = "stylesheet",
                href = "https://fonts.googleapis.com/css?family=Poppins:300,400,500,600,700|Asap+Condensed:500"
            )

            css("assets/css/app.css")
            css("assets/css/splash.css")
            css("assets/css/mobile-auth-style.css")

            css("https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.0.2/css/bootstrap.min.css")
            css(href = "https://use.fontawesome.com/releases/v5.15.4/css/all.css")
            css("https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css")


            css("https://cdnjs.cloudflare.com/ajax/libs/intl-tel-input/17.0.8/css/intlTelInput.css")
            jscript("https://cdnjs.cloudflare.com/ajax/libs/intl-tel-input/17.0.8/js/intlTelInput.min.js")

            jscript(src = "https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js")

            script {
                unsafe {
                    +"""const mGBaseUrl = '${Env.BASE_URL}';""".trimMargin()
                }
            }
            //              for login only
            jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js")
            jscript(src = "https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js")
            jscript(src = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js")
            jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.2/jquery.form.min.js")
            jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.19.1/jquery.validate.min.js")

            jscript(src = "assets/js/scripts.bundle.min.js")

        }
    }
    fun HTML.headerInfoFile() {
        head {
            meta(charset = "utf-8")
            title("Karum Application")
            meta(name = "description", content = "Karum Application")
            meta(name = "viewport", content = "width=device-width, initial-scale=1, shrink-to-fit=no")

            //<!--begin::Fonts -->
            link(
                rel = "stylesheet",
                href = "https://fonts.googleapis.com/css?family=Poppins:300,400,500,600,700|Asap+Condensed:500"
            )

            css("assets/css/splash.css")
            css("assets/css/app.css")

            css(href = "https://use.fontawesome.com/releases/v5.15.4/css/all.css")
            css("https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css")


            css("https://cdnjs.cloudflare.com/ajax/libs/intl-tel-input/17.0.8/css/intlTelInput.css")
            jscript("https://cdnjs.cloudflare.com/ajax/libs/intl-tel-input/17.0.8/js/intlTelInput.min.js")

            jscript(src = "https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js")

            script {
                unsafe {
                    +"""const mGBaseUrl = '${Env.BASE_URL}';""".trimMargin()
                }
            }
            //              for login only
            jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js")
            jscript(src = "https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js")
            jscript(src = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js")
            jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.2/jquery.form.min.js")
            jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.19.1/jquery.validate.min.js")

            jscript(src = "assets/js/scripts.bundle.min.js")

        }
    }
}