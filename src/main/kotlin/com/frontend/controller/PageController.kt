package com.frontend.controller

import com.Env
import com.helper.isValid
import com.helper.withBaseUrl
import com.model.DocumentModel
import com.model.PersonInfoModel
import com.model.UserModel
import com.plugins.ProductSession
import com.plugins.userSession
import com.tables.pojos.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import kotlinx.html.*
import java.util.*

class PageController(call: ApplicationCall) : TemplateController(call) {

    suspend fun landingPage() {
        val productId = call.parameters["i"]?.takeIf { it.isNotBlank() }
        val price = call.parameters["p"]?.takeIf { it.isNotBlank() }

        if (!productId.isNullOrBlank() && !price.isNullOrBlank()) {
            call.sessions.set(ProductSession(productId, price))
        }

        call.respondHtml(HttpStatusCode.OK) {
            // Head Part of Login Page
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

                css("assets/css/landing-page.css")
                css(href = "https://use.fontawesome.com/releases/v5.15.4/css/all.css")
                css("https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css")
                jscript(src = "https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js")

                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js")
                jscript(src = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js")
                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.2/jquery.form.min.js")
                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.19.1/jquery.validate.min.js")


                script {
                    unsafe {
                        +"""const mGBaseUrl = '${Env.BASE_URL}';"""
                    }
                }
            }

            unsafe {
                +"""<style>
           
                |.iti{position: relative; display: block;}
                |.iti__arrow{display: none;}
    
                </style>""".trimMargin()
            }

            body {
                // Splash load for five secons
//                Splash Screen Remove
//                div(classes = "preloader")
                // main landing page after splash
                div("container-fluid") {
                    div(classes = "row landingParent") {
                        div(classes = "col-md-6 landing-left") {
                            h1(classes = "PagarTitle") {
                                +"Pagar con"
                            }
                            img(classes = "karumLogo") {
                                src = "/assets/media/instant-logo.png"
                                height = "50px"
                                width = "200px"
                            }
                            /*p(classes = "landing-left-p") {
                                +"La comprobación de su elegibilidad no afectará a su puntuación de crédito"
                            }*/

                            button {
                                id = "applyBtnId"
                                onClick = "onApplyBtnClick(this)"
                                +"Solicita aquí tu crédito"
                            }

                            h4(classes = "howDosWorkTitle") {
                                +"cómo funciona"
                            }

                            div("stepperContainer") {
                                div("col-md-2") {
                                    div("number-style") {
                                        style = "margin:8px 0px;"
                                        span {
                                            +"1"
                                        }
                                        div {
                                            style = "border-left:1px dashed #182035; height:25px; margin-bottom:4px;"
                                        }
                                        span {
                                            +"2"
                                        }
                                        div {
                                            style = "border-left:1px dashed #182035; height:25px; margin-bottom:4px;"
                                        }
                                        span {
                                            +"3"
                                        }
                                        div {
                                            style = "border-left:1px dashed #182035; height:25px; margin-bottom:4px;"
                                        }
                                        span {
                                            +"4"
                                        }
                                    }
                                }
                                div("col-md-10") {
                                    style = "text-align:left"
                                    p(classes = "landing-stepper-p") {
                                        +"Ingresa toda la información que se solicita"
                                    }
                                    p(classes = "landing-stepper-p") {
                                        +"Incorpora identificación oficial vigente con firma y foto por ambos lados (INE o pasaporte)"
                                    }
                                    p(classes = "landing-stepper-p") {
                                        +"Incorpora tu último recibo de nómina"
                                    }
                                    p(classes = "landing-stepper-p") {
                                        +"Procesaremos tu solicitud de crédito y te notificaremos los siguientes pasos"
                                    }
                                }
                            }
                        }

                        div(classes = "col-md-6 landing-right") {
                            div(classes = "col-md-9") {
                                h5(classes = "landingFirstH5") {
                                    +"Registra ahora tus datos y solicita tu crédito"
                                }

                                p(classes = "landingFirstP") {
                                    +"Debes contar con la siguiente documentacion para iniciar tu registro:"
                                }
                            }

                            div(classes = "col-md-9 landingRightContainer") {
                                h5(classes = "identificationH5") {
                                    style = "color:#ff6700;"
                                    +"Identificación Oficial:"
                                }

                                h5 {
                                    style = "color:#182035; font-size:0.875rem; margin-bottom:18px; margin-top:16px"
                                    +"INE/IFE vigente"
                                }

                                h5 {
                                    style = "color:#182035; font-size:0.875rem; margin-bottom:18px; margin-top:16px"
                                    +"Pasaporte vigente"
                                }

                                h5 {
                                    style = "color:#ff6700; margin-bottom:16px;"
                                    +"Recibo de nómina"
                                }

                              /*  p(classes = "landing-right-p") { +"CFE luz" }
                                p(classes = "landing-right-p") { +"Agua" }
                                p(classes = "landing-right-p") { +"Predial" }
                                p(classes = "landing-right-p") { +"Telmex (línea  fija)" }
                                p(classes = "landing-right-p") { +"TV de paga cable fijo" }
                                p(classes = "landing-right-p") { +"Gas natural / Conexión fija" }
                                p(classes = "landing-right-p") { +"Contrato de arrendamiento a nombre del solicitante" }
                                p(classes = "landing-right-p") { +"Edos. de cta. bancarios a nombre del solicitante (cheques, ahorro, crédito, débito)" } */
                            }

                            div(classes = "rightLogoContainer") {
                                img(classes = "landingRightLogo") {
                                    src = "/assets/media/instant-logo.png"
                                    height = "80px"
                                }
                            }
                        }
                    }
                }

                jscript(src = "assets/js/landing-page.js")
            }
        }
    }

    suspend fun welcomePage() {
        val userSession = call.userSession

        val user: User = UserModel.getUser(userSession.mobile) ?: error("Invalid session")

        if (user.status == UserModel.NEW_USER) {
            call.respondRedirect("/identification".withBaseUrl())
            return
        }

        call.respondHtml(HttpStatusCode.OK) {
            // Head Part of Login Page
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
                css("assets/css/welcome-mobile.css")
                css(href = "https://use.fontawesome.com/releases/v5.15.4/css/all.css")
                css("https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css")
                jscript(src = "https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js")

                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js")
                jscript(src = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js")
                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.2/jquery.form.min.js")
                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.19.1/jquery.validate.min.js")


                script {
                    unsafe {
                        +"""const mGBaseUrl = '${Env.BASE_URL}';"""
                    }
                }
            }

            body {
                div("main-container") {
                    header(classes = "info-header") {
                        style = "margin:0;"
                        div("karum-logo") {
                            style = "display: flex; justify-content: center;"
                            img {
                                src = "/assets/media/instant-logo.png"
                                height = "65px"
                            }
                        }
                        /*h1(classes = "splash-title") {
                            style = "font-size:2.25rem; padding-top:30px;"
                            +"Eliminar approva"
                        }*/
                        val headText =
                            if (user.status < UserModel.TC44_API_COMPLETE) {
                                "Bienvenido, continua con tu solicitud, te llevará unos minutos."
                            } else {
                                "Complemento al proceso de originación de crédito"
                            }
                        h2(classes = "splash-subtitle") {
                            style = "text-align:center; font-size:2rem; padding:30px 0px;"
                            //+"Proceso de originación"
                            +headText
                        }
                    }

                    div(classes = "container welcomeContainer") {
                        form(method = FormMethod.post) {
                            div("container_row") {

                                if (user.status >= UserModel.TC44_API_COMPLETE) {
                                    h4(classes = "welcome-heading") { +"Bienvenido de nuevo" }
                                }

                                /*if (userSession.status == 9) {
                                    span {
                                        style = "display:block; text-align:center; margin-top:20px;"

                                        h5 {
                                            style = "display:inline-block; color:#ffb700; font-size:1.725rem;"
                                            +"Número de folio:"
                                        }
                                        h4 {
                                            style = "color:#000; display:inline-block; font-size:1.7rem;"
                                            +"${user?.confirmationCode ?: " "}"
                                        }
                                    }

                                    span(classes = "error") {
                                        id = "folioCodeErrorId"
                                        style = "font-size: 1.4rem; text-align:center; display:none;"
                                        +"Número de folio no válido o faltante"
                                    }
                                }*/

                                val btnText =
                                    if (user.status >= UserModel.SUMMARY_1_COMPLETE && user.status < UserModel.TC44_API_COMPLETE) {
                                        "Continuar solicitud"
                                    } else {
                                        "Subir documentos"
                                    }

                                div(classes = "iniciarBtnContainer") {
                                    a(classes = "btn btn-primary iniciarBtn") {
//                                        href = "/identification".withBaseUrl()
                                        onClick = "onClickContinueBtn(${user.status})"
                                        +btnText
                                    }
                                }

                            }
                        }
                    }
                }

                jscript(src = "assets/js/main.js")
                jscript(src = "assets/js/welcome-page.js")
            }
        }
    }

    suspend fun documentCheckPage() {
        val userSession = call.userSession
        val user = UserModel.getUser(userSession.mobile) ?: error("Invalid session!")
        val document = DocumentModel.getDocumentByUserId(userSession.userId)
        if (userSession.isReturning.not()) {
            if (pageRedirect()) return
        } else {
            /*if (user.status > UserModel.DOCUMENT_COMPLETE && user.status < UserModel.TC44_API_COMPLETE) {
                if (pageRedirect()) return
            }*/
        }

        call.respondHtml(HttpStatusCode.OK) {
            // Head Part of Login Page
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
                css("assets/css/document-preview-style.css")
                css(href = "https://use.fontawesome.com/releases/v5.15.4/css/all.css")
                css("https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css")
                jscript(src = "https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js")

                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js")
                jscript(src = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js")
                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.2/jquery.form.min.js")
                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.19.1/jquery.validate.min.js")

                script {
                    unsafe {
                        +"""const mGBaseUrl = '${Env.BASE_URL}';"""
                    }
                }
            }

            body {
                div("main-container") {
                    header(classes = "info-header") {
                        style = "margin:0;"
                        span("karum-logo") {
                            style = "display: flex; justify-content: center;"
                            img {
                                src = "/assets/media/instant-logo.png"
                                height = "65px"
                            }
                        }
                        h1(classes = "splash-title") {
                            style = "font-size:2.25rem; padding-top:25px;"
                            +"Centro de carga de documentos"
                        }
                    }

                    div(classes = "container documentPageContainer") {
                        form(method = FormMethod.post) {
                            style = "overflow:hidden;"

                            h4 {
                                style = "color:#ff6700; font-size:1.6rem; text-align:center;"
                                +"Da clic en el tipo de documento que deseas cargar"
                            }

                            div {
                                style = "width:100%; margin:80px auto 40px auto; text-align:center;"
                                span("documentUploadSpan") {
                                    h6(classes = "lastUpdateTitle") { +"Ultimo archivo" }
                                    p(classes = "documentUploadDate") {
                                        +"${document?.ineTimestamp ?: "YYYY-MM-DD"}"
                                    }
                                    a {
                                        style = "cursor:pointer;"
                                        href = if (document?.ineFront != null) {
                                            // "/documentPreview?type=".withBaseUrl()            preview already upload documents
                                            "/uploadDocument?type=".withBaseUrl()
                                        } else {
                                            // "/uploadDocument?type=".withBaseUrl()
                                            "/uploadDocument?type=".withBaseUrl()
                                        }
                                        img {
                                            src = "/assets/media/ine-upload.png"
                                            width = "90%"
                                        }
                                    }
                                }

                                span("documentUploadSpan") {
                                    h6(classes = "lastUpdateTitle") { +"Ultimo archivo" }
                                    p(classes = "documentUploadDate") {
                                        +"${document?.passpotTimestamp ?: "YYYY-MM-DD"}"
                                    }
                                    a {
                                        style = "cursor:pointer;"
                                        href = if (document?.passport != null) {
                                            // "/uploadDocument?type=p".withBaseUrl()           preview of already uploaded documents
                                            "/uploadDocument?type=p".withBaseUrl()
                                        } else {
                                            // "/uploadDocument?type=p".withBaseUrl()
                                            "/uploadDocument?type=p".withBaseUrl()
                                        }
                                        img {
                                            src = "/assets/media/pass-upload.png"
                                            width = "90%"
                                        }
                                    }
                                }

                                span("documentUploadSpan") {
                                    h6(classes = "lastUpdateTitle") { +"Ultimo archivo" }
                                    p(classes = "documentUploadDate") {
                                        +"${document?.poaTimestamp ?: "YYYY-MM-DD"}"
                                    }
                                    a {
                                        style = "cursor:pointer;"
                                        href = if (document?.proofOfAddress != null) {
                                            // "/uploadDocument?type=a".withBaseUrl()           preview of already uploaded documents
                                            "/uploadDocument?type=a".withBaseUrl()
                                        } else {
                                            // "/uploadDocument?type=a".withBaseUrl()
                                            "/uploadDocument?type=a".withBaseUrl()
                                        }
                                        img {
                                            src = "/assets/media/poa-upload.png"
                                            width = "90%"
                                        }
                                    }
                                }

                                if (user.status >= UserModel.TC44_API_COMPLETE) {
                                    span("documentUploadSpan") {
                                        h6(classes = "lastUpdateTitle") { +"Ultimo archivo" }
                                        p(classes = "documentUploadDate") {
                                            +"${document?.poiTimestamp ?: "YYYY-MM-DD"}"
                                        }
                                        a {
                                            style = "cursor:pointer;"
                                            href = if (document?.proofOfIncome != null) {
                                                // "/documentPreview?type=i".withBaseUrl()
                                                "/uploadDocument?type=i".withBaseUrl()
                                            } else {
                                                // "/uploadDocument?type=i".withBaseUrl()
                                                "/uploadDocument?type=i".withBaseUrl()
                                            }
                                            img {
                                                src = "/assets/media/poi-upload.png"
                                                width = "90%"
                                            }
                                        }
                                    }
                                }
                            }

                            if (user.status >= UserModel.TC44_API_COMPLETE) {
                                p {
                                    style =
                                        "width:70%; margin:auto; font-size:1rem; font-weight:bold; margin-bottom:25px;"
                                    /*+"""Despúes de haber cargado tus documentops solicitador por nuestro Call Center, se
                                        continuará con el proceso de análisis de crédito, en caso de requerir información 
                                        adicional te contactaremos llamando a tu número celular. Muchas gracias. """.trimMargin()*/

                                    +"""Despúes de haber cargado tus documentos solicitados por nuestra Central de Crédito, se 
                                    continuará con el proceso de análisis de crédito, en caso de requerir información 
                                    adicional te contactaremos llamando a tu número celular. Muchas gracias.""".trimMargin()
                                }
                            }

                            val forwardBtnText = if (user.status >= UserModel.TC44_API_COMPLETE) {
                                "Finalizar carga de documentos"
                            } else {
                                "Continuar solicitud"
                            }

                            a {
                                style =
                                    "float:right; margin-top:-15px; text-align:center; margin-right:10px; cursor:pointer;"
                                onClick = "onClickDocumentForwardBtn(${user.status})"
                                img {
                                    src = "/assets/media/forward.png"
                                    width = "60px"
                                    height = "60px"
                                }
                                p {
                                    style = "font-size:0.75rem; color:#ff6700; margin:0;"
                                    +forwardBtnText
                                }
                            }
                        }
                    }
                }

                jscript(src = "assets/js/main.js")
                jscript(src = "assets/js/document-check-page.js")
            }
        }
    }

    suspend fun documentPreviewPage() {
        val userSession = call.userSession
        val document = DocumentModel.getDocumentByUserId(userSession.userId)
        val type = call.parameters["type"]
        call.respondHtml(HttpStatusCode.OK) {
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
                css("assets/css/document-preview-style.css")
                css(href = "https://use.fontawesome.com/releases/v5.15.4/css/all.css")
                css("https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css")
                jscript(src = "https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js")

                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js")
                jscript(src = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js")
                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.2/jquery.form.min.js")
                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.19.1/jquery.validate.min.js")

                script {
                    unsafe {
                        +"""const mGBaseUrl = '${Env.BASE_URL}';"""
                    }
                }
            }

            body {
                div("main-container") {
                    header(classes = "info-header") {
                        span("karum-logo") {
                            style = "display:flex;justify-content: center;"
                            img {
                                src = "/assets/media/instant-logo.png"
                                height = "65px"
                            }
                        }
                        h1(classes = "splash-title-left") {
                            style = "font-size:2.25rem;"
                            +"APPROVA"
                        }
                    }

                    div(classes = "container") {
                        div {
                            h3(classes = "load_center_text") {
                                +"CENTRO DE CARGA"
                            }
                            h4 {
                                style =
                                    "color:#ffb700; text-align:center; margin-top:16px; margin-bottom:24px;text-decoration: underline;"
                                +"Identificación"
                            }
                            h5 {
                                style = "color: #ffb700; text-align: center; margin-bottom:24px;"
                                val subTitle = when (type) {
                                    "p" -> "Pasaporte"
                                    "a" -> "Comprobante de Domicilio (opcional)"
                                    "i" -> "Comprobante de Ingresos"
                                    else -> "IFE/INE"
                                }
                                +subTitle
                            }
                            h5(classes = "timestamp") {
                                val lastUpdate = when (type) {
                                    "p" -> "${document?.passpotTimestamp}"
                                    "a" -> "${document?.poaTimestamp}"
                                    "i" -> "${document?.poiTimestamp}"
                                    else -> "${document?.ineTimestamp}"
                                }
                                +lastUpdate
                            }
                        }

                        div("row") {
                            style = "width:80%; margin:auto; text-align:center"
                            if (type == "") {
                                val ineFront = Base64.getEncoder().encodeToString(document?.ineFront)
                                val ineBack = Base64.getEncoder().encodeToString(document?.ineBack)

                                div("col-md-6") {
                                    style = "padding:10px 15px;"
                                    img {
                                        src = "data:image/png;base64,$ineFront"
                                        width = "85%"
                                    }
                                }

                                div("col-md-6") {
                                    style = "padding:10px 15px;"
                                    img {
                                        src = "data:image/png;base64,$ineBack"
                                        width = "85%"
                                    }
                                }
                            } else {
                                val link = when (type) {
                                    "p" -> Base64.getEncoder().encodeToString(document?.passport)
                                    "a" -> Base64.getEncoder().encodeToString(document?.proofOfAddress)
                                    else -> Base64.getEncoder().encodeToString(document?.proofOfIncome)
                                }
                                div("col-md-3")
                                div("col-md-6 col-sm-12") {
                                    /*style = "display:flex; justify-content:center;"*/
                                    img {
                                        src = "data:image/png;base64,$link"
                                        width = "85%"
                                    }
                                }
                                div("col-md-3")
                            }
                        }

                        div {
                            h5 {
                                style = "color: #ffb700; text-align: center; margin-bottom:16px; margin-top:30px;"
                                +"Haga clic/toque el BOTÓN ADELANTE para continuar"
                            }

                            h6 {
                                style = "margin-bottom:16px;"
                                +"(Se sustituira el INE existente)"
                            }

                            a {
                                style = "cursor:pointer;"
                                onClick = "window.history.go(-1); return false;"
//                                href = "/document".withBaseUrl()
                                img {
                                    src = "/assets/media/back.png"
                                    width = "60px"
                                    height = "60px"
                                }
                            }

                            a {
                                style = "cursor:pointer; float:right;"
                                href = "/uploadDocument?type=$type".withBaseUrl()
                                img {
                                    src = "/assets/media/forward.png"
                                    width = "60px"
                                    height = "60px"
                                }
                            }
                        }

                    }
                }

//                jscript(src = "assets/js/document-check-page.js")
            }
        }
    }

    suspend fun calificationPage() {
        val userSession = call.userSession
        val user = UserModel.getUser(userSession.mobile) ?: error("Invalid session")
        if (!Env.DEBUG) {
            if (user.status < UserModel.SUMMARY_1_COMPLETE || user.status > UserModel.TC41_API_COMPLETE) {
                if (pageRedirect()) return
            }
        }
        val tc41Data = PersonInfoModel.getPersonInfoData(userSession.userId)

        if (tc41Data?.isValid() != true) {
            call.respondRedirect("/person_info".withBaseUrl())
            return
        }

        call.respondHtml(HttpStatusCode.OK) {
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

                css("assets/css/pre-clarification.css")
                css("assets/css/document-mobile.css")
                css("https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.2/css/all.min.css")
                css("https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css")
                jscript(src = "https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js")

                script {
                    unsafe {
                        +"""const mGBaseUrl = '${Env.BASE_URL}';""".trimMargin()
                    }
                }

                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js")
                jscript(src = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js")
                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.2/jquery.form.min.js")
                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.19.1/jquery.validate.min.js")

                jscript(src = "assets/js/scripts.bundle.min.js")
            }

            body(classes = "clarificationBody") {
                div("main-container") {
                    id = "main-container"
                    header(classes = "header") {
                        span("karum-logo2") {
                            img {
                                src = "/assets/media/instant-logo.png"
                                height = "65px"
                            }
                        }
                        /* h1(classes = "splash-title-left") {
                             style = "display:inline-block; text-shadow: 2px 2px #694620;"
                             +"APPROVA"
                         }*/
                        h2(classes = "splash-subtitle-left") {
                            style = "display:inline-block;"
                            +"Pre - calificación"
                        }
                        h4 {
                            style = "display:inline-block; color:#182035;"
                            +" (paso 1)"
                        }
                        h4(classes = "message") {
                            +"Te queremos conocer mejor, por favor responde las preguntas para poder hacerlo:"
                        }
                    }

                    div(classes = "container") {
                        form(classes = "clarificationFormWrapper") {
                            div(classes = "row") {
                                div(classes = "col-md-10 col-12") {
                                    p(classes = "radioStatement") {
                                        +"""Declaro bajo protesta de decir verdad que no desempeño actualmente 
                                            ni durante el año inmediato anterior algún cargo público destacado 
                                            a nivel  federal, estatal, municipal o distrito en México o en el 
                                            extranjero.""".trimMargin()
                                    }
                                }

                                div(classes = "col-md-2 col-12 calif-radio") {
                                    div(classes = "acceptRadioContainer") {
                                        input(
                                            classes = "form-check-input acceptRadioBtn",
                                            type = InputType.radio
                                        ) {
                                            name = "radioBtn01"
                                            checked = true
                                        }
                                        label(classes = "radioLabel") {
                                            +"Si acepto"
                                        }
                                    }

                                    div(classes = "notAcceptRadioContainer") {
                                        input(classes = "form-check-input", type = InputType.radio) {
                                            name = "radioBtn01"
                                        }
                                        label(classes = "radioLabel") {
                                            +"No acepto"
                                        }
                                    }
                                }
                            }
                            div(classes = "row") {
                                style = "margin-top:14px;"
                                div(classes = "col-md-10 col-12") {
                                    p(classes = "radioStatement") {
                                        +"""Declaro también que mi cónyuge, en su caso, o pariente por 
                                                    consanguineidad o afinidad hasta el 2° grado, no desempeña 
                                                    actualmente ni durante el año inmediato anterior ningún cargo 
                                                    público destacado a nivel federal, estatal, municipal o distrital 
                                                    en México o en el extranjero.""".trimMargin()
                                    }
                                }

                                div(classes = "col-md-2 col-12 calif-radio") {
                                    div(classes = "acceptRadioContainer") {
                                        input(classes = "form-check-input acceptRadioBtn", type = InputType.radio) {
                                            name = "radioBtn02"
                                            checked = true
                                        }
                                        label(classes = "radioLabel") {
                                            +"Si acepto"
                                        }
                                    }
                                    div(classes = "notAcceptRadioContainer") {
                                        input(classes = "form-check-input", type = InputType.radio) {
                                            name = "radioBtn02"
                                        }
                                        label(classes = "radioLabel") {
                                            +"No acepto"
                                        }
                                    }
                                }
                            }
                            div(classes = "row") {
                                style = "margin-top:14px;"
                                div(classes = "col-md-10 col-12") {
                                    p(classes = "radioStatement") {
                                        +"Declaro que ningún tercero obtendrá los beneficios derivados de las operaciones realizadas con “KARUM” ni ejercerá los derechos de uso, aprovechamiento o disposición de los recursos operados, siendo el verdadero propietario de los mismos."
                                    }
                                }

                                div(classes = "col-md-2 col-12 calif-radio") {
                                    div(classes = "acceptRadioContainer") {
                                        input(classes = "form-check-input acceptRadioBtn", type = InputType.radio) {
                                            name = "radioBtn03"
                                            checked = true
                                        }
                                        label(classes = "radioLabel") {
                                            +"Si acepto"
                                        }
                                    }
                                    div(classes = "notAcceptRadioContainer") {
                                        input(classes = "form-check-input", type = InputType.radio) {
                                            name = "radioBtn03"
                                        }
                                        label(classes = "radioLabel") {
                                            +"No acepto"
                                        }
                                    }
                                }
                            }
                            div(classes = "row") {
                                style = "margin-top:14px;"
                                div(classes = "col-md-10 col-12") {
                                    p(classes = "radioStatement") {
                                        +"Declaro que ningún tercero aportará regularmente recursos para el cumplimiento de las obligaciones derivadas del contrato que se establece con “KARUM” sin ser el titular de dicho contrato ni obtener los beneficios económicos derivados del mismo."
                                    }
                                }

                                div(classes = "col-md-2 col-12 calif-radio") {
                                    div(classes = "acceptRadioContainer") {
                                        input(classes = "form-check-input acceptRadioBtn", type = InputType.radio) {
                                            name = "radioBtn04"
                                            checked = true
                                        }
                                        label(classes = "radioLabel") {
                                            +"Si acepto"
                                        }
                                    }
                                    div(classes = "notAcceptRadioContainer") {
                                        input(classes = "form-check-input", type = InputType.radio) {
                                            name = "radioBtn04"
                                        }
                                        label(classes = "radioLabel") {
                                            +"No acepto"
                                        }
                                    }
                                }
                            }
                            div(classes = "row") {
                                style = "margin-top:14px;"
                                div(classes = "col-md-10 col-12") {
                                    p(classes = "radioStatement") {
                                        +"Declaro bajo protesta de decir verdad que para efectos de la realización de las operaciones con “KARUM” estoy actuando por cuenta propia."
                                    }
                                }

                                div(classes = "col-md-2 col-12 calif-radio") {
                                    div(classes = "acceptRadioContainer") {
                                        input(classes = "form-check-input acceptRadioBtn", type = InputType.radio) {
                                            name = "radioBtn05"
                                            checked = true
                                        }
                                        label(classes = "radioLabel") {
                                            +"Si acepto"
                                        }
                                    }

                                    div(classes = "notAcceptRadioContainer") {
                                        input(classes = "form-check-input", type = InputType.radio) {
                                            name = "radioBtn05"
                                        }
                                        label(classes = "radioLabel") {
                                            +"No acepto"
                                        }
                                    }
                                }
                            }
                            div(classes = "row") {
                                style = "margin-top:14px;"
                                div(classes = "col-md-10 col-12") {
                                    p(classes = "radioStatement") {
                                        +"Declaro que los recursos que utilizaré para el pago de este producto provienen de una fuente lícita. "
                                    }
                                }

                                div(classes = "col-md-2 col-12 calif-radio") {
                                    div(classes = "acceptRadioContainer") {
                                        input(classes = "form-check-input acceptRadioBtn", type = InputType.radio) {
                                            name = "radioBtn06"
                                            checked = true
                                        }
                                        label(classes = "radioLabel") {
                                            +"Si acepto"
                                        }
                                    }

                                    div(classes = "notAcceptRadioContainer") {
                                        input(classes = "form-check-input", type = InputType.radio) {
                                            name = "radioBtn06"
                                        }
                                        label(classes = "radioLabel") {
                                            +"No acepto"
                                        }
                                    }
                                }
                            }
                            div(classes = "row") {
                                div(classes = "col-md-10 col-sm-10") {
                                    p {
                                        id = "notAcceptError"
                                        style =
                                            "text-align:center; color:red; font-size:1.5rem; font-weight:bold; display:none;"
                                        +"Por favor lea y acepte"
                                    }
                                }
                                div(classes = "col-md-2 col-sm-2") {
                                    style = "padding-top:8px; text-align:right; padding-right:0px;"
                                    a(href = "#") {
                                        onClick = "showAuthModal(${user.status})"
                                        img {
                                            src = "/assets/media/forward.png"
                                            width = "60px"
                                            height = "60px"
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                div(classes = "modal fade auth-modal") {
                    id = "authModal"
                    role = "dialog"

                    div(classes = "modal-dialog modal-lg") {
                        div("modal-content") {
                            style =
                                "background-color:#fff; border:4px solid #ffdcB3; border-radius:10px; padding:0px 10px;"
                            div("modal-body") {

                                h4 {
                                    style = "color:#ff6700; text-align:left; display:inline-block;font-size: 1.4em;"
//                                    +"Autorización:"
                                    +"AUTORIZACIÓN BURÓ DE CRÉDITO:"
                                    span {
                                        style = "color:#000000; font-size:1rem"
                                        +" ${tc41Data.name} ${tc41Data.parentSurname} ${tc41Data.motherSurname} "
                                    }
                                }

                                unsafe {
                                    +"""
                                        <p style="font-size:1rem;">
                                        Por este conducto autorizo expresamente a <b>KARUM OPERADORA DE PAGOS S.A.P.I. DE 
                                        C.V., SOFOM E.N.R.</b>, para que por conducto de sus funcionarios facultados lleve 
                                        a cabo investigaciones, sobre mi comportamiento crediticio en las Sociedades de 
                                        Información Crediticia que estime conveniente.
                                        </p>""".trimMargin()
                                }
                                br

                                unsafe {
                                    +"""
                                        <p style="font-size:1rem;">
                                        Así mismo, declaro que conozco la naturaleza y alcance de las Sociedades de Información 
                                        Crediticias y de la información contenida en los reportes de crédito y reportes de 
                                        crédito especiales, declaro que conozco la naturaleza y alcance de la información 
                                        que se solicitará, del uso que <b>KARUM OPERADORA DE PAGOS S.A.P.I. DE C.V., SOFOM 
                                        E.N.R.</b>, hará de tal información y que ésta podrá realizar consultas periódicas 
                                        sobre mi historial crediticio o el de la empresa que represento, consintiendo que 
                                        esta autorización se encuentre vigente por un período de 3 años contados a partir 
                                        de su expedición y en todo caso durante el tiempo que se mantenga la relación 
                                        jurídica.
                                        </p>""".trimMargin()
                                }

                                form {
                                    method = FormMethod.post
                                    id = "otp_form"
                                    div {
                                        style =
                                            "width:100%; margin:auto; text-align:center; margin-top:20px; position: relative;"
                                        div {
                                            style =
                                                "display: flex;justify-content: center;flex-direction: column;align-items:center;"
                                            input(
                                                classes = "authInput block-copy-paste numeric numMaxLength",
                                                type = InputType.number
                                            ) {
                                                style = "width: 150px; text-align: center;"
                                                name = "auth_code"
                                                required = true
//                                                placeholder = "Para autorizar ingrese su NIP"
                                                placeholder = "# # # # # #"
                                                maxLength = "6"
                                                minLength = "6"
                                                onKeyDown = "return event.keyCode !== 69"
                                            }
                                        }

                                        a {
                                            style =
                                                "margin-left:90px; color:darkgrey; text-decoration:underline; font-size:0.8rem; position:absolute; top:8px; cursor:pointer;"
                                            onClick = "onClickResendOtp()"
                                            id = "reenviar_codigo_id"
                                            +"Reenviar código"
                                        }

                                        span(classes = "error") {
                                            id = "authCodeErrorId"
                                            style = "font-size:1rem; display:none;"
                                            +"Invalido, error"
                                        }
                                    }
                                }

                                p {
                                    style = "color:#ff6700; text-align: center; margin-top: 10px;"
                                    +"Para autorizar, captura tu código"
                                }

                                div(classes = "row checkBoxRowContainer") {
                                    div {
                                        div(classes = "col-lg-12 form-check form-check-inline") {
                                            style = "display:flex; justify-content:center;"
                                            input(classes = "form-check-input", type = InputType.checkBox) {
                                                style = "min-width: 20px; min-height: 20px;"
                                                id = "acceptDocuments1"
                                                value = ""
                                            }
                                            label(classes = "checkbox_label") {
                                                style =
                                                    "color:blue; text-decoration:underline; display:inline; text-align:left;"
                                                id = "acceptDocumentsLink1"
//                                                +"ACEPTO TÉRMINOS Y CONDICIONES DE MEDIOS ELECTRONICOS"
                                                +"ACEPTO TÉRMINOS Y CONDICIONES DEL USO DE MEDIOS ELECTRÓNICOS"
                                            }
                                        }
                                        span(classes = "error") {
                                            style = "font-size:0.75rem; display:none;"
                                            id = "acceptError1Id"
                                            +"Por favor, reconozca que esta de acuerdo con Términos y condiciones de medios electrónicos"
                                        }
                                    }
                                    div {
                                        div(classes = "col-lg-12 form-check form-check-inline") {
                                            style = "margin-top:25px; display:flex; justify-content:center;"
                                            input(classes = "form-check-input", type = InputType.checkBox) {
                                                style = "min-width: 20px; min-height: 20px;"
                                                id = "acceptDocuments2"
                                                value = ""
                                            }
                                            label(classes = "checkbox_label") {
                                                style =
                                                    "color:blue; text-decoration:underline; display:inline; text-align: left;"
                                                id = "acceptDocumentsLink2"
                                                +"ACEPTO AUTORIZACIÓN BURÓ DE CRÉDITO"
                                            }
                                        }
                                        span(classes = "error") {
                                            style = "font-size: 0.75rem; display:none;"
                                            id = "acceptError2Id"
                                            +"Por favor, reconozca que esta de acuerdo con Autorización buro de crédito"
                                        }
                                    }
                                    div {
                                        div(classes = "col-lg-12 form-check form-check-inline") {
                                            style = "margin-top:25px; display:flex; justify-content:center;"
                                            input(classes = "form-check-input", type = InputType.checkBox) {
                                                style = "min-width: 20px; min-height: 20px;"
                                                id = "acceptDocuments3"
                                                value = ""
                                            }
                                            label(classes = "checkbox_label") {
                                                style =
                                                    "color:blue; text-decoration:underline; display:inline; text-align: left;"
                                                id = "acceptDocumentsLink3"
//                                                +"ACEPTO TÉRMINOS Y CONDICIONES"
//                                                +"ACEPTO AVISO DE PRIVACIDAD E INTERCAMBIO E INTERCAMBIO DE INFORMACIÓN"
                                                +"ACEPTACION DEL AVISO DE PRIVACIDAD E INTERCAMBIO DE INFORMACIÓN"
                                            }
                                        }
                                        span(classes = "error") {
                                            style = "font-size: 0.75rem; display:none;"
                                            id = "acceptError3Id"
                                            +"Por favor, reconozca que esta de acuerdo con los Terminos y condiciones"
                                        }
                                    }
                                }

                                /*div {
                                    style = "width:80%; margin:auto; margin-top:25px; text-align:center;"
                                    div(classes = "form-check form-check-inline") {
                                        style = "display:flex; justify-content:center;"
                                        input(classes = "form-check-input", type = InputType.checkBox) {

                                        }
                                        label(classes = "checkbox_label") {
                                            style = "color:blue; text-decoration:underline; margin-left: 7px;"

                                            +"ACEPTO TÉRMINOS Y CONDICIONES DE MEDIOS ELECTRONICOS"
                                        }
                                    }
                                    span(classes = "error") {

                                    }

                                    div(classes = "form-check form-check-inline") {
                                        style = "display:flex; justify-content:center; margin-top:8px;"
                                        input(classes = "form-check-input", type = InputType.checkBox) {

                                        }
                                        label(classes = "checkbox_label") {
                                            style = "color: blue; text-decoration: underline; margin-left: 7px;"

                                            +"ACEPTO AUTORIZACIÓN BURÓ DE CRÉDITO"
                                        }
                                    }
                                    span(classes = "error") {

                                    }

                                    div(classes = "form-check form-check-inline") {
                                        style = "display:flex; justify-content:center; margin-top:8px;"
                                        input(classes = "form-check-input", type = InputType.checkBox) {

                                        }
                                    }
                                    span(classes = "error") {

                                    }
                                }*/

                                a {
                                    style = "float:left; margin-top:5px; margin-left:10px; visibility:hidden"
                                    onClick = "closeAuthModal();"
                                    img {
                                        src = "/assets/media/back.png"
                                        width = "60px"
                                        height = "60px"
                                    }
                                }

                                a {
                                    style = "float: right; margin-top:5px; margin-right:10px; cursor:pointer"
                                    onClick = "onValidateOTP(this)"
                                    img {
                                        src = "/assets/media/forward.png"
                                        width = "60px"
                                        height = "60px"
                                    }
                                }
                            }
                        }
                    }
                }

                div(classes = "modal fade success-modal") {
                    id = "revisionModal"
                    role = "dialog"

                    div(classes = "modal-dialog modal-lg") {
                        style = "width:90%; margin:auto; height:auto;"
                        div("modal-content") {
//                            style = "background-color:#858688;"
                            style = "background-color:#fff; border:4px solid #ffdcB3;"
                            div("modal-body") {
                                h4(classes = "dialog-title") {
                                    style = "color:#ff6700; text-align:center;"
                                    +"En revisión !"
                                }

                                div {
                                    style = "width:20%; margin:auto; margin-bottom:30px;"
                                    img {
                                        style = "text-align:center; margin-top:90px;"
                                        src = "/assets/media/tick.png"
                                        width = "100px"
                                        height = "100px"
                                    }
                                }

                                h4(classes = "dialog-message") {
//                                    +"Enhorabuena  - Tu solicitud ha sido pre - aprobada"
                                    +"Enhorabuena  - Tu solicitud está en proceso"
                                }

                                h5 {
                                    style =
                                        "color:#ff6700; text-align:center; margin-top:10px; font-size:0.875rem; margin-bottom:35px;"
                                    +"Completa la siguiente información para terminar el trámite"
                                }

                                a {
                                    style = "float:left; margin-top:5px; margin-left:10px;"
                                    onClick = "onDismissSuccessModel()"
                                    /*attributes.apply {
                                        put("data-dismiss", "modal")
                                    }*/
                                    img {
                                        src = "/assets/media/back.png"
                                        width = "60px"
                                        height = "60px"
                                    }
                                }

                                a {
                                    style = "float: right; margin-top:5px; margin-right:10px;"
                                    href = "/supplementaryData".withBaseUrl()
                                    img {
                                        src = "/assets/media/forward.png"
                                        width = "60px"
                                        height = "60px"
                                    }
                                }
                            }
                        }
                    }
                }

                div(classes = "modal fade success-modal") {
                    id = "authErrorModal"
                    role = "dialog"

                    div(classes = "modal-dialog modal-lg") {
                        style = "width:90%; margin:auto; height:auto;"
                        div("modal-content") {
//                            style = "background-color:#858688;"
                            style = "background-color:#fff; border:4px solid #ffdcB3;"
                            div("modal-body") {
                                h4(classes = "dialog-title") {
                                    style = "color:#ff6700; text-align:center;"
                                    +"Declinado"
                                }

                                div {
                                    style = "width:30%; margin:auto; margin-bottom:50px; text-align:center;"
                                    img {
                                        style = "text-align:center; margin-top:90px;"
                                        src = "/assets/media/sad-emoji.png"
                                        width = "180px"
                                        height = "180px"
                                    }
                                }

                                h4 {
                                    style = "text-align:center; color:#000;"
                                    +"Tú crédito ha sido declinado"
                                }

                                a {
                                    style = "float: right; margin-top:5px; margin-right:10px;"
                                    onClick = "onDismissDeclineModel()"
                                    img {
                                        src = "/assets/media/forward.png"
                                        width = "60px"
                                        height = "60px"
                                    }
                                }
                            }
                        }
                    }
                }

                div(classes = "container") {
                    id = "document1"
                    style = "display:none; height:80vh !important; overflow:auto;"

                    div(classes = "row") {
                        style = "padding:16px 30px; font-size:1.125rem; text-align:left;"
                        div(classes = "col-md-12") {
                            h5 {
                                style = "text-align:center; margin:20px 0px; color:#ff6700; font-weight:bold;"
//                                +"TERMINOS Y CONDICIONES"
                                +"TÉRMINOS Y CONDICIONES DEL USO DE MEDIOS ELECTRÓNICOS"
                            }

                            unsafe {
                                +"""<p style = "color:#182035;">
                                    <b>KARUM OPERADORA DE PAGOS S.A.P.I. DE C.V., SOFOM E.N.R</b>. (en lo sucesivo “<b>KARUM</b>”),le ofrece 
                                    a sus Solicitantes, Contratantes y/o Tarjetahabientes (a quienes para efectos de los 
                                    presentes términos y condiciones también se les denominará como “<b>Cliente</b>”), la posibilidad 
                                    de celebrar la contratación, modificación o cancelación de operaciones y servicios relativos 
                                    a los créditos y programas ofertados por “<b>KARUM</b>”, así como el efectuar Operaciones 
                                    Electrónicas por los diversos Medios Electrónicos que pone a su disposición, para lo 
                                    cual es IMPORTANTE que previamente a su contratación, lea detenidamente los términos 
                                    y condiciones para su uso que aquí se le informan.
                                    </p>""".trimMargin()
                            }

                            p {
                                style = "color:#182035;"
                                +"Definiciones"
                            }

                            ul {
                                style = "color:#182035;"
                                unsafe {
                                    +"""                                        
                                        <li>
                                        Tarjetahabiente. - es el nombre que recibe el usuario de una tarjeta de crédito, débito, prepago, garantizada y/o lealtad.
                                        </li>
                                        <li>
                                        Solicitante. – es la persona física que solicita a “<b>KARUM</b>” una tarjeta de crédito, débito, prepago, garantizada y/o lealtad.
                                        </li>
                                        Contratante. - es la persona que contrata las operaciones o servicios relativos  a los créditos o programas ofrecidos por “<b>KARUM</b>”.
                                        <li>
                                        Medios Electrónicos. - los equipos, medios ópticos o de cualquier otra tecnología, sistemas automatizados de procesamiento de datos y redes de telecomunicaciones, ya sean públicos o privados.
                                        </li>
                                        <li>
                                        Operaciones Electrónicas. - el conjunto de operaciones y servicios que “<b>KARUM</b>” realice con sus Clientes a través de Medios Electrónicos.
                                        </li>
                                    """.trimIndent()
                                }
                            }

                            unsafe {
                                +"""<p style = "color:#182035;">
                                    Al utilizar cualquiera de los servicios que “<b>KARUM</b>” pone a su disposición a través de 
                                    Medios Electrónicos considere que acepta tácitamente cumplir con los presentes términos 
                                    y condiciones, mismos que podrán actualizarse en cualquier momento y se pondrán a su 
                                    disposición en <a href="https://www.karum.com/storage/operadora-terminos-y-condiciones.pdf" target="_blank">https://www.karum.com/storage/operadora-terminos-y-condiciones.pdf</a>
                                    </p>""".trimMargin()
                            }

                            unsafe {
                                +"""<p style = "color:#182035;">
                                    Así mismo, le recordamos que “<b>KARUM</b>”se encuentra comprometida con la protección de sus 
                                    Datos Personales por lo que le invitamos a conocer nuestro Aviso de Privacidad 
                                    en <a href="https://www.karum.com/storage/operadora-aviso-de-privacidad-web.pdf" target="_blank">https://www.karum.com/storage/operadora-aviso-de-privacidad-web.pdf</a>
                                    </p>""".trimMargin()
                            }

                            unsafe {
                                +"""<p style = "color:#182035">
                                    En el momento que usted solicita los servicios o productos de <b>KARUM</b> está de acuerdo en 
                                    adquirir el carácter de cliente de la Sociedad. Ponemos a su disposición el aviso de 
                                    privacidad integral en la página de internet <a href="https://www.karum.com/storage/operadora-aviso-de-privacidad-web.pdf" target="_blank">https://www.karum.com/storage/operadora-aviso-de-privacidad-web.pdf</a> 
                                    en donde se le da a conocer el detalle del tratamiento que se les dará a sus datos 
                                    personales, así como los derechos que usted puede hacer valer. En este acto el titular 
                                    de los datos personales otorga su consentimiento expreso a <b>KARUM</b> para tratar sus datos 
                                    personales y para transferirlos a sociedades que formen parte del grupo económico de 
                                    <b>KARUM</b> y/o terceros (con los que <b>KARUM</b> tenga celebrados acuerdos comerciales, 
                                    independientemente de que dicho acuerdo haya concluido, incluidas sociedades de 
                                    información crediticia, proveedores, subsidiarias, directas o indirectas y partes 
                                    relacionadas) que realicen operaciones de contratación de créditos y prestación de 
                                    servicios, promociones, publicidad, recompensas y demás contemplados en nuestro aviso 
                                    de privacidad, así como para el mantenimiento o cumplimiento de una relación jurídica.
                                    </p>""".trimMargin()
                            }

                            /*p {
                                style = "color:#fff;"
                                +"del crédito."
                            }*/

                            /*p {
                                style = "color:#fff;"
                                +"""Acepto los términos y condiciones del contrato de adhesión registro REC 15227-440-035157/01-03640-0921. 
                                    Estoy conforme y por ende autorizo para que los datos aqui asentados sean investigados 
                                    a efecto de verificar su autenticidad. Asi mismo, estoy de acuerdo que mediante la 
                                    solicitud de los productos o servicios que realice a KUAL/ SERVICIOS INTEGRALES DE 
                                    EMPRENDIMIENTO S.A.PI. DE C.V., SOFOM E.N.R. (hoy KARUM OPERADORA DE PAGOS S.A.P.I.DE C.V., 
                                    SOFOM E.N.R.) en lo sucesivo "KARUM" obtendré el caracter de cliente, también estoy 
                                    de acuerdo que este documento es propiedad exclusiva de KARUM y declaro que he leido 
                                    y entendido el contrato de crédito que rige el manejo del crédito aquí solicitado, que una
                                    versión impresa del mismo fue puesta a mi disposición y con la firma a continuación 
                                    mi consentimiento para obligarme conforme a su clausulado, mismo que puedo
                                    consultar en la página de internet www.karum.com/credito-para-persond""".trimMargin()
                            }*/
                        }
                    }

                    div {
                        style = "text-align:right;"
                        a {
                            onClick = "onPrivacyForward()"
                            style = "cursor: pointer;"
                            img {
                                src = "/assets/media/forward.png"
                                width = "60px"
                                height = "60px"
                            }
                        }
                    }
                }

                div(classes = "container") {
                    id = "document2"
                    style = "display:none;"

                    div(classes = "row") {
                        style = "padding:16px 30px; font-size:1.125rem; text-align:left;"
                        div(classes = "col-md-12") {
                            h5 {
                                style = "text-align:center; margin:20px 0px; color:#ff6700; font-weight:bold;"
                                +"AUTORIZACIÓN BURÓ DE CRÉDITO"
                            }

                            unsafe {
                                +"""
                                    <p style="color:#182035;">
                                    Por este conducto autorizo expresamente a <b>KARUM OPERADORA DE PAGOS S.A.P.I. DE C.V., 
                                    SOFOM E.N.R.</b>, para que por conducto de sus funcionarios facultados lleve a cabo investigaciones, 
                                    sobre mi comportamiento crediticio en las Sociedades de Información Crediticia que estime 
                                    conveniente.
                                    </p>
                                    """.trimMargin()
                            }

                            unsafe {
                                +"""
                                    <p style="color:#182035;">
                                    Así mismo, declaro que conozco la naturaleza y alcance de las Sociedades de Información 
                                    Crediticias  y de la información contenida en los reportes de crédito y reportes de 
                                    crédito especiales, declaro que conozco la naturaleza y alcance de la información que 
                                    se solicitará, del uso que <b>KARUM OPERADORA DE PAGOS S.A.P.I. DE C.V., SOFOM E.N.R.</b>, 
                                    hará de tal información y que ésta podrá realizar consultas periódicas sobre mi historial 
                                    crediticio o el de la empresa que represento, consintiendo que esta autorización se 
                                    encuentre vigente por un período de 3 años contados a partir de su expedición y en todo 
                                    caso durante el tiempo que se mantenga la relación jurídica.
                                    </p>""".trimMargin()
                            }
                        }
                    }

                    div {
                        style = "text-align:right;"
                        a {
                            onClick = "onPrivacyForward()"
                            style = "cursor: pointer;"
                            img {
                                src = "/assets/media/forward.png"
                                width = "60px"
                                height = "60px"
                            }
                        }
                    }
                }

                div(classes = "container") {
                    id = "document3"
                    style = "display:none;"

                    div(classes = "row") {
                        style = "padding:16px 30px; font-size:1.125rem; text-align:left;"
                        div(classes = "col-md-12") {
                            h5 {
                                style = "text-align:center; margin:20px 0px; color:#ff6700; font-weight:bold;"
//                                +"TÉRMINOS Y CONDICIONES PARA USO DE MEDIOS ELECTRÓNICOS"
                                +"ACEPTACIÓN DEL AVISO DE PRIVACIDAD E INTERCAMBIO DE INFORMACIÓN"
                            }

                            unsafe {
                                +"""
                                    <p style="color:#182035; font-weight:normal;">
                                    <b>KARUM OPERADORA DE PAGOS S.A.P.I. DE C.V., SOFOM E.N.R</b>. (en lo sucesivo “<b>KARUM</b>”), con 
                                    domicilio para oír y recibir notificaciones en Blvd. Manuel Ávila Camacho No. 5, Interior 
                                    S 1000, Ed. Torre B, Piso 10, Of. 1045, Col. Lomas de Sotelo, Naucalpan de Juárez, Estado 
                                    de México, C.P. 53390, es el responsable del uso y protección de sus datos personales, en 
                                    ese contexto, le informa que los datos personales recabados, serán utilizados para la 
                                    operación de los productos que usted solicite o contrate, así como para hacerle llegar 
                                    información de promociones relacionadas con el mismo. En el momento que usted solicita 
                                    los servicios o productos de <b>KARUM</b> está de acuerdo en adquirir el carácter de cliente de 
                                    la Sociedad. Ponemos a su disposición el aviso de privacidad integral en la página de 
                                    internet <a href="https://www.karum.com/storage/operadora-aviso-de-privacidad-web.pdf" target="_blank">https://www.karum.com/storage/operadora-aviso-de-privacidad-web.pdf</a> en donde se 
                                    le da a conocer el detalle del tratamiento que se les dará a sus datos personales, así 
                                    como los derechos que usted puede hacer valer. En este acto el titular de los datos 
                                    personales otorga su consentimiento expreso a <b>KARUM</b> para tratar sus datos personales 
                                    y para transferirlos a sociedades que formen parte del grupo económico de <b>KARUM</b> y/o 
                                    terceros (con los que <b>KARUM</b> tenga celebrados acuerdos comerciales, independientemente 
                                    de que dicho acuerdo haya concluido, incluidas sociedades de información crediticia, 
                                    proveedores, subsidiarias, directas o indirectas y partes relacionadas) que realicen 
                                    operaciones de contratación de créditos y prestación de servicios, promociones, publicidad, 
                                    recompensas y demás contemplados en nuestro aviso de privacidad, así como para el 
                                    mantenimiento o cumplimiento de una relación jurídica.
                                    </p>""".trimMargin()
                            }

                            /*p {
                                style = "color:#fff;"
                                +"""KUALI SERVICIOS INTEGRALES DE EMPRENDIMIENTO S.A.PI. DE C.V.SOFOM E.N.R., (hoy KARUM OPERADORA DE PAGOS
                                S.A.PI. DE C.V., SOFOM E.N.R.) en adelante "KARUM", le ofrece a sus Solicitantes, Contratantes y/o Acreditados (a quienes
                                para efectos de los presentes términos y condiciones también se les denominará como "Cliente"), la posibilidad de celebrar
                                la contratación, modificación o cancelación de operaciones y servicios relativos a los créditos y programas ofertados por
                                KARUM, asi como el efectuar Operaciones Electrónicas a través de los diversos Medios Electrónicos que pone a su
                                disposición, para lo cual es IMPORTANTE que previamente a su contratación, lea detenidamente los términos y condiciones
                                para su uso que aquí se le informan.""".trimMargin()
                            }
                            p {
                                style = "color:#fff;"
                                +"""Definiciones""".trimMargin()
                            }
                            p {
                                style = "color:#fff;"
                                +"""• Acreditado es el nombre que recibe la persona a la cual se otorga un crédito y/o financiamiento.""".trimMargin()
                            }
                            p {
                                style = "color:#fff;"
                                +"""• Solicitante persona física que solicita a KARUM la aprobación de un crédito, financiamiento y/o programa de lealtad.""".trimMargin()
                            }
                            p {
                                style = "color:#fff;"
                                +"""• Contratante es la persona que contrata las operaciones o servicios relativos a los créditos o programas ofrecidos por KARUM.""".trimMargin()
                            }*/
                        }
                    }

                    div {
                        style = "text-align:right;"
                        a {
                            onClick = "onPrivacyForward()"
                            style = "cursor: pointer;"
                            img {
                                src = "/assets/media/forward.png"
                                width = "60px"
                                height = "60px"
                            }
                        }
                    }
                }

                modalLoader()
                jscript(src = "assets/js/main.js")
                jscript(src = "assets/js/preclarification.js")
            }
        }
    }

    suspend fun declarationPage() {
        val userSession = call.userSession
        val user = UserModel.getUser(userSession.mobile) ?: error("Invalid session!")
        if (!Env.DEBUG) {
            if (user.status < UserModel.MOBILE_DATA_COMPLETE || user.status >= UserModel.TC44_API_COMPLETE) {
                if (pageRedirect()) return
            }
        }
        call.respondHtml(HttpStatusCode.OK) {
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
                css("assets/css/declaration.css")
                css(href = "https://use.fontawesome.com/releases/v5.15.4/css/all.css")
                css("https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css")
                jscript(src = "https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js")

                script {
                    unsafe {
                        +"""const mGBaseUrl = '${Env.BASE_URL}';""".trimMargin()
                        +"""var mUserStatus = '${user.status}';""".trimMargin()
                    }
                }

                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js")
                jscript(src = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js")
                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.2/jquery.form.min.js")
                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.19.1/jquery.validate.min.js")

            }

            body {
                div("main-container") {
                    id = "main-container-declaration"
                    header(classes = "info-header declaration-header") {
                        span("karum-logo2") {
                            img {
                                src = "/assets/media/instant-logo.png"
                                height = "65px"
                            }
                        }
                        /*h1(classes = "splash-title-left") {
                            style = "display:inline-block;"
                            +"APPROVA"
                        }*/
                        h2(classes = "splash-subtitle-left") {
                            style = "display:inline-block; padding-top:15px;"
                            +"Pre - calificación"
                        }
                    }

                    div(classes = "container") {
                        div("row declaration-form-container") {
//                            style = "height:50vh; overflow:auto;"
                            div(classes = "row") {
                                style = "margin:20px 0px; font-size:1rem;"
                                div(classes = "col-md-12") {
                                    p {
                                        style = "font-weight:bolder;"
                                        +"""CONTRATO DE APERTURA DE CRÉDITO DE CARÁCTER INDIVIDUAL (EL “CONTRATO”) QUE CELEBRAN, POR UNA PARTE, KARUM OPERADORA DE PAGOS, S.A.P.I. DE C.V., SOFOM E.N.R.EN LO SUCESIVO LA “SOFOM”; Y POR OTRA PARTE, LA PERSONA CUYO NOMBRE APARECE EN LA SOLICITUD DE CRÉDITO QUE ANTECEDE AL PRESENTE CONTRATO Y QUE SE ADJUNTA COMO “ANEXO A”, EL CUAL FORMA PARTE INTEGRANTE DEL MISMO, EN LO SUCESIVO, EL “ACREDITADO”, CONFORME A LOS SIGUIENTES ANTECEDENTES, DECLARACIONES Y CLÁUSULAS:""".trimMargin()
                                    }

                                    h5(classes = "declarationAntecedents") { +"ANTECEDENTES" }
                                    unsafe {
                                        +"""
                                            <p> 
                                            <b>I.</b> El <b>ACREDITADO</b> ingresó una solicitud por medios electrónicos para el otorgamiento de un crédito (en lo sucesivo la “<b>SOLICITUD</b>”), a través de la aplicación móvil de la <b>SOFOM</b> (en lo sucesivo la “<b>APLICACIÓN</b>”) disponible para dispositivos Android e iOS, o a través del portal de la <b>SOFOM</b> denominado “<b>APPROVA</b>”, la cual se adjunta como <b>ANEXO A.</b>
                                            <br> <br>
                                            <b>II.</b> El <b>ACREDITADO</b> tuvo a su disposición en el portal <b>APPROVA</b> y/o la <b>APLICACIÓN</b> de la <b>SOFOM</b> los siguientes documentos: (i) el Aviso de Privacidad vigente de la <b>SOFOM</b> (ii) los términos y condiciones de uso del portal y de la <b>APLICACIÓN</b>, así como (iii) el Contrato y sus anexos, mismos que fueron aprobados y/o aceptados por el <b>ACREDITADO</b> a través de medios electrónicos.
                                            </p>
                                            """.trimMargin()
                                    }
                                    h5(classes = "declarationAntecedents") { +"DECLARACIONES" }
                                    unsafe {
                                        +"""
                                            <p>
                                            Declara la “<b>SOFOM</b>”, a través de su representante legal:
                                            </p>
                                            """.trimMargin()
                                    }
                                    unsafe {
                                        +"""
                                            <p>
                                            a)  Que KUALI SERVICIOS INTEGRALES DE EMPRENDIMIENTO, S.A.P.I DE C.V., SOFOM, E.N.R. (hoy KARUM OPERADORA DE PAGOS, S.A.P.I. DE C.V., SOFOM E.N.R.), se constituyó al amparo de las leyes mexicanas en los términos del Artículo 87-B (ochenta y siete guion B) de la Ley General de Organizaciones y Actividades Auxiliares del Crédito, como lo acredita con el instrumento notarial número 130,843 (ciento treinta mil ochocientos cuarenta y tres), volumen 1,829 (mil ochocientos veintinueve), del protocolo del Licenciado Raúl Sicilia Alamilla, titular de la Notaría número 1 de la Ciudad de Tula, Hidalgo, de fecha 22 de enero de 2019, inscrito en el Registro Público de Comercio en Cuautitlán Estado de México, bajo el folio mercantil número 2019009747 de fecha 11 de febrero de 2019; quedando facultada para operar como una Sociedad Financiera de Objeto Múltiple, Entidad No Regulada, de conformidad con lo establecido en la Ley General de Sociedades Mercantiles y la Ley General de Organizaciones y Actividades Auxiliares del Crédito en vigor. Asimismo, que la <b>SOFOM</b> adoptó la modalidad de Sociedad Anónima Promotora de Inversión de Capital Variable, en términos de la Ley General de Sociedades Mercantiles y la Ley del Mercado de Valores.
                                            <br> <br>
                                            b)	Que KUALI SERVICIOS INTEGRALES DE EMPRENDIMIENTO, S.A.P.I DE C.V., SOFOM, E.N.R. cambió de denominación a KARUM OPERADORA DE PAGOS, S.A.P.I DE C.V., SOFOM, E.N.R., tal como se acredita con el instrumento notarial número 123,487 (ciento veintitrés mil cuatrocientos ochenta y siete), de fecha 6 de agosto de 2021, pasado ante la fe del Lic. Arturo Sobrino Franco titular de la notaría número 49 de la Ciudad de México.
                                            <br> <br>
                                            c)	 Que de conformidad con el Artículo 87 J de la Ley General de Organizaciones y Actividades Auxiliares del Crédito, la <b>SOFOM</b> para su constitución y operación no requiere de autorización de la Secretaría de Hacienda y Crédito Público y está sujeta a la supervisión de la Comisión Nacional Bancaria y de Valores únicamente para efectos de lo dispuesto por el artículo 56 de la ley en comento, tal como se indica en la página de internet <a href="https://karum.com/operadora" target="_blank">https://karum.com/operadora</a>.
                                            <br> <br>
                                            d)	 Que se encuentra debidamente inscrita en el Registro Federal de Contribuyentes bajo el número KSI190122S23.
                                            <br> <br>
                                            e)	 Que su domicilio para oír y recibir todo tipo de notificaciones está ubicado en Blvd. Manuel Ávila Camacho No. 5, Interior S 1000, Edificio Torre B, Piso 10, Oficina 1045, Col. Lomas de Sotelo, Código Postal 53390, Naucalpan de Juárez, Estado de México.
                                            <br> <br>
                                            f)	 Que se encuentra legitimada para otorgar créditos simples y en cuenta corriente a los <b>ACREDITADOS</b>, para ser dispuestos en cualesquiera tiendas participantes (en lo sucesivo, “<b>LOS ESTABLECIMIENTOS</b>”) y/o cualquier otro medio autorizado por la <b>SOFOM</b> a los <b>ACREDITADOS</b> que habiendo llenado su SOLICITUD, cumplan con el perfil necesario, a fin de que puedan realizar disposiciones de una línea de crédito (por línea de crédito se entiende aquel monto máximo de dinero puesto a disposición del ACREDITADO con motivo del crédito simple y/o en cuenta corriente que le sea otorgado, en lo sucesivo “LÍNEA DE CRÉDITO”) dentro del territorio nacional a discreción de la <b>SOFOM</b>.
                                            <br> <br>
                                            g)	 Que entre su objeto social, está la realización habitual y profesional de una o más de las actividades de otorgamiento de crédito, arrendamiento financiero y/o factoraje financiero en los términos de la Ley General de Organizaciones y Actividades Auxiliares de Crédito.
                                            <br> <br>
                                            h)   Que el presente Contrato cuenta con un registro vigente ante el Registro de Contratos de Adhesión de la <b>CONDUSEF</b> bajo el número: [*].
                                            <br> <br>
                                            i)   Que el <b>ACREDITADO</b> le otorgó su autorización para realizar la investigación sobre su historial crediticio en los términos de lo previsto por la Ley para Regular las Sociedades de Información Crediticia.
                                            </p>
                                            """.trimMargin()
                                    }
                                    unsafe {
                                        +"""
                                            <p>
                                            Declara el <b>ACREDITADO</b>, por su propio derecho:
                                            </p>
                                            """.trimMargin()
                                    }
                                    unsafe {
                                        +"""
                                            <p>
                                            a)	 Ser una persona física mayor de edad con la capacidad legal necesaria para celebrar el presente Contrato y obligarse en los términos que en el mismo se establecen.
                                            <br> <br>
                                            b)	 Que la información proporcionada a la <b>SOFOM</b>, así como aquella contenida en la <b>SOLICITUD</b> que se adjunta al presente Contrato como <b>ANEXO A</b>, es cierta, correcta y completa; así como que está enterado y conoce de las penas y responsabilidades legales en que incurren las personas que al solicitar un crédito o para obtenerlo, falsean, alteran u ocultan información relevante para el otorgamiento o negativa del mismo.  
                                            <br> <br>
                                            c)	 Que ha otorgado su autorización a la <b>SOFOM</b> para realizar investigaciones sobre su historial crediticio en términos de lo previsto por la Ley para Regular las Sociedades de Información Crediticia.
                                            <br> <br>
                                            d)   Que ha leído el presente Contrato, comprende sus alcances legales, consecuencias de los derechos y obligaciones que asume y está de acuerdo con todas y cada una de las estipulaciones contenidas en el presente, por lo que al firmarlo se obliga a sujetarse en los términos y condiciones aquí pactadas relativas a estructura y operación del crédito que se le otorga, su plazo, tasa y consecuencias por el incumplimiento de sus obligaciones.
                                            <br> <br>
                                            e)   Que los recursos con los cuales ha de pagar el crédito dispuesto han sido o serán obtenidos o generados a través de una fuente de origen lícito y que el destino que dará a los recursos obtenidos al amparo del presente Contrato será tan sólo a fines permitidos por la ley y que no se encuentran dentro de los supuestos establecidos en los artículos 139 Quáter y 400 bis del Código Penal Federal.
                                            <br> <br>
                                            f)   Que con anterioridad al llenado de la <b>SOLICITUD</b> y previo a la celebración del presente Contrato, la <b>SOFOM</b> puso a su disposición su Aviso de Privacidad, en términos de la Ley Federal de Protección de Datos Personales en Posesión de los Particulares (en lo sucesivo “<b>LFPDPPP</b>”), donde se señala, además del tratamiento que se dará a sus datos personales, los derechos de acceso, rectificación, cancelación, oposición, revocación, limitación en el uso y/o divulgación con los que cuenta y la forma cómo los puede hacer valer.
                                            <br> <br>
                                            g)   Que es su voluntad celebrar el presente Contrato a través de medios electrónicos, manifestando su consentimiento a través de los procesos de autenticación establecidos en la <b>APLICACIÓN</b> y/o <b>APPROVA</b> de la <b>SOFOM</b>, de conformidad con los artículos 80, 89 bis, 90 y 93 del Código de Comercio.
                                            </p>
                                            """.trimMargin()
                                    }
                                    h5(classes = "declarationAntecedents") { +"CLÁUSULAS:" }
                                    unsafe {
                                        +"""
                                            <p>
                                            <b>PRIMERA. OBJETO Y LÍMITE DE CRÉDITO</b>. Una vez que (i) la <b>SOLICITUD</b> sea aprobada y (ii) el presente Contrato se haya celebrado; la <b>SOFOM</b> otorgará al <b>ACREDITADO</b> una <b>LÍNEA DE CRÉDITO</b> disponible en crédito simple o en cuenta corriente en moneda nacional, con límite hasta por la cantidad indicada en la carátula de crédito del Contrato, misma que se agrega a este instrumento como <b>ANEXO B</b>: al respecto, el <b>ACREDITADO</b> manifiesta que tiene capacidad económica suficiente para cumplir con los pagos de manera oportuna por el límite concedido (en adelante “<b>LÍMITE DE CRÉDITO</b>”), incluyendo las variaciones de dicha LÍNEA DE CRÉDITO según lo previsto en las cláusulas de este Contrato.
                                            <br> <br>
                                            Dentro del <b>LÍMITE DE CRÉDITO</b> no quedan comprendidos los intereses, las comisiones y los gastos de cobranza que se indican en el <b>ANEXO B</b>.
                                            <br> <br>
                                            Para otorgar el crédito, la <b>SOFOM</b> ha tomado en cuenta los análisis de información cuantitativa y cualitativa del <b>ACREDITADO</b>.
                                            <br> <br>
                                            La <b>SOFOM</b> sólo podrá otorgar crédito a personas mayores de edad, quedando prohibido el otorgamiento del mismo a personas menores de edad o incapaces legalmente.
                                            <br> <br>
                                            Además de la celebración de este Contrato es necesario que el <b>ACREDITADO</b> acepte a través de la <b>APLICACIÓN</b>, los términos y condiciones bajo los cuales la <b>SOFOM</b> autorizará su <b>SOLICITUD</b> por medios electrónicos.
                                            <br> <br>
                                            El Contrato y sus anexos serán enviados al <b>ACREDITADO</b> por correo electrónico y se mantendrán a su disposición en el portal de internet <a href="https://karum.com/operadora-activacel" target="_blank">https://karum.com/operadora-activacel</a> y la <b>APLICACIÓN</b>.
                                            <br> <br>
                                            El <b>ACREDITADO</b> reconoce y acepta que la <b>SOFOM</b> podrá formular oferta(s) al <b>ACREDITADO</b> para incrementar el <b>LÍMITE DE CRÉDITO</b> de acuerdo con las políticas internas de la <b>SOFOM</b>. Asimismo, en términos del artículo 294 de la Ley General de Títulos y Operaciones de Crédito, el <b>ACREDITADO</b> autoriza a la <b>SOFOM</b> a disminuir el <b>LÍMITE DE CRÉDITO</b> de conformidad con las políticas internas de la <b>SOFOM</b> notificándole por escrito o por medios electrónicos al ACREDITADO de este hecho.
                                            </p>
                                            """.trimMargin()
                                    }
                                    unsafe {
                                        +"""
                                            <p>
                                            Para que la <b>SOFOM</b> incremente o disminuya el <b>LÍMITE DE CRÉDITO</b> considerará: (i) el historial crediticio del <b>ACREDITADO</b> en los últimos 6 (seis) meses, (ii) los cargos realizados, (iii) los pagos efectuados, y (iv) el comportamiento en el cumplimiento de sus obligaciones crediticias derivadas de este instrumento y con terceros.
                                            <br> <br>
                                            Tratándose de incrementos al <b>LÍMITE DE CRÉDITO</b>, la <b>SOFOM</b> le notificará al <b>ACREDITADO</b> una oferta para incrementarlo, de manera escrita, mediante el estado de cuenta, correo electrónico, telefónicamente y/o bien, por cualquier medio electrónico. Una vez notificado, el <b>ACREDITADO</b> tendrá un plazo máximo de 60 (sesenta) días para aceptar expresamente el aumento de su <b>LÍMITE DE CRÉDITO</b> por cualquier medio indicado en la notificación de la oferta. Transcurrido dicho plazo caducará la oferta de incrementar el <b>LÍMITE DE CRÉDITO</b> y la <b>SOFOM</b> mantendrá el <b>LÍMITE DE CRÉDITO</b> sin cambio alguno.
                                            <br> <br>
                                            La <b>SOFOM</b> podrá declarar inactiva la cuenta del <b>ACREDITADO</b> y podrá revocar el crédito otorgado, si transcurren 6 (seis) meses sin compra y/o cargo alguno realizado. La <b>SOFOM</b> podrá reactivar la cuenta del <b>ACREDITADO</b> a solicitud del mismo siempre y cuando éste siga cumpliendo con los requisitos iniciales de contratación, como lo es tener un puntaje suficiente de score creditico a juicio de la <b>SOFOM</b>, en la Sociedad de Información Crediticia.
                                            <br> <br>
                                            <b>SEGUNDA. MEDIOS DE DISPOSICIÓN.</b> El <b>ACREDITADO</b> contará con diversas opciones que la <b>SOFOM</b> le habilitará para realizar la(s) disposición(es) de su crédito simple y/o en cuenta corriente y/o de su <b>LÍNEA DE CRÉDITO</b>, entre los que se incluyen, pero no se limitan: medios electrónicos, aplicaciones móviles, tarjeta plástica, tarjeta digital o vinculada y/o disposiciones en efectivo dentro de territorio nacional, a discreción de la <b>SOFOM</b> (en adelante “<b>MEDIOS DE DISPOSICIÓN</b>”).
                                            <br> <br>
                                            Tratándose de órdenes de compra y/o servicios que el <b>ACREDITADO</b> y/o la(s) persona(s) que este haya autorizado como adicional(es) para disponer de su <b>LÍNEA DE CRÉDITO</b>, que sean efectuados dentro de territorio nacional y/o en el extranjero por vía telefónica, internet u otros medios electrónicos, conforme a los términos de la autorización proporcionada por la <b>SOFOM</b>, el <b>ACREDITADO</b> quedará como responsable único por las compras y servicios adquiridos, así como de verificar que la persona o medio al cual le proporcione la información sobre sus <b>MEDIOS DE DISPOSICIÓN</b> está verdaderamente autorizada o vinculada con el establecimiento para tal efecto. Tratándose de operaciones electrónicas que se realicen por teléfono o a través de internet, se llevarán a cabo los procesos de verificación de identidad del <b>ACREDITADO</b> que se estimen necesarios.
                                            <br> <br>
                                            La <b>SOFOM</b> no asume responsabilidad en caso de que otras instituciones o establecimientos se rehúsen a admitir el uso de los <b>MEDIOS DE DISPOSICIÓN</b> ni en caso de que no puedan efectuarse disposiciones debido a cualquier tipo de desperfecto y/o suspensión del servicio en equipos automatizados, cajeros automáticos, sistemas telefónicos y/o electrónicos, entre otros.
                                            <br> <br>
                                            El <b>ACREDITADO</b>, cuando la <b>SOFOM</b> así lo habilite, podrá disponer en efectivo de la <b>LÍNEA DE CRÉDITO</b> mediante la obtención de dinero en efectivo a través de la red de comisionistas, y/o cajeros automáticos, lo anterior mediante la suscripción de comprobantes de disposición o de otros documentos o medios que sean aceptados por la <b>SOFOM</b> y/o a través de cualquier <b>MEDIO DE DISPOSICIÓN</b> inclusive electrónicos que estén habilitados por la <b>SOFOM</b>, dentro de los límites, condiciones y comisiones que se tengan establecidos. La <b>SOFOM</b> podrá restringir o limitar las disposiciones de efectivo de la <b>LÍNEA DE CRÉDITO</b> lo cual informará al <b>ACREDITADO</b> en su estado de cuenta. En todo momento, las disposiciones en efectivo de la <b>LÍNEA DE CRÉDITO</b> serán consideradas como disposiciones del crédito simple y/o en cuenta corriente y de la propia <b>LÍNEA DE CRÉDITO</b>.
                                            </p>
                                            """.trimMargin()
                                    }
                                    unsafe {
                                        +"""
                                            <p>
                                            Por razones de identificación o de seguridad y a solicitud de la <b>SOFOM</b>, de <b>LOS ESTABLECIMIENTOS</b> y/o comisionistas, el <b>ACREDITADO</b> acepta presentar, cuando le sea requerida, una identificación oficial vigente con fotografía y firma, al realizar disposiciones de la <b>LÍNEA DE CRÉDITO</b>.
                                            <br> <br>
                                            El <b>ACREDITADO</b> será responsable del mal uso que se haga del Número de Identificación Personal (en lo sucesivo "<b>NIP</b>") o de cualquier otra firma electrónica y/o de cualquier clave de acceso o de identificación personal que se implemente en el futuro, que permitan la utilización de los <b>MEDIOS DE DISPOSICIÓN</b> y demás medios electrónicos, sistemas electrónicos, automatizados o telecomunicación.
                                            <br> <br>
                                            En caso que el <b>ACREDITADO</b> convenga con la <b>SOFOM</b> utilizar una tarjeta plástica (en adelante la “<b>TARJETA PLÁSTICA</b>”) o digital (en adelante la “<b>TARJETA DIGITAL</b>”) como <b>MEDIOS DE DISPOSICIÓN</b> de su <b>LÍNEA DE CRÉDITO</b> (en adelante conjunta e indistintamente la “<b>TARJETA</b>” o las “<b>TARJETAS</b>” en plural), la <b>SOFOM</b> deberá de poner a disposición del <b>ACREDITADO</b> dichas <b>TARJETAS</b> para su uso exclusivo, las cuales serán personales e intransferibles, en el entendido que serán de exclusiva propiedad de la <b>SOFOM</b> y que conservará el <b>ACREDITADO</b> en calidad de depositario.
                                            <br> <br>
                                            El <b>ACREDITADO</b> podrá solicitar por escrito o a través de medios electrónicos a la <b>SOFOM</b> la expedición de TARJETAS adicionales compartiendo el <b>LÍMITE DE CRÉDITO</b> otorgado en el presente Contrato, en su totalidad o solo un porcentaje de éste según sea requerido por el <b>ACREDITADO</b>, quedando a entera discreción de la <b>SOFOM</b> el aceptar o rechazar este tipo de solicitudes sobre <b>TARJETAS</b> adicionales.
                                            <br> <br>
                                            Una vez que el <b>ACREDITADO</b> reciba su <b>TARJETA FÍSICA</b> y previo a su utilización, el <b>ACREDITADO</b> deberá firmarla y a partir de ese momento quedará como único responsable del uso de la misma. Los mismos términos aplicarán para el caso de <b>TARJETAS FÍSICAS</b> adicionales. Por lo anterior, la <b>SOFOM</b> queda liberada de toda responsabilidad por el uso indebido de dichas <b>TARJETAS FÍSICAS</b>. El <b>ACREDITADO</b> y/o las personas autorizadas para utilizar las <b>TARJETAS</b> adicionales, por el simple uso y aceptación de las <b>TARJETAS</b>, se obligan tácitamente a cumplir con todas y cada una de las estipulaciones del presente Contrato, incluyendo sus anexos. Los usuarios de las <b>TARJETAS</b> adicionales que, en su caso sean expedidas, no serán considerados como obligados solidarios del <b>ACREDITADO</b>.
                                            <br> <br>
                                            Como medida de seguridad la <b>SOFOM</b> utiliza al menos un elemento para autenticar las operaciones autorizadas por el <b>ACREDITADO</b> y/o usuarios de las <b>TARJETAS</b> adicionales, ya sea al momento de realizar la operación, o bien, al momento de entregar el bien o servicio adquirido en virtud de dicha operación. El referido elemento puede ser información que la <b>SOFOM</b> proporcione al <b>ACREDITADO</b> o usuarios de las <b>TARJETAS</b> adicionales o permita a estos generar, para ingresarlas al sistema, tales como contraseña, <b>NIP</b>, cualquier otra firma electrónica o información contenida o generada por medios o dispositivos electrónicos, así como información derivada de características propias del <b>ACREDITADO</b> o usuarios de las <b>TARJETAS</b> adicionales en su caso,  tales como huellas dactilares, geometría de la mano o de la cara, patrones de retina, entre otros, o aquella otra información autorizada por Banco de México.
                                            <br> <br>
                                            El <b>ACREDITADO</b> acepta que como medio de manifestación de su voluntad, en el uso de los <b>MEDIOS DE DISPOSICIÓN</b> y/o en las disposiciones de la <b>LÍNEA DE CRÉDITO</b>, se utilizará, según sea el caso, su firma autógrafa, la presentación y/o uso de la <b>TARJETAS</b> en establecimientos y/o comisionistas y/o su Firma Electrónica y/o <b>NIP</b>. El suministro de la información de las <b>TARJETAS</b> vía voz (teléfono), o por medios electrónicos, o la presentación y uso de las <b>TARJETAS</b> en otros equipos electrónicos o automatizados de <b>LOS ESTABLECIMIENTOS</b>, constituye también medios de manifestación de la voluntad.
                                            <br> <br>
                                            <b>TERCERA. ENTREGA Y ACTIVACIÓN DE LA TARJETA PLÁSTICA</b>. Una vez que (i) la <b>SOLICITUD</b> sea aprobada y (ii) el presente Contrato se haya celebrado, la <b>SOFOM</b> dentro de un plazo que no excederá de 15 (quince) días hábiles posteriores a la celebración del presente acuerdo, le entregará al <b>ACREDITADO</b> personalmente (o a la persona que se identifique y se encuentre en el domicilio señalado en la <b>SOLICITUD</b>) la(s) <b>TARJETA(s) FÍSICA(S)</b> que la <b>SOFOM</b> hubiere emitido para uso exclusivo del <b>ACREDITADO</b> y/o sus adicionales.
                                            <br> <br>
                                            El plazo establecido en el párrafo que antecede para la entrega de la(s) <b>TARJETA(S) FÍSICA(S)</b> podrá ser modificado si por causas imputables al <b>ACREDITADO</b> no se puede realizar la entrega; incluyendo, de forma enunciativa más no limitativa, que el <b>ACREDITADO</b> haya proporcionado de forma errónea o incompleta los datos de su domicilio y/o domicilio alterno.
                                            <br> <br>
                                            Las <b>TARJETAS FÍSICAS</b> se entregarán desactivadas y para su activación el <b>ACREDITADO</b> deberá: (i) solicitar activarlas vía telefónicay seguir el proceso correspondiente en el Centro de Atención Telefónica de la <b>SOFOM</b>.
                                            <br> <br>
                                            No serán procedentes los cargos a las <b>TARJETAS</b> que no estén activadas, tampoco serán aplicables otros cargos previamente autorizados por el <b>ACREDITADO</b>, cuando se sustituyan las <b>TARJETAS FÍSICAS</b> y éstas no hayan sido activadas.
                                            <br> <br>
                                            <b>CUARTA. ASIGNACIÓN DE NIP Y RECOMENDACIONES</b>. Uno de los factores de autenticación del <b>ACREDITADO</b> que podrá ser solicitado para efectuar disposiciones de su <b>LÍNEA DE CRÉDITO</b>, es el <b>NIP</b>, este será generado por la <b>SOFOM</b> a través de los mecanismos adecuados durante el proceso de activación de la <b>TARJETA</b> que el <b>ACREDITADO</b> lleve a cabo en el Centro de Atención Telefónica (cuyos datos se indican al reverso de la <b>TARJETA FÍSICA</b> y/o en los <b>MEDIOS DE DISPOSICIÓN</b>).
                                            <br> <br>
                                            Asimismo, el <b>ACREDITADO</b> podrá registrar elementos biométricos de autenticación (en lo sucesivo los "<b>BIOMÉTRICOS</b>") a través de la <b>APLICACIÓN</b> de la <b>SOFOM</b> para identificarse.
                                            <br> <br>
                                            En relación con el <b>NIP</b> la <b>SOFOM</b> realiza las siguientes recomendaciones al <b>ACREDITADO</b>:
                                            </p>
                                            """.trimMargin()
                                    }
                                    unsafe {
                                        +"""
                                            <p>
                                            a)	No compartir su <b>NIP</b> con terceros.
                                            <br> <br>
                                            b)	No grabar el <b>NIP</b> en la <b>TARJETA FÍSICA</b> o guardarlo junto con ella.
                                            <br> <br>
                                            c)	Destruir el documento con el <b>NIP</b> una vez memorizado.
                                            <br> <br>
                                            d)	Evita dar clic o entrar a sitios desconocidos o sospechosos.
                                            <br> <br>
                                            e)	La <b>SOFOM</b> nunca te solicitará datos por ningún medio (<b>NIP</b>, contraseñas, código de seguridad o token).
                                            </p>
                                            """.trimMargin()
                                    }

                                    unsafe {
                                        +"""
                                            <p>
                                            El <b>ACREDITADO</b> reconoce y acepta que el <b>NIP</b>, el <b>BIOMÉTRICO</b>, o cualquier otro número confidencial y/o contraseña que  llegare a convenir con la <b>SOFOM</b>, equivalen a su firma electrónica y son medios que lo identifican  al realizar las disposiciones de la <b>LÍNEA DE CRÉDITO</b>, así como para la adquisición de bienes o servicios; mediante el uso de equipos automatizados, y al realizar las demás operaciones con dichos equipos que se previenen en este Contrato,  así como por los medios electrónicos previamente contratados.
                                            <br> <br> 
                                            El <b>ACREDITADO</b> expresamente reconoce y acepta que la <b>TARJETA</b> es de uso personal e intransferible y el <b>NIP</b>  o cualquier otro número confidencial y/o contraseña que llegare a convenir con la <b>SOFOM</b> son confidenciales.
                                            <br> <br>
                                            El <b>ACREDITADO</b> reconoce y acepta que serán de su exclusiva responsabilidad las disposiciones que se realicen derivado de cualquier uso indebido que terceros no autorizados llegaren a hacer de la <b>TARJETA</b>, del <b>NIP</b> o cualquier otro número confidencial y/o contraseña que llegare a convenir con la <b>SOFOM</b>.
                                            <br> <br>
                                            <b>QUINTA. DISPOSICIONES EN CAJEROS AUTOMÁTICOS Y MEDIOS ELECTRÓNICOS</b>. A efecto de realizar las operaciones a que se refiere este Contrato, la <b>SOFOM</b> y el <b>ACREDITADO</b> convienen en que las <b>TARJETAS FÍSICAS</b> podrán estar habilitadas o no para acceder a cajeros automáticos o para llevar a cabo operaciones vía internet, según lo determine la <b>SOFOM</b> al momento de la aprobación de la <b>SOLICITUD</b> del <b>ACREDITADO</b>.
                                            <br> <br>
                                            Sin perjuicio de lo anterior, el <b>ACREDITADO</b> podrá solicitar a través de la <b>APLICACIÓN</b> que la <b>TARJETA FÍSICA</b> sea habilitada o deshabilitada para realizar disposiciones de su <b>LÍNEA DE CRÉDITO</b>, previa aprobación de la <b>SOFOM</b>.
                                            <br> <br>
                                            <b>SEXTA. FORMA DE ACCEDER O DISPONER DEL CRÉDITO Y COBERTURA</b>. El <b>ACREDITADO</b> podrá disponer del crédito concedido por la SOFOM dentro de territorio nacional y/o en el extranjero, a discreción de la <b>SOFOM</b>, a través de los <b>MEDIOS DE DISPOSICIÓN</b> pactados, consintiendo cada operación, ya sea mediante la expedición de un comprobante físico o electrónico de la transacción correspondiente en favor de la <b>SOFOM</b>. El <b>ACREDITADO</b> podrá disponer de su <b>LÍMITE DE CRÉDITO</b>, a discreción de la <b>SOFOM</b> conforme a lo siguiente: i) total o parcialmente, ii) en una sola operación o en varias disposiciones parciales, iii) a través de créditos simples o cuenta corriente, y iv) en programas a meses con o sin intereses, sin que la suma del importe de las operaciones sobrepase el <b>LÍMITE DE CRÉDITO</b> concedido por la <b>SOFOM</b>.
                                            <br> <br>
                                            Por razones de identificación y de seguridad, el <b>ACREDITADO</b> al disponer de su <b>LÍNEA DE CRÉDITO</b> deberá presentar en <b>LOS ESTABLECIMIENTOS</b> o con los comisionistas, cuando sea aplicable, además de la <b>TARJETA FÍSICA</b>, una identificación oficial vigente con fotografía y firma que a simple vista sea similar a la estampada al reverso de la <b>TARJETA FÍSICA</b>, el <b>NIP</b> o algún <b>BIOMÉTRICO</b> para hacer disposiciones en los términos previstos en el presente Contrato, de lo contrario los establecimientos o comisionistas podrán negar la autorización del servicio.
                                            <br> <br>
                                            El <b>ACREDITADO</b> documentará las disposiciones de su <b>LÍNEA DE CRÉDITO</b> mediante la suscripción de pagarés o cualquier otro documento o medio autorizado por la <b>SOFOM</b> y aceptado por <b>LOS ESTABLECIMIENTOS</b> o comisionistas. Los pagarés y cualesquiera otros documentos se suscribirán y expedirán a la orden de la <b>SOFOM</b> y serán entregados por el <b>ACREDITADO</b> a <b>LOS ESTABLECIMIENTOS</b> y/o comisionistas.
                                            <br> <br>
                                            Las sumas que el <b>ACREDITADO</b> disponga con cargo a su <b>LÍNEA DE CRÉDITO</b> dentro del territorio nacional, serán documentadas invariablemente en moneda en curso legal en los Estados Unidos Mexicanos.
                                            <br> <br>
                                            <b>LOS ESTABLECIMIENTOS</b> se reservan el derecho de admitir o no el pago de mercancía, consumos, o servicios con cargo a la <b>LÍNEA DE CRÉDITO</b> a que se refiere el presente Contrato cuando el <b>ACREDITADO</b> no presente identificación oficial con fotografía y firma y/o cuando la firma estampada en el pagaré o documento que instrumente la disposición del crédito sea a simple vista distinta a la estampada al reverso de la <b>TARJETA FÍSICA</b>. La <b>SOFOM</b> no asume responsabilidad en caso de que <b>LOS ESTABLECIMIENTOS</b> se rehúsen a admitir el uso de la <b>TARJETA FÍSICA</b> y/o de cualquier otro <b>MEDIO DE DISPOSICIÓN</b> y/o en caso de que no puedan efectuarse disposiciones por desperfecto o suspensión del servicio en equipos automatizados, sistemas telefónicos y/o electrónicos, entre otros.
                                            <br> <br>
                                            La <b>SOFOM</b> es ajena a las relaciones mercantiles o civiles existentes o que surjan entre el <b>ACREDITADO</b> y <b>LOS ESTABLECIMIENTOS</b> o entre el <b>ACREDITADO</b> y aquéllos a quienes se efectúen pagos por orden de éste y con cargo a la <b>LÍNEA DE CRÉDITO</b> otorgada.
                                            <br> <br>
                                            La <b>SOFOM</b> no asumirá responsabilidad alguna por la calidad, cantidad, precio, garantías, plazo de entrega o cualesquiera otras características de los bienes o servicios que se adquieran en <b>LOS ESTABLECIMIENTOS</b> mediante el uso de los <b>MEDIOS DE DISPOSICIÓN</b>. Consecuentemente, cualquier derecho que llegare a asistir al <b>ACREDITADO</b> por los conceptos citados, deberá hacerse valer directamente en contra de los referidos establecimientos.
                                            <br> <br>
                                            La <b>SOFOM</b>, no podrá negar o condicionar el uso de los <b>MEDIOS DE DISPOSICIÓN</b> por razones de género, nacionalidad, étnicas, preferencia sexual, religiosa o cualquier otra particularidad.
                                            <br> <br>
                                            <b>SÉPTIMA. ROBO O EXTRAVÍO DEL MEDIO DE DISPOSICIÓN O DEFUNCIÓN DEL ACREDITADO</b>. En caso de robo o extravío de cualesquiera de sus <b>MEDIOS DE DISPOSICIÓN</b>, el <b>ACREDITADO</b> deberá notificarlo telefónicamente de forma inmediata a la Unidad Especializada de Atención a Usuarios de la <b>SOFOM</b> cuyo teléfono aparecerá en la <b>TARJETA FÍSICA</b> y/o en la <b>APLICACIÓN</b>, debiendo ratificar por escrito dicha situación a la <b>SOFOM</b>, presentando su escrito en el domicilio de la <b>SOFOM</b> dentro de las 24 (veinticuatro) horas siguientes a dicha notificación.
                                            </p>
                                            """.trimMargin()
                                    }
                                    unsafe {
                                        +"""
                                            <p>
                                            Para todas las disposiciones ocurridas previo al reporte telefónico en el que se le haya otorgado la clave o folio de reporte, el <b>ACREDITADO</b> será responsable, sin restricción ni condición alguna, de las disposiciones realizadas con cargo a su <b>LÍNEA DE CRÉDITO</b> mediante el uso del <b>MEDIO DE DISPOSICIÓN</b> siendo igualmente responsable de los cargos e intereses ordinarios y moratorios que por tales disposiciones se generen en los términos previstos en este Contrato.
                                            <br> <br>
                                            En caso de defunción del <b>ACREDITADO</b>, el presente Contrato dejará de surtir efectos salvo por aquellos saldos insolutos que deberán de ser cubiertos por la sucesión del mismo. En caso de defunción, deberá de notificarse a la <b>SOFOM</b> en los mismos términos indicados en el párrafo anterior, en el entendido de que dicha notificación deberá ser realizada por el albacea de la sucesión, o cualquier persona con interés jurídico, o el titular de la <b>TARJETA</b> adicional, en caso de haberlo. En todo caso, la sucesión del <b>ACREDITADO</b> y solidariamente el titular de la <b>TARJETA</b> adicional, en caso de haberlo, serán responsables únicamente por los cargos efectuados con el <b>MEDIO DE DISPOSICIÓN</b> y/o <b>TARJETA</b> adicional con posterioridad a la notificación de la defunción del <b>ACREDITADO</b>.
                                            <br> <br>
                                            <b>OCTAVA. ESTADO DE CUENTA</b>. La <b>SOFOM</b> enviará al <b>ACREDITADO</b> un estado de cuenta mensual, en el que se informarán los movimientos realizados y generados en cada periodo de su cuenta, así como la indicación del plazo para su pago. Lo anterior sólo en caso de que se registre un saldo o movimiento en la cuenta del <b>ACREDITADO</b>. Dicho estado de cuenta será enviado dentro de los 10 (diez) días siguientes al corte de la cuenta. El estado de cuenta será enviado por medios electrónicos y/o por correo al domicilio que el <b>ACREDITADO</b> señaló en la <b>SOLICITUD</b>, por lo que cualquier cambio de domicilio deberá ser notificado por el <b>ACREDITADO</b> a la <b>SOFOM</b> al teléfono señalado en el estado de cuenta o por cualquier otro medio que la <b>SOFOM</b> le indique, a más tardar con 15 (quince) días naturales de anticipación a la fecha de corte de su crédito; de lo contrario, se considerará válidamente enviado el estado de cuenta al <b>ACREDITADO</b>. No obstante lo anterior, la <b>SOFOM</b> podrá enviar al <b>ACREDITADO</b> el estado de cuenta a través de medios electrónicos, siempre y cuando el <b>ACREDITADO</b> así lo autorice, en este supuesto, el <b>ACREDITADO</b> será responsable de consultar el estado de cuenta por los medios pactados.
                                            <br> <br>
                                            El <b>ACREDITADO</b> dispondrá de un plazo de 90 (noventa) días naturales contados a partir de su fecha de corte, que será el día que al efecto se señale en el estado de cuenta de manera mensual, para objetar por escrito dicho estado de cuenta. Transcurrido el plazo mencionado sin haber presentado objeción al estado de cuenta, se entenderá que el <b>ACREDITADO</b> acepta los términos del mismo.
                                            <br> <br>
                                            El presente Contrato acompañado de la certificación del contador de la <b>SOFOM</b> respecto del estado de cuenta, constituirán título ejecutivo mercantil, sin necesidad de reconocimiento de firma ni de otro requisito alguno.
                                            <br> <br>
                                            En todo momento el <b>ACREDITADO</b> podrá solicitar telefónicamente al Centro de Atención Telefónica, su estado de cuenta y saldo pendiente de pago o, en su defecto, la información contenida en su estado de cuenta.
                                            <br> <br>
                                            Tomando en consideración la facultad que le concede la <b>SOFOM</b> al <b>ACREDITADO</b>, para exigir en cualquier tiempo la información sobre el importe del saldo, el <b>ACREDITADO</b> se obliga a pagar a más tardar a los 10 (diez) días hábiles siguientes a la fecha de corte, el saldo de su <b>LÍNEA DE CRÉDITO</b>, aún y cuando por cualquier razón no haya recibido su estado de cuenta.]
                                            <br> <br>
                                            <b>NOVENA. PAGOS</b>. La fecha límite de pago será la que determine el estado de cuenta que la <b>SOFOM</b> entregue al <b>ACREDITADO</b>. En caso que la fecha límite de pago sea un día inhábil bancario, el pago respectivo podrá realizarse el siguiente día hábil bancario, en cuyo caso el <b>ACREDITADO</b> no pagará intereses moratorios a la <b>SOFOM</b>.
                                            <br> <br>
                                            Los pagos deberán efectuarse en la red de comisionistas, en equipos automatizados, sistemas telefónicos y/o electrónicos en moneda nacional, salvo que la <b>SOFOM</b> notifique al <b>ACREDITADO</b> cualquier otro medio de pago.
                                            <br> <br>
                                            El <b>ACREDITADO</b> pagará como Pago Mínimo, la cantidad que resulte más alta de los puntos siguientes:
                                            <br> <br>
                                            La suma de 1.5% (uno punto cinco por ciento) del saldo insoluto de la parte revolvente del importe del periodo correspondiente (“<b>CICLO</b>”), sin contar los intereses del periodo ni el <b>IVA</b>, más los referidos intereses y el <b>IVA</b>.
                                            <br> <br>
                                            El 1.25% (uno punto veinticinco por ciento) del <b>LÍMITE DE CRÉDITO</b>.
                                            </p>
                                            """.trimMargin()
                                    }

                                    unsafe {
                                        +"""
                                            <p>
                                            La <b>SOFOM</b> podrá determinar libremente el importe del Pago Mínimo, siempre y cuando dicho importe sea mayor al de los incisos a) y b), en este caso, el <b>ACREDITADO</b> pagará como Pago Mínimo a la <b>SOFOM</b> la cantidad correspondiente señalada en el estado de cuenta, incluyendo intereses ordinarios, intereses moratorios, impuestos y comisiones. El Pago Mínimo será recalculado en cada fecha de corte. No obstante, en caso de que el resultado de dicha operación sea menor a $200.00 (doscientos pesos 00/100 M.N.), el <b>ACREDITADO</b> deberá pagar como Pago Mínimo la cantidad de $200.00 (doscientos pesos 00/100 M.N.). Para efectos del cálculo anterior, se entenderá como saldo al corte la cantidad que resulte de sumar: (i) el importe de los cargos realizados en el Período Mensual de que se trate, más; (ii) el saldo deudor de periodos anteriores, para el caso de que aplique; más (iii) los intereses ordinarios que en su caso se hayan devengado, más; (iv) las comisiones que apliquen y los intereses moratorios que procedan de acuerdo a lo establecido en el presente Contrato. En caso de que el saldo al corte sea menor a $200.00 (doscientos pesos 00/100 M.N.), no se calculará el Pago Mínimo y se deberá liquidar el saldo total. 
                                            </p>
                                            """.trimMargin()
                                    }

                                    unsafe {
                                        +"""
                                            <p>
                                            En caso de promociones ofrecidas por la <b>SOFOM</b>, aplicarán: i) el tipo de crédito (simple o cuenta corriente), ii) el programa (meses con o sin intereses), iii) la tasa de interés (ordinaria o moratoria), y/o iv) el cálculo indicado en cada promoción.
                                            </p>
                                            """.trimMargin()
                                    }

                                    unsafe {
                                        +"""
                                            <p>
                                            Para los efectos de la presente cláusula se entenderá por Periodo Mensual los días que transcurran entre el día siguiente a la fecha de corte del mes inmediato anterior y la fecha de corte del mes inmediato siguiente.
                                            </p>
                                            """.trimMargin()
                                    }

                                    unsafe {
                                        +"""
                                            <p>
                                            Si el importe del saldo insoluto de la cuenta excede al <b>LÍMITE DE CRÉDITO</b>, el excedente deberá́ cubrirse de inmediato por el <b>ACREDITADO</b>.
                                            </p>
                                            """.trimMargin()
                                    }

                                    unsafe {
                                        +"""
                                            <p>
                                            <b>DÉCIMA. PAGOS ANTICIPADOS Y PAGOS ADELANTADOS</b>. El <b>ACREDITADO</b> podrá realizar pagos adelantados a la fecha de vencimiento del pago sin penalización o comisión alguna, debiendo realizarse por un importe igual o mayor a la amortización que corresponda, siempre y cuando el <b>ACREDITADO</b> se encuentre al corriente en el pago del crédito, intereses y sus accesorios, debiendo dar aviso a la <b>SOFOM</b> previo a la fecha en que se realice el pago a través de cualquiera de los medios que le sean especificados de tiempo en tiempo por esta en términos de lo dispuesto por la cláusula Vigésima Cuarta. 
                                            </p>
                                            """.trimMargin()
                                    }

                                    unsafe {
                                        +"""
                                            <p>
                                            Los pagos adelantados que realice el <b>ACREDITADO</b> que no alcancen a cubrir el saldo total, se aplicarán a las amortizaciones periódicas subsecuentes, reduciendo el monto a pagar de la amortización que corresponda o en su caso se reducirá el número de semanas, quincenas o meses de pago del crédito, previo acuerdo con el <b>ACREDITADO</b>.
                                            </p>
                                            """.trimMargin()
                                    }

                                    unsafe {
                                        +"""
                                            <p>
                                            Los pagos adelantados parciales realizados por el <b>ACREDITADO</b> se verán reflejados en el estado de cuenta del período en que se efectuó el pago adelantado parcial correspondiente y serán aplicados de acuerdo a la prelación establecida.
                                            </p>
                                            """.trimMargin()
                                    }

                                    unsafe {
                                        +"""
                                            <p>
                                            El pago total del crédito podrá ser efectuado por el <b>ACREDITADO</b> mediante el pago anticipado correspondiente. Una vez efectuado el pago total del crédito a entera satisfacción de la <b>SOFOM</b>, ésta a solicitud del <b>ACREDITADO</b> podrá poner a su disposición un documento que ampare el finiquito total de la operación.
                                            </p>
                                            """.trimMargin()
                                    }

                                    unsafe {
                                        +"""
                                            <p>
                                            <b>DÉCIMA PRIMERA. APLICACIÓN DE LOS PAGOS</b>. Los pagos deberán realizarse en moneda nacional, en efectivo o por cualquier otro medio que la <b>SOFOM</b> le informe al <b>ACREDITADO</b>, a través de cualquiera de los medios autorizados por la <b>SOFOM</b> en términos de este Contrato y que de tiempo en tiempo se hagan del conocimiento del <b>ACREDITADO</b> en el estado de cuenta.
                                            </p>
                                            """.trimMargin()
                                    }

                                    unsafe {
                                        +"""
                                            <p>
                                            El <b>ACREDITADO</b> acuerda con la <b>SOFOM</b> aplicar las cantidades que aquel pague, en el siguiente orden: (i) saldo vencido (ii) saldo vigente. Se entiende por saldo vencido, en este orden, (i) los impuestos, (ii) comisiones, (iii) intereses y capital derivados de las disposiciones del crédito y no cubiertos en tiempo; y por saldo vigente, en este orden: (i) los impuestos, (ii) comisiones), intereses y capital derivados de las disposiciones del crédito que se encuentren vigentes.
                                            </p>
                                            """.trimMargin()
                                    }

                                    unsafe {
                                        +"""
                                            <p>
                                            La aplicación de pagos mencionada en el párrafo anterior será modificada cuando el <b>ACREDITADO</b> estando al corriente efectúe pagos superiores al saldo deudor, en el entendido que el excedente podrá (i) ser aplicado al saldo vigente en el periodo o (ii) ser dispuesto por el <b>ACREDITADO</b> a través de las formas de disposición establecidas en la cláusula Sexta del presente Contrato, en el entendido que la <b>LÍNEA DE CRÉDITO</b> otorgada no constituye un medio de ahorro, ni un medio para realizar transacciones a otras cuentas, es decir, es solo un medio de pago, por lo que, si después de compensados todos los saldos, existiera un saldo a favor, este no generará intereses en favor del <b>ACREDITADO</b> incluso quedando reservada para la SOFOM la facultad de dar por terminado y/o cancelar la <b>LÍNEA DE CRÉDITO</b> correspondiente cuando el <b>ACREDITADO</b> incurra reiteradamente en este tipo de prácticas.
                                            </p>
                                            """.trimMargin()
                                    }

                                    unsafe {
                                        +"""
                                            <p>
                                            Debido a que la <b>LÍNEA DE CRÉDITO</b> materia del presente Contrato, no constituye un medio de ahorro, los saldos de los pagos realizados en exceso por el <b>ACREDITADO</b> no generarán rendimientos a su favor.
                                            </p>
                                            """.trimMargin()
                                    }

                                    unsafe {
                                        +"""
                                            <p>
                                            En caso de promociones, el pago se aplicará al capital con la siguiente prelación: en primer lugar se liquidan las promociones sin intereses con mayor número de parcialidades vencidas y en último lugar las promociones con intereses con mayor número de parcialidades vencidas.
                                            </p>
                                            """.trimMargin()
                                    }

                                    unsafe {
                                        +"""
                                            <p>
                                            Adicionalmente, en caso de tener saldo pendiente de pago, el <b>ACREDITADO</b> deberá cubrir el total del Pago Mínimo requerido en su estado de cuenta para permanecer en las diferentes promociones vigentes. Los pagos se aplicarán primero a las promociones especiales y por último al plan de pagos indicado en la carátula de crédito. En caso de incumplimiento del Pago Mínimo, el <b>ACREDITADO</b> perderá los beneficios de las promociones aplicando al total del saldo los intereses ordinarios indicados en la carátula.
                                            <br> <br>
                                            <b>DÉCIMA SEGUNDA. INTERESES</b>. El interés ordinario referido en el presente Contrato será determinado anualmente aplicando la tasa de interés ordinaria fija referida en el <b>ANEXO</b> B del presente Contrato al saldo promedio diario no pagado que tenga el <b>ACREDITADO</b>; dividiendo el resultado obtenido entre 12 (doce) meses, la tasa resultante se multiplicará por el saldo del capital, el que por concepto de intereses ordinarios deberá pagar el <b>ACREDITADO</b> en la fecha que corresponda. Los intereses podrán capitalizarse. Su pago se exigirá por periodos vencidos.
                                            <br> <br>
                                            En caso de incumplimiento de pago o pago tardío conforme a los términos del presente Contrato, se cobrará un interés moratorio al <b>ACREDITADO</b> determinado anualmente aplicando la tasa de interés moratoria fija referida en el <b>ANEXO</b> B del presente Contrato, calculada diariamente sobre el capital vencido y no pagado en los términos indicados en el referido <b>ANEXO B</b> a partir del primer día después de la fecha de incumplimiento y hasta el día en que se efectúe el pago total del adeudo. En el entendido que el cobro de intereses moratorios elimina o sustituye el cobro de comisión por pago tardío. 
                                            <br> <br>
                                            <b>DÉCIMA TERCERA. CARGOS Y COMISIONES</b>. El concepto, monto y periodicidad de pago de los cargos, intereses y comisiones aplicables al presente Contrato se indican en el <b>ANEXO B</b> del mismo, los cuales también podrán ser consultados de tiempo en tiempo en <a href="https://karum.com/operadora-activacel" target="_blank">https://karum.com/operadora-activacel</a>. El <b>ACREDITADO</b> se obliga a pagar dichos cargos, intereses y comisiones más su respectivo IVA, sin necesidad de previo requerimiento por los medios que la <b>SOFOM</b> ponga a disposición del <b>ACREDITADO</b>. 
                                            <br> <br>
                                            La <b>SOFOM</b> no podrá cobrar comisiones por conceptos distintos a los señalados en el <b>ANEXO B</b>. Durante la vigencia del presente Contrato la <b>SOFOM</b> podrá modificar y/o actualizar los cargos, intereses y comisiones debiendo notificar previamente de manera expresa, mediante el estado de cuenta, correo electrónico, telefónicamente o bien, por cualquier medio electrónico al <b>ACREDITADO</b> los nuevos términos establecidos, con una anticipación de cuando menos 30 (treinta) días naturales a la fecha en que entren en vigor, señalando específicamente esta última. El <b>ACREDITADO</b> acepta expresamente que se entenderá que otorga su consentimiento a las nuevas cuotas que la <b>SOFOM</b> le notifique, si este hiciere uso de la <b>LÍNEA DE CRÉDITO</b> posterior a la notificación, o bien si no manifestare expresamente su objeción a las mismas dentro de los 30 (treinta) días naturales siguientes a la fecha en que entren en vigor.
                                            <br> <br>
                                            <b>DÉCIMA CUARTA. PROGRAMAS (CRÉDITO SIMPLE O CUENTA CORRIENTE) Y PROMOCIONES (CON INTERESES O PAGO DIFERIDO)</b>. Es la opción que tiene el <b>ACREDITADO</b> para solicitar a la <b>SOFOM</b>, a través del Centro de Atención a Clientes o de los medios electrónicos que éste último ponga a su disposición, que respecto de una parte o el total de su saldo deudor se apliquen condiciones de pago distintas en cuanto a tipo de crédito, plazo y tasa de interés. El tipo de crédito, plazo y tasa de interés aplicables serán informados al <b>ACREDITADO</b> por la <b>SOFOM</b> al momento de la solicitud que aquel realice por los medios pactados. Esta solicitud estará sujeta a la aprobación de la <b>SOFOM</b>.
                                            <br> <br>
                                            La solicitud con el tipo de crédito, plazo y tasa de interés aplicables, así como la vigencia de la misma, se establecerá en el documento denominado Detalles del Crédito (ANEXO D). La <b>SOFOM</b> podrá dar por terminada la vigencia de la promoción en cualquier momento dando aviso al <b>ACREDITADO</b> a través de los medios de comunicación señalados en el presente Contrato.
                                            <br> <br>
                                            <b>DÉCIMA QUINTA. TASA DE INTERÉS PROMOCIONAL</b>. Es la tasa de interés que la <b>SOFOM</b> podrá poner a disposición del <b>ACREDITADO</b> como resultado de campañas promocionales que le serán informadas al <b>ACREDITADO</b> de manera escrita, mediante el estado de cuenta, correo electrónico, telefónicamente o bien, por cualquier otro medio electrónico.
                                            <br> <br>
                                            <b>DÉCIMA SEXTA. CARGOS A LA CUENTA Y AUTORIZACIÓN DE CARGOS RECURRENTES</b>. La <b>SOFOM</b> cargará a la cuenta los conceptos que se mencionan a continuación, los cuales, el <b>ACREDITADO</b> se obliga a pagar a la <b>SOFOM</b>, a través de la red de comisionistas, en equipos automatizados, sistemas telefónicos y/o electrónicos en moneda nacional y/o en efectivo, salvo que la <b>SOFOM</b> notifique al <b>ACREDITADO</b> cualquier otro medio de pago, sin necesidad de requerimiento previo:
                                            </p>
                                            """.trimMargin()
                                    }

                                    unsafe {
                                        +"""
                                            <p>
                                            a)	El importe de los pagos de bienes, servicios, impuestos y demás conceptos que realice la <b>SOFOM</b> por cuenta del <b>ACREDITADO</b> o de las personas a quien éste último otorgue <b>TARJETAS</b> adicionales, cuando el <b>ACREDITADO</b> y éstos últimos: (i) hayan suscrito pagarés u otros documentos que sean aceptados por la <b>SOFOM</b> y se hayan entregado al establecimiento respectivo, (ii) los hayan autorizado a través de cualquier medio, o (iii) hayan solicitado por vía telefónica o electrónica a <b>LOS ESTABLECIMIENTOS</b> la compra de bienes o servicios. 
                                            <br> <br>
                                            b)	El importe de los pagos de bienes, servicios, impuestos y demás conceptos que realice la <b>SOFOM</b> por cuenta del <b>ACREDITADO</b>, por concepto de cargos recurrentes que hayan sido contratados y autorizados por el <b>ACREDITADO</b> directamente con <b>LOS ESTABLECIMIENTOS</b>, a través de cualquier medio, incluyendo vía telefónica y/o electrónica. En todo caso el <b>ACREDITADO</b> deberá de cancelar las autorizaciones que hubiere efectuado para la contratación de cargos recurrentes de forma directa con los propios establecimientos y/o comercios con quienes los haya contratado, salvo que la <b>SOFOM</b> le informe de otro medio para realizarlo.
                                            <br> <br>
                                            c)	Las disposiciones en efectivo de su <b>LÍNEA DE CRÉDITO</b> hechas en establecimientos afiliados o a través de otros medios automatizados o electrónicos que al efecto tenga establecidos o se pacten con la <b>SOFOM</b>, en el territorio nacional; dentro de las disposiciones de efectivo se comprenderán aquellas cantidades cargadas como comisiones a fin de obtener la cantidad dispuesta por parte del <b>ACREDITADO</b>. 
                                            <br> <br>
                                            d)	Los intereses pactados.
                                            <br> <br>
                                            e)	Las comisiones que al efecto la <b>SOFOM</b> tenga establecidas en los términos de la cláusula denominada <b>CARGOS Y COMISIONES</b> de este Contrato. 
                                            <br> <br>
                                            f)	El <b>IVA</b> o cualquier otro impuesto que establezcan las leyes respectivas. 
                                            <br> <br>
                                            g)	Cualquier otro importe que se genere a cargo del <b>ACREDITADO</b>, en virtud de este Contrato.
                                            </p>
                                            """.trimMargin()
                                    }

                                    unsafe {
                                        +"""
                                            <p>
                                            <b>DÉCIMA SÉPTIMA. ANEXOS</b>. Los siguientes anexos forman parte del Contrato, por lo cual se deberán considerar como parte integrante del mismo:
                                            </p>
                                            """.trimMargin()
                                    }

                                    unsafe {
                                        +"""
                                            <p>
                                            <b>Anexo A.</b> Solicitud de crédito. <br>
                                            <b>Anexo B.</b> Carátula de crédito. <br>
                                            <b>Anexo C.</b> Referencias legales. <br>
                                            <b>Anexo D.</b> Detalles del crédito.
                                            </p>
                                            """.trimMargin()
                                    }

                                    unsafe {
                                        +"""
                                            <p>
                                            <b>DÉCIMA OCTAVA. INCUMPLIMIENTO POR MORA</b>. En caso de que el <b>ACREDITADO</b> no cumpla con el Pago Mínimo que haya acordado con la <b>SOFOM</b> dentro del periodo pactado en el presente, estará obligado a pagar adicionalmente una comisión por pago tardío, determinada conforme a lo referido en el <b>ANEXO B</b> del Contrato. No se cobrará dicha comisión cuando se cobren intereses moratorios durante el mismo periodo.
                                            <br> <br>
                                            En caso de incumplimiento de tres o más pagos mínimos consecutivos, la <b>SOFOM</b> podrá dar por vencido anticipadamente el crédito, exigir el pago correspondiente del saldo total a la fecha de terminación.
                                            <br> <br>
                                            La omisión por parte de la <b>SOFOM</b> en el ejercicio de los derechos que deriven a su favor del presente Contrato, en ningún caso deberá entenderse o tendrán el efecto de considerarse como una renuncia a los mismos, así como el ejercicio parcial de un derecho derivado de este Contrato por parte de la <b>SOFOM</b> no excluye la posibilidad de ejercer algún otro derecho o facultad.
                                            <br> <br>
                                            <b>DÉCIMA NOVENA. CARACTERÍSTICAS DEL CRÉDITO</b>. Las características de la operación que constan en este Contrato se precisan en el <b>ANEXO B</b> (carátula de crédito), el cual forma parte integrante del presente instrumento.
                                            <br> <br>
                                            <b>VIGÉSIMA. COSTO ANUAL TOTAL</b>. El Costo Anual Total (“<b>CAT</b>”) se reflejará en el estado de cuenta a que se refiere la cláusula Octava. El <b>CAT</b> está expresado en términos porcentuales anuales que, para fines informativos y de comparación, incorpora la totalidad de los costos y gastos inherentes a los créditos. El <b>CAT</b> ha sido precisado con fines de referencia en el <b>ANEXO B</b> del presente Contrato.
                                            <br> <br>
                                            <b>VIGÉSIMA PRIMERA. CESIÓN DE CRÉDITO</b>. El <b>ACREDITADO</b> faculta expresamente a la <b>SOFOM</b> para ceder o descontar el crédito del presente Contrato, así como los derechos y obligaciones derivados del mismo, sin necesidad de autorización del <b>ACREDITADO</b>. 
                                            <br> <br>
                                            El <b>ACREDITADO</b> no podrá ceder los derechos y obligaciones derivadas del presente instrumento.
                                            <br> <br>
                                            <b>VIGÉSIMA SEGUNDA. VIGENCIA, TERMINACIÓN ANTICIPADA O RESCISIÓN DEL CONTRATO</b>. La duración de este Contrato será indefinida. Cualquiera de las Partes podrá dar por terminado este acuerdo, en cualquier momento, notificando previamente en los términos establecidos en esta cláusula.
                                            <br> <br>
                                            El <b>ACREDITADO</b> podrá solicitar en todo momento la terminación del presente Contrato, bastando para ello la presentación de una solicitud por escrito en el domicilio de la <b>SOFOM</b>, o mediante una llamada telefónica al Centro de Atención Telefónica, siempre y cuando el <b>ACREDITADO</b> haya liquidado previamente la totalidad de los adeudos, tanto accesorios como principales, otorgando la <b>SOFOM</b> al <b>ACREDITADO</b> un acuse o código de seguimiento sobre el trámite de terminación.
                                            <br> <br>
                                            Para tales efectos, la <b>SOFOM</b> deberá comunicar al <b>ACREDITADO</b> el importe de los adeudos, a más tardar el día hábil siguiente a la fecha de solicitud de terminación del Contrato; dicha información estará a disposición del <b>ACREDITADO</b> en el domicilio de la <b>SOFOM</b> dentro de los 5 (cinco) días siguientes a la solicitud de terminación. 
                                            <br> <br>
                                            Una vez ocurrido lo anterior, previa entrega y revisión del estado de cuenta a la fecha de la solicitud de terminación del <b>ACREDITADO</b>, la <b>SOFOM</b> se sujetará a lo siguiente:
                                            <br> <br>
                                            El presente Contrato se dará por terminado el día hábil siguiente al de la acreditación del pago íntegro de los adeudos generados por el crédito otorgado y la presentación de la solicitud de terminación por parte del <b>ACREDITADO</b>. Una vez autorizada la solicitud de terminación del <b>ACREDITADO</b>, la <b>SOFOM</b> inhabilitará la <b>LÍNEA DE CRÉDITO</b> y no se podrá realizar más disposiciones por ningún medio. 
                                            <br> <br>
                                            En la fecha que se dé por terminado el Contrato, la <b>SOFOM</b> deberá entregar al <b>ACREDITADO</b> cualquier saldo que éste tenga a su favor, deduciendo en su caso, las comisiones y cualquier otra cantidad que, en términos del presente Contrato, puedan resultar a cargo del <b>ACREDITADO</b>, mediante la habilitación de una última operación en su <b>TARJETA FISICA</b>, consistente en el retiro de la cantidad de dinero en efectivo que corresponda reintegrarle. Esta última operación no será considerada como una disposición de su <b>LÍNEA DE CRÉDITO</b> sino una devolución de un saldo a favor. 
                                            <br> <br>
                                            Realizado el pago y liquidado todo adeudo que tenga el <b>ACREDITADO</b>, la <b>SOFOM</b> deberá poner a su disposición un documento, o bien un estado de cuenta, que dé constancia del fin de la relación contractual y de la inexistencia de adeudos entre las Partes. La <b>SOFOM</b> una vez que se hubiere agotado el procedimiento previsto en este artículo, no podrá efectuar al <b>ACREDITADO</b> requerimiento de pago alguno.
                                            <br> <br>
                                            Si el <b>ACREDITADO</b> incumple con cualquier obligación a su cargo prevista en el presente instrumento, el saldo total del crédito será exigible inmediatamente, incluyendo comisiones e intereses ordinarios y moratorios. Es causa de rescisión del presente Contrato el incumplimiento de cualquier obligación pactada en el mismo.
                                            <br> <br>
                                            El mismo procedimiento descrito en esta cláusula se seguirá si la <b>SOFOM</b> denuncia el presente Contrato para el monto del crédito que el <b>ACREDITADO</b> no hubiere utilizado en dicho momento.
                                            <br> <br>
                                            La <b>SOFOM</b> tendrá derecho a dar por terminado el Contrato en cualquier momento sin responsabilidad adicional que la de dar aviso al <b>ACREDITADO</b> mediante comunicación escrita, el estado de cuenta, correo electrónico, telefónicamente o bien, por cualquier otro medio electrónico. La terminación referida en este párrafo surtirá efectos al día siguiente de su notificación; no obstante, continuarán surtiendo efectos los términos aplicables al saldo pendiente de pago por parte del <b>ACREDITADO</b> al momento de la terminación, hasta que se liquide la totalidad del saldo adeudado. 
                                            <br> <br>
                                            A la terminación del Contrato, el <b>ACREDITADO</b> deberá devolver inmediatamente a la <b>SOFOM</b> las <b>TARJETAS FÍSICAS</b> que le hubieren sido entregadas con motivo de este Contrato, esto sin perjuicio del derecho de cobro que tiene la <b>SOFOM</b> respecto del <b>ACREDITADO</b>, en los tiempos y plazos acordados en este instrumento.
                                            </p>
                                            """.trimMargin()
                                    }

                                    unsafe {
                                        +"""
                                            <p>
                                            Consecuentemente, se extinguirá el crédito en la parte que el <b>ACREDITADO</b> no hubiere hecho efectiva.
                                            <br> <br>
                                            <b>VIGÉSIMA TERCERA. MODIFICACIONES AL CONTRATO</b>. La <b>SOFOM</b> podrá modificar el presente Contrato parcial o totalmente, informándole por escrito al <b>ACREDITADO</b> las modificaciones, con 30 (treinta) días naturales de anticipación a su entrada en vigor, por medios electrónicos y/o por cualquier otro medio que establezcan las Partes. En caso de que se modifiquen las condiciones generales y/o tasas de interés del presente Contrato, las nuevas condiciones serán dadas a conocer al <b>ACREDITADO</b> en comunicación escrita o por medios electrónicos y por cualquier otro medio que establezcan las Partes, bastando la anotación que al respecto se haga en el estado de cuenta referido en la cláusula Octava, salvo en el caso de reestructura del crédito, para ello, la <b>SOFOM</b> notificará y explicará al <b>ACREDITADO</b> de forma fehaciente la manera en que operará la reestructura y a su vez obtendrá su consentimiento expreso.
                                            <br> <br>
                                            El <b>ACREDITADO</b> acepta expresamente que se entenderá que otorga su consentimiento a las modificaciones al Contrato que la <b>SOFOM</b> le notifique, si este hiciere uso de la <b>LÍNEA DE CRÉDITO</b> posterior a la notificación, o bien si no manifestare expresamente su objeción a las mismas dentro de los 30 (treinta) días naturales siguientes a la fecha en que entren en vigor. En caso de que el <b>ACREDITADO</b> no esté de acuerdo con las modificaciones propuestas, podrá solicitar la terminación del presente Contrato hasta 60 (sesenta) días naturales después de la entrada en vigor de dichas modificaciones, sin responsabilidad alguna a su cargo, debiendo cubrir, en su caso, los adeudos que ya se hubieren generado. En este caso la <b>SOFOM</b> no podrá cobrar cantidad adicional alguna por la terminación de la prestación de los servicios, con excepción de los adeudos que ya se hubieren generado a la fecha en que el <b>ACREDITADO</b> solicite dar por terminado el servicio.
                                            <br> <br>
                                            Se tendrá como el contrato de adhesión válido entre las Partes, el vigente registrado ante la <b>CONDUSEF</b> en el Registro de Contratos de Adhesión del producto respectivo. Lo anterior salvo que dicho contrato de adhesión sea dado de baja en el referido registro por dejar de comercializarse el producto, en cuyo caso, se considerarán los avisos de las modificaciones que el <b>ACREDITADO</b> haya aceptado para determinar la versión vigente del mismo.
                                            <br> <br>
                                            El <b>ACREDITADO</b> reconoce y está de acuerdo en que la <b>SOFOM</b> podrá libremente y sin limitación alguna, realizar todas las modificaciones técnicas, físicas, mecánicas o de cualquier otra naturaleza, que sean necesarias para mejorar, actualizar o suprimir algunas de las funciones de los medios electrónicos incluyendo la <b>APLICACIÓN</b>, así mismo, podrá llevar a cabo la eliminación de algunas operaciones electrónicas o la eliminación total de algunos de los medios electrónicos, según la <b>SOFOM</b> lo considere necesario.
                                            <br> <br>
                                            <b>VIGÉSIMA CUARTA. DOMICILIOS Y NOTIFICACIONES</b>. Para todos los efectos judiciales y extrajudiciales, el <b>ACREDITADO</b> señala como medio para recibir y oír todo tipo de notificaciones la <b>APLICACIÓN</b> y/o su domicilio indicado en la <b>SOLICITUD</b> que como <b>ANEXO</b> A se agrega a este Contrato. Por lo tanto, las personas que se encuentren en dicho domicilio se entienden como expresamente autorizadas por el <b>ACREDITADO</b> para recibir todo tipo de avisos y previa identificación podrán recibir los <b>MEDIO DE DISPOSICIÓN</b> del <b>ACREDITADO</b> objeto del presente Contrato.
                                            <br> <br>
                                            La <b>SOFOM</b> señala como domicilio convencional el ubicado en Blvd. Manuel Ávila Camacho No. 5, Interior S 1000, Edificio Torre B, Piso 10, Oficina 1045, Col. Lomas de Sotelo, Naucalpan de Juárez, Estado de México. CP 53390.
                                            <br> <br>
                                            Las Partes expresan su conformidad para que las notificaciones sobre cualquier asunto relacionado con el presente Contrato, en tanto las cláusulas específicas no señalen una tramitación especial, sean mediante simple comunicación escrita, la cual podrá ser entregada por los medios electrónicos acordados con el <b>ACREDITADO</b>.  
                                            <br> <br>
                                            <b>VIGÉSIMA QUINTA. VERIFICACIÓN DE DATOS</b>. El <b>ACREDITADO</b> autoriza expresamente a la <b>SOFOM</b> para que por sí misma o a través de prestadores de servicios y sociedades especializadas, corrobore los datos aquí asentados e investigue su historial crediticio, incluso autoriza a que le llamen telefónicamente para dichos efectos y especialmente autoriza a que la <b>SOFOM</b> pueda proporcionar a cualesquiera sociedades de información crediticia su comportamiento crediticio. Lo anterior lo autoriza mediante la firma del documento que se agrega al presente instrumento formando parte integrante del mismo como <b>ANEXO A</b>. 
                                            <br> <br>
                                            De tiempo en tiempo la <b>SOFOM</b> podrá monitorear llamadas telefónicas realizadas entre el <b>ACREDITADO</b> y/o titulares de <b>TARJETAS</b> adicionales y la <b>SOFOM</b> con la finalidad de asegurar la calidad de servicio al cliente. El <b>ACREDITADO</b> será avisado por parte de la <b>SOFOM</b> del monitoreo de la llamada telefónica. 
                                            <br> <br>
                                            <b>VIGÉSIMA SEXTA. PREVENCIÓN DE FRAUDES</b>. La <b>SOFOM</b> no solicitará al <b>ACREDITADO</b> por ningún medio y bajo ninguna circunstancia pagos o depósitos a cuentas de terceros que no se encuentren bajo la titularidad de la <b>SOFOM</b> para otorgar créditos, así como tampoco solicitará el número de identificación personal <b>NIP</b> para ninguna consulta, modificación o aclaración.
                                            <br> <br>
                                            <b>VIGÉSIMA SÉPTIMA. INFORMACIÓN</b>. El <b>ACREDITADO</b> autoriza a la <b>SOFOM</b> a:
                                            </p>
                                            """.trimMargin()
                                    }

                                    unsafe {
                                        +"""
                                            <p>
                                            a)	Proporcionar información que se estime pertinente a quien preste los servicios operativos y de maquila de la <b>TARJETA</b> o <b>MEDIO DE DISPOSICIÓN</b> que corresponda.
                                            <br> <br>
                                            b)	Utilizar la información que le haya proporcionado en la <b>SOLICITUD</b> y de la operación del crédito para actividades promocionales o para ofrecer operaciones y servicios de cualquier empresa subsidiaria, filial o controlada directa o indirectamente por la <b>SOFOM</b>.
                                            <br> <br>
                                            c)	Verificar su Credencial para Votar ante el Instituto Nacional Electoral (en lo sucesivo “INE”); en ese contexto, el <b>ACREDITADO</b> reconoce y acepta que en el caso de haber otorgado su consentimiento expreso por medios electrónicos, a través de los mecanismos que la <b>SOFOM</b> tenga habilitados y/o llegue a habilitar para tal efecto en la <b>APLICACIÓN</b> y/o a través de su portal, la <b>SOFOM</b>, podrá hacer uso de sus datos personales como la entidad federativa, municipio y localidad que corresponden a su domicilio, sección electoral, apellido paterno, apellido materno y nombre completo, y datos personales sensibles como su firma, huella digital y fotografía contenidos en su credencial para votar, misma que exhibe de manera digital como medio de autenticación de su identidad, esto a efecto de corroborar que dichos datos coinciden con los que obran en poder del INE, por medio del servicio de verificación de datos de la Credencial para Votar del INE, cuyos principales objetivos son:
                                            <br>
                                            <p style="margin-left:20px;">(i)	Verificar la vigencia y coincidencia de los datos del INE que presenten los ciudadanos para  identificarse ante las instituciones públicas y privadas, así como las asociaciones civiles, respecto de la información almacenada en la base de datos del Padrón Electoral.</p>
                                            <p style="margin-left:20px;">(ii)	Autenticar y/o verificar las huellas dactilares, así como información del ciudadano derivada de sus propias características físicas, que sirvan para su identificación mediante la  correlación gráfica de aquellas capturadas a través de la <b>APLICACIÓN</b>, entre sí, y/o con aquellas que se encuentran almacenadas en la base de datos del Padrón Electoral.</p> 
                                            <span style="color:#182035;">
                                            La <b>SOFOM</b> realizará la verificación del <b>INE</b> exclusivamente para efectos de identificación del <b>ACREDITADO</b> y prevención de robo de identidad, por lo que en ningún caso los datos contenidos en la Credencial para Votar serán utilizados para fines publicitarios, de comercialización o distribución       del servicio.
                                            <br> <br>
                                            d)	Proporcionar a las instituciones u organismos relacionados con la administración, operación, y/o manejo de los medios de disposición, aquella información que se estime pertinente y que tenga que ver con el reporte, tratamiento y/o prevención de delitos, ilícitos o irregularidades, en el entendido de que cualquier tercero al que sea proporcionada la información personal del <b>ACREDITADO</b> se sujetará a los términos previstos en el Aviso de Privacidad de la <b>SOFOM</b>.
                                            </span>
                                            </p>
                                            """.trimMargin()
                                    }

                                    unsafe {
                                        +"""
                                            <p>
                                            Todos los datos personales recolectados al <b>ACREDITADO</b> se encuentran sujetos al tratamiento previsto en el Aviso de Privacidad simplificado, mismo que se incluye a este Contrato con el <b>ANEXO A</b> y su versión integral completa en la página electrónica <a href="https://karum.com/operadora" target="_blank">https://karum.com/operadora</a>.
                                            <br> <br>
                                            <b>VIGÉSIMA OCTAVA. ACEPTACIÓN</b>. El <b>ACREDITADO</b> manifiesta que previo a la firma de este Contrato lo ha leído y afirma que está de acuerdo en obligarse conforme a su clausulado, otorgando su consentimiento y aceptación con la Firma Electrónica que le fue generada a través de la <b>APLICACIÓN</b>. Asimismo, acepta tener conocimiento de las comisiones vigentes, <b>CAT</b>, tasas de interés, términos y condiciones y que para los efectos de este instrumento los intereses devengados y no pagados tendrán la consideración de saldo insoluto.
                                            <br> <br>
                                            El <b>ACREDITADO</b> está enterado y conoce de las penas y responsabilidades legales en que incurren las personas que al solicitar un crédito o para obtenerlo, falsean, alteran u ocultan información relevante para el otorgamiento o negativa del mismo, por lo que asegura que los datos proporcionados a la <b>SOFOM</b> son verdaderos y correctos.
                                            <br> <br>
                                            EL <b>ACREDITADO</b> contará con un período de gracia de 10 (diez) días hábiles posteriores a la firma del presente Contrato para cancelarlo sin responsabilidad y sin que se genere comisión alguna por este concepto, con independencia de aquellos cargos, cobros o comisiones que hayan ocurrido previo a dicha cancelación. Lo anterior, siempre y cuando el <b>ACREDITADO</b> no haya utilizado u operado los productos o servicios financieros contratados.
                                            <br> <br>
                                            <b>VIGÉSIMA NOVENA. EFECTOS DE LA FIRMA ELECTRÓNICA</b>. Ambas Partes aceptan que la firma electrónica sustituirá la firma autógrafa del <b>ACREDITADO</b> por una de carácter electrónico, por lo que las constancias documentales o técnicas en donde aparezca producirán los mismos efectos que las leyes otorguen a los documentos suscritos con firma autógrafa, en consecuencia, tendrán igual valor probatorio.
                                            <br> <br>
                                            El <b>ACREDITADO</b> reconoce el carácter personal e intransferible de la firma electrónica, la cual quedará bajo su custodia, control y cuidado, por lo que será de la exclusiva responsabilidad del <b>ACREDITADO</b> cualquier daño o perjuicio que pudiese sufrir como consecuencia del uso indebido de la misma. 
                                            <br> <br>
                                            El <b>ACREDITADO</b> manifiesta que conoce el alcance que en el presente Contrato se le atribuye a la firma electrónica, por lo que su uso es bajo su estricta responsabilidad. El <b>ACREDITADO</b>, en protección de sus propios intereses, deberá mantener la firma electrónica como confidencial, toda vez que el uso de la misma, para todos los efectos legales a que haya lugar, será atribuido al <b>ACREDITADO</b>, aún y cuando medie caso fortuito o fuerza mayor. 
                                            <br> <br>
                                            Las Partes convienen en que serán aplicables, en su momento, los términos del Código de Comercio y cualquier otra disposición aplicable, respecto de la identidad y expresión de consentimiento de las mismas por medios electrónicos, ópticos o de cualquier otra tecnología mediante el uso de la firma electrónica, a fin de que los mensajes de datos sean comunicados entre las Partes de manera segura en su identificación, auténticos e íntegros en su contenido y no repudiables respecto del emisor y receptor.
                                            </p>
                                            """.trimMargin()
                                    }

                                    unsafe {
                                        +"""
                                            <p>
                                            <b>TRIGÉSIMA. SEGUROS, SERVICIOS Y/O ASISTENCIAS ADICIONALES</b>. Las Partes convienen que, en caso de así consentirlo en los términos y condiciones de la <b>APLICACIÓN</b>, la <b>SOFOM</b> podrá ofrecer directamente o a través de terceros, de manera gratuita o con costo, seguros, asistencias y/o servicios adicionales, siempre y cuando cumpla con todos los requisitos establecidos por la <b>SOFOM</b> o prestadores de servicios que en su caso corresponda. Los términos y condiciones de estos seguros, asistencias y/o servicios adicionales, serán puestos a disposición del <b>ACREDITADO</b> en la página de internet <a href="https://karum.com/operadora" target="_blank">https://karum.com/operadora</a> y/o en la página web de los terceros que presten los servicios, para que puedan ser impresos y consultados en cualquier momento. El ofrecimiento de los seguros, asistencias y/o servicios adicionales que se mencionan en esta cláusula puede variar, suspenderse o cancelarse en cualquier momento, sin responsabilidad alguna para la <b>SOFOM</b>. Cualquier término prestación o condición de los seguros, asistencias y/o servicios adicionales antes mencionados, será responsabilidad directa de la empresa que presta los servicios de seguros, por lo que la <b>SOFOM</b> no asumirá responsabilidad alguna en el evento de que el prestador de seguros no llegue a proporcionar los servicios adecuadamente, debiendo en todo caso dirimirse dicha reclamación directamente entre el <b>ACREDITADO</b> y el prestador de servicios de seguros de que se trate, obligándose este último a sacar en paz y a salvo a la <b>SOFOM</b> de cualquier controversia relacionada con dichos seguros. La cancelación del (los) seguro(s), asistencia(s) y/o servicio(s) adicional(es) que el <b>ACREDITADO</b> realice, no implicará la cancelación o terminación anticipada del presente Contrato.
                                            <br> <br>
                                            <b>TRIGÉSIMA PRIMERA. UNIDAD ESPECIALIZADA DE ATENCIÓN A USUARIOS, ACLARACIONES Y QUEJAS</b>. El <b>ACREDITADO</b> podrá notificar, y realizar telefónicamente cualquier aclaración o consulta, sin costo, de forma inmediata a la Unidad Especializada de Atención a Usuarios, en donde se le proporcionará una clave, para que se le dé el seguimiento correspondiente. Los datos de la Unidad Especializada de Atención a Usuarios son los siguientes:
                                            </p>
                                            """.trimMargin()
                                    }

                                    ul {
                                        style = "color:#182035;"
                                        unsafe {
                                            +"""
                                            <li>
                                            Correo electrónico UNE: <a href="mailto:atencionaclientes@karum.com">atencionaclientes@karum.com</a>.  
                                            </li>
                                            <li>
                                            Correo electrónico atención a usuarios: <a href="mailto:atencionaclientes@karum.com">atencionaclientes@karum.com</a>
                                            </li>
                                            <li>
                                            Horario: 09:00 a 18:00 horas.
                                            </li>
                                            <li>
                                            Teléfono UNE: (55) 8852 8207.
                                            </li>
                                            <li>
                                            Teléfonos de atención a usuarios: 5568222627.
                                            </li>
                                            <li>
                                            Domicilio: Blvd. Manuel Ávila Camacho No. 5, Interior S 1000, Edificio Torre B, Piso 10, Oficina 1045, Col. Lomas de Sotelo, CP 53390, Naucalpan de Juárez, Estado de México.
                                            </li>
                                            """.trimMargin()
                                        }
                                    }

                                    unsafe {
                                        +"""
                                            <p>
                                            En caso de alguna reclamación, el <b>ACREDITADO</b> deberá presentarla por escrito, ya sea física o electrónicamente, a la Unidad Especializada de Atención a Usuarios.
                                            <br> <br>
                                            La Unidad Especializada de Atención a Usuarios emitirá una respuesta y se la notificará al <b>ACREDITADO</b> en un plazo que no excederá de 30 (treinta) días naturales contados a partir de la presentación de la aclaración, inconformidad, reclamación o queja.
                                            <br> <br>
                                            <b>TRIGÉSIMA SEGUNDA. LEGISLACIÓN APLICABLE Y JURISDICCIÓN</b>. Para todo lo relativo a la interpretación y cumplimiento del presente Contrato, las Partes convienen en sujetarse a lo establecido en las diversas disposiciones emitidas por las autoridades financieras competentes, a las contenidas en las leyes mercantiles y civiles que sean aplicables, así como a las sanas prácticas y usos bancarios y mercantiles de la República Mexicana.
                                            <br> <br>
                                            Asimismo, las Partes convienen expresamente en que la solución de controversias que llegaren a surgir se someterán a los tribunales competentes de la Ciudad de México, renunciando expresamente a cualquier otro fuero que pudiere corresponderles en virtud de sus domicilios presentes o futuros o por cualquier otra razón. No obstante lo anterior, en caso de cualquier controversia derivada del presente Contrato, el ACREDITADO tendrá la facultad, sí así lo decide, de acudir previamente a la Comisión Nacional para la Protección y Defensa de los Usuarios de Servicios Financieros.
                                            <br> <br>
                                            El número telefónico de atención a usuarios de la <b>CONDUSEF</b> es: (5340-0999 o LADA sin costo 01-800-999-8080), dirección en Internet (www.condusef.gob.mx) y correo electrónico (asesoria@condusef.gob.mx).
                                            <br> <br>
                                            Es del conocimiento del <b>ACREDITADO</b> que la <b>SOFOM</b> no requiere autorización de la Secretaría de Hacienda y Crédito Público; y para la realización de sus operaciones está sujeta a la supervisión de la Comisión Nacional Bancaria y de Valores únicamente para efectos de lo dispuesto por el artículo 56 de la Ley General de Organizaciones y Actividades Auxiliares del Crédito.
                                            <br> <br>
                                            El presente Contrato, datos y referencias consignados en la carátula y <b>SOLICITUD</b>, constituyen la expresión de la voluntad de las Partes, por lo que se suscribe por la <b>SOFOM</b> y por el <b>ACREDITADO</b> en la Ciudad de <b>[entidad federativa donde se suscribe el documento]</b> a los <b>[indicar día]</b> días del mes de <b>[indicar mes]</b>  del año <b>[indicar año]</b>.
                                            <br> <br>
                                            EN ESTE ACTO SE LE ENTREGA UNA COPIA DEL PRESENTE CONTRATO AL <b>ACREDITADO</b> EN FORMA FÍSICA O POR MEDIOS ELECTRÓNICOS.
                                            </p>
                                            """.trimMargin()
                                    }

                                    div("row") {
                                        style = "margin-top:50px;"
                                        div("col-md-6") {
                                            style = "padding:0px 30px;"
                                            h6 {
                                                style = "text-align:center; font-weight:bold; margin-bottom:64px;"
                                                +"SOFOM"
                                            }
                                            hr {
                                                style = "height:2px; color:#000; margin-top:30px;"
                                            }
                                            p {
                                                style = "padding:0; font-size:14px; text-align:center;"
                                                +"Karum Operadora de Pagos S.A.P.I. de C.V., SOFOM E.N.R."
                                            }
                                        }
                                        div("col-md-6") {
                                            style = "padding:0px 30px;"
                                            h6 {
                                                style = "text-align:center; font-weight:bold; margin-bottom:64px"
                                                +"ACREDITADO"
                                            }
                                            h6 {
                                                style = "text-align:center; font-weight:bold; margin-top:25px;"
//                                                +"[Firma del ACREDITADO]"
                                            }
                                            hr {
                                                style = "height:2px; color:#000; margin-top:30px;"
                                            }
                                            p {
                                                style =
                                                    "font-weight:bold; text-align:center; font-size:14px; padding:0;"
//                                                +"[Nombre completo del ACREDITADO]"
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    div {
                        style = "text-align:center; margin:30px 0px; cursor: pointer;"
                        a(classes = "declaration-btn") {
//                            href = "/goodBye".withBaseUrl()
                            onClick = "onNotAgreeBtnClick()"
                            +"No estoy de acuerdo"
                        }

                        a(classes = "declaration-btn") {
                            id = "declarationAcceptBtn"
                            style = "cursor: pointer;"
                            onClick = "onAcceptBtnClick()"
                            +"De acuerdo"
                        }
                    }

                    form {
                        div {
                            div(classes = "toast") {
                                style = "margin:auto; background:none; border:none; box-shadow: none;"
                                div(classes = "toast-body") {
                                    h4("mr-auto text-danger") {
                                        style = "text-align:center;"
                                        id = "add_toast_id"
                                    }
                                }
                            }
                        }

                    }
                }

                div(classes = "modal fade") {
                    style = "padding-top:70px;"
                    id = "completeModal"
                    role = "dialog"

                    div(classes = "modal-dialog") {
                        style = "width:75%; margin:auto;"
                        div("modal-content") {
                            style = "background-color:#fff; border:4px solid #ffdcB3; border-radius:10px;"
                            div("modal-body") {
                                h3 {
                                    style = "font-weight:700; color:#ff6700; text-align:center;"
                                    +"Trámite enviado!"
                                }
                                div {
                                    style = "width:96%; margin:auto; margin-top:20px;"
                                    h6 {
                                        style = "padding-left: 32px; color: #000; font-weight: 500;"
                                        +"Siguientes pasos:"
                                    }

                                    /*ul {
                                        style = "color:#000; line-height:1.8rem;"
                                        li { +"Nuesta Central de Crédito analizará tu trámite." }
                                        li { +"En caso de requerir algún documento o información adicional te llamará." }
                                        li { +"Una vez autorizado tu crédito, recibiras un mensaje de texto con tu número de referencia para realizar el pago del 30% en tiendas OXXO." }
                                    }*/

                                    ul {
                                        style = "color:#000; font-size:0.85rem;"
                                        li { +"Nuestra Central de Crédito procederá a evaluar su solicitud de crédito." }
                                        li { +"En caso de llegar a requerir algún documento o información adicional uno de nuestros ejecutivos se comunicará con usted." }
                                        li { +"Una vez que su solicitud de crédito haya sido evaluada, recibirá un SMS para informarle el estatus final de su solicitud." }
                                        li { +"De ser autorizado su crédito, recibirá un mensaje de texto (SMS), el cual contendrá su número de referencia único para realizar el pago del enganche en tiendas OXXO, correspondiente al 30% del costo de su nuevo teléfono celular. El pago deberá ser efectuado en una sola exhibición, por la cantidad exacta que se indica en el mensaje." }
                                        li { +"Su número de referencia único tendrá una vigencia de 7 días naturales. De no realizar el pago en este plazo, la venta será cancelada." }
                                        li { +"Una vez efectuado el pago del enganche, comenzará el proceso de preparación para el envío de su teléfono a su domicilio." }
                                        li { +"Recibirá un mensaje SMS con su número de referencia permanente, con el cual podrá efectuar el pago de sus mensualidades solamente en tiendas OXXO." }
                                    }
                                }

                                div {
                                    style = "width:15%; margin:10px auto;"
                                    img {
                                        style = "text-align:center;"
                                        src = "/assets/media/tick.png"
                                        width = "65px"
                                        height = "65px"
                                    }
                                }

                                /*h3 {
                                    style = "color:#ffb700; text-align:center; margin-top:10px; font-size:1.2rem;"
                                    +"Número de Confirmación"
                                }*/

                                /*h4 {
                                     style =
                                         "color:#fff; text-align:center; margin-top:10px; font-size:17px;padding:0px 25px;"
                                     +"Se envie el numero de cuenta"
                                 }*/

                                /*h3 {
                                    id = "tc44Code"
                                    style = "color:#000; margin-top:10px; text-align:center; font-weight:700;"
                                    +"XXXXXX"
                                }*/

                                div {
                                    style = "text-align:center;"
                                    a(classes = "completeBtn") {
                                        href = "/goodBye".withBaseUrl()
                                        +"Entendido"
                                    }

                                    /*h4 {
                                        style = "color:#000; text-align:center; margin-top:10px; font-size:1rem;"
                                        +"Numero de confirmacion"
                                    }*/
                                }
                            }
                        }
                    }
                }

                div(classes = "modal fade") {
                    style = "padding-top:100px;"
                    id = "notAgreeModal"
                    role = "dialog"

                    div(classes = "modal-dialog") {
                        style = "width:75%; margin:auto;"
                        div("modal-content") {
                            style = "background-color:#fff; border:4px solid #ffdcB3; border-radius:10px;"
                            div("modal-body") {
                                style = "padding:50px 0px;"
                                h3 {
                                    style = "font-weight:700; color:#ff6700; text-align:center;"
                                    +"En desacuerdo!"
                                }
                                div {
                                    style = "width:90%; margin:auto; margin-top:25px; padding:15px;"
                                    h6 {
                                        id = "firstBtnClickText"
                                        style = "color:#000; text-align:center; font-size:1.1rem; display:none;"
                                        +"""Para continuar y aprobar su crédito, así como la compra del equipo celular 
                                            necesitas estar de acuerdo con este contrato.""".trimIndent()
                                    }

                                    h6 {
                                        id = "secondBtnClickText"
                                        style = "color:#000; text-align:center; font-size:1.1rem; display:none;"
                                        +"""Tu solicitud no se procesará debido a que no aceptaste el contrato de crédito.
                                            Muchas gracias.
                                        """.trimIndent()
                                    }
                                }

                                div {
                                    style = "text-align:center;"
                                    a(classes = "completeBtn") {
                                        style = "cursor:pointer;"
                                        onClick = "onCloseBtnClick()"
                                        +"OK"
                                    }
                                }
                            }
                        }
                    }
                }

                modalLoader()
                jscript(src = "assets/js/main.js")
                jscript(src = "assets/js/declaration-page.js")
            }
        }
    }

    suspend fun goodByePage() {
        /*   val userSession = call.userSession
           val user = UserModel.getUser(userSession.mobile) ?: error("Invalid session!")
           if (user.status < UserModel.MOBILE_DATA_COMPLETE) {
               if (pageRedirect()) return
           }*/
        call.respondHtml(HttpStatusCode.OK) {
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

//                css("assets/css/info-style.css")
                css("assets/css/goodBye-style.css")
                css(href = "https://use.fontawesome.com/releases/v5.15.4/css/all.css")
                css("https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css")
                jscript(src = "https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js")

                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js")
                jscript(src = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js")
                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.2/jquery.form.min.js")
                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.19.1/jquery.validate.min.js")

            }

            body {
//                div("goodBye")
                div(classes = "goodBye-instant") {
                    img {
                        src = "assets/media/instant-logo.png"
                    }
                    h1 { +"Tu smartphone al instante." }
                }
            }
        }
    }

    /*

            data class Stat(val title: String, val subtitle: String, val value: String, val colSize: String)

        private fun FlowContent.dashBoardStatRow2(stats: List<Stat>) {
            val fonts = listOf("kt-font-brand", "kt-font-success", "kt-font-warning", "kt-font-danger")
            //<!--begin:: Widgets/Stats-->
            div(classes = "kt-portlet") {
                div(classes = "kt-portlet__body  kt-portlet__body--fit") {
                    div(classes = "row row-no-padding row-col-separator-lg") {
                        stats.forEachIndexed { index, stat ->
                            div(classes = "col-md-12 col-lg-6 col-xl-${stat.colSize}") {

                                //<!--begin::Total Profit-->
                                div(classes = "kt-widget24") {
                                    div(classes = "kt-widget24__details") {
                                        div(classes = "kt-widget24__info") {
                                            h4(classes = "kt-widget24__title") {
                                                +stat.title
                                            }
                                            span(classes = "kt-widget24__desc") {
                                                +stat.subtitle
                                            }
                                        }
                                        span(classes = "kt-widget24__stats ${fonts[index % fonts.size]}") {
                                            id = "first-stat-value"
                                            +stat.value
                                        }
                                    }
                                    /*div(classes = "progress progress--sm") {
                                        div(classes = "progress-bar kt-bg-brand") {
                                            role = "progressbar"
                                            style = "width: 78%;"
            //        aria-valuenow="50" aria-valuemin="0" aria-valuemax="100"
                                        }
                                    }
                                    div(classes = "kt-widget24__action") {
                                        span(classes = "kt-widget24__change") {
                                            +"Change"
                                        }
                                        span(classes = "kt-widget24__number") {
                                            +"78%"
                                        }
                                    }*/
                                }

                                //<!--end::Total Profit-->
                            }
                        }
                    }
                }
            }

            //<!--end:: Widgets/Stats-->
        }


           suspend fun indexPage() {
            pageTemplate {
                css("assets/css/dashboard.css")

                h3 {
                    +"Karum Card Management!"
                }
                val registered = 100
                val approved = 35
                val pending = 75

                dashBoardStatRow2(
                    listOfNotNull(
                        Stat("Registered", "Application", "$registered", "3"),
                        Stat("Approved", "Application", "$approved", "3"),
                        Stat("Pending", "Application", "$pending", "3"),
                    )
                )
            }
        }

        suspend fun questionnairePage() {
            pageTemplate {
                portlet(title = "Request For Karum Card") {
                    div {
                        style = "display:block; margin-left:auto; margin-right:auto; margin-bottom:32px;"
                        h4 {
                            style = "color:black; margin-bottom:24px;"
                            +"We want to know you better, so please answer the following questions carefully!"
                        }

                        div(classes = "row") {
                            style = "padding-left:32px;"
                            div(classes = "col-lg-6") {
                                ul {
                                    style = "list-style-type:circle; font-size:18px; padding:0;"
                                    li {
                                        +"I declare under pretense of telling the truth that I do not currently hold or during the immediately preceding year any prominent public position at the federal, state, municipal or district level in Mexico or abroad"
                                    }
                                }
                            }
                            div(classes = "col-lg-2") {
                                style = "margin-top:auto; margin-bottom:auto; margin-top:auto; margin-bottom:auto;"
                                div {
                                    div(classes = "form-check") {
                                        style = "margin-bottom:8px;"
                                        input(classes = "form-check-input", type = InputType.checkBox) {
                                            value = ""
                                            id = "yesIAcceptId"
                                        }
                                        label(classes = "form-check-label") { +"Yes, I Accept" }
                                    }
                                    div(classes = "form-check") {
                                        style = "margin-top:8px;"
                                        input(classes = "form-check-input", type = InputType.checkBox) {
                                            value = ""
                                            id = "iDoNotAcceptId"
                                        }
                                        label(classes = "form-check-label") { +"I do not Accept" }
                                    }
                                }
                            }
                        }

                        div(classes = "row") {
                            style = "margin-top:32px; padding-left:32px;"
                            div(classes = "col-lg-6") {
                                ul {
                                    style = " list-style-type:circle; font-size:18px; padding:0;"
                                    li {
                                        +"I declare under pretense of telling the truth that I do not currently hold or during the immediately preceding year any prominent public position at the federal, state, municipal or district level in Mexico or abroad"
                                    }
                                }
                            }
                            div(classes = "col-lg-2") {
                                style = "margin-top:auto; margin-bottom:auto; margin-top:auto; margin-bottom:auto;"

                                div {
                                    div(classes = "form-check") {
                                        style = "margin-top:8px;"
                                        input(classes = "form-check-input", type = InputType.checkBox) {
                                            value = ""
                                            id = "yesIAcceptId"
                                        }
                                        label(classes = "form-check-label") { +"Yes, I Accept" }
                                    }
                                    div(classes = "form-check") {
                                        style = "margin-top:8px;"
                                        input(classes = "form-check-input", type = InputType.checkBox) {
                                            value = ""
                                            id = "iDoNotAcceptId"
                                        }
                                        label(classes = "form-check-label") { +"I do not Accept" }
                                    }
                                }
                            }
                        }

                        div(classes = "row") {
                            style = "margin-top:32px; padding-left:32px;"
                            div(classes = "col-lg-6") {
                                ul {
                                    style = " list-style-type:circle; font-size:18px; padding:0;"
                                    li {
                                        +"I also declare that my spouse, if any, or relative by consanguinity of affinity up to the 2nd degree, does no currently hold or during the immediately preceding year any outstanding public office at the federal state, municipal or district level in Mexico or abroad"
                                    }
                                }
                            }
                            div(classes = "col-lg-2") {
                                style = "margin-top:auto; margin-bottom:auto; margin-top:auto; margin-bottom:auto;"

                                div {
                                    div(classes = "form-check") {
                                        style = "margin-top:8px;"
                                        input(classes = "form-check-input", type = InputType.checkBox) {
                                            value = ""
                                            id = "yesIAcceptId"
                                        }
                                        label(classes = "form-check-label") { +"Yes, I Accept" }
                                    }
                                    div(classes = "form-check") {
                                        style = "margin-top:8px;"
                                        input(classes = "form-check-input", type = InputType.checkBox) {
                                            value = ""
                                            id = "iDoNotAcceptId"
                                        }
                                        label(classes = "form-check-label") { +"I do not Accept" }
                                    }
                                }
                            }
                        }

                        div(classes = "row") {
                            style = "margin-top:32px; padding-left:32px;"
                            div(classes = "col-lg-6") {
                                ul {
                                    style = " list-style-type:circle; font-size:18px; padding:0;"
                                    li {
                                        +"I also declare that my spouse, if any, or relative by consanguinity of affinity up to the 2nd degree, does no currently hold or during the immediately preceding year any outstanding public office at the federal state, municipal or district level in Mexico or abroad"
                                    }
                                }
                            }
                            div(classes = "col-lg-2") {
                                style = "margin-top:auto; margin-bottom:auto; margin-top:auto; margin-bottom:auto;"

                                div {
                                    div(classes = "form-check") {
                                        style = "margin-top:8px;"
                                        input(classes = "form-check-input", type = InputType.checkBox) {
                                            value = ""
                                            id = "yesIAcceptId"
                                        }
                                        label(classes = "form-check-label") { +"Yes, I Accept" }
                                    }
                                    div(classes = "form-check") {
                                        style = "margin-top:8px;"
                                        input(classes = "form-check-input", type = InputType.checkBox) {
                                            value = ""
                                            id = "iDoNotAcceptId"
                                        }
                                        label(classes = "form-check-label") { +"I do not Accept" }
                                    }
                                }
                            }
                        }

                        div(classes = "row") {
                            style = "margin-top:32px; padding-left:32px;"
                            div(classes = "col-lg-6") {
                                ul {
                                    style = " list-style-type:circle; font-size:18px; padding:0;"
                                    li {
                                        +"I also declare that my spouse, if any, or relative by consanguinity of affinity up to the 2nd degree, does no currently hold or during the immediately preceding year any outstanding public office at the federal state, municipal or district level in Mexico or abroad"
                                    }
                                }
                            }
                            div(classes = "col-lg-2") {
                                style = "margin-top:auto; margin-bottom:auto; margin-top:auto; margin-bottom:auto;"

                                div {
                                    div(classes = "form-check") {
                                        style = "margin-top:8px;"
                                        input(classes = "form-check-input", type = InputType.checkBox) {
                                            value = ""
                                            id = "yesIAcceptId"
                                        }
                                        label(classes = "form-check-label") { +"Yes, I Accept" }
                                    }
                                    div(classes = "form-check") {
                                        style = "margin-top:8px;"
                                        input(classes = "form-check-input", type = InputType.checkBox) {
                                            value = ""
                                            id = "iDoNotAcceptId"
                                        }
                                        label(classes = "form-check-label") { +"I do not Accept" }
                                    }
                                }
                            }
                        }

                        div(classes = "row") {
                            style = "margin-top:32px; padding-left:32px;"
                            div(classes = "col-lg-6") {
                                ul {
                                    style = " list-style-type:circle; font-size:18px; padding:0;"
                                    li {
                                        +"I also declare that my spouse, if any, or relative by consanguinity of affinity up to the 2nd degree, does no currently hold or during the immediately preceding year any outstanding public office at the federal state, municipal or district level in Mexico or abroad"
                                    }
                                }
                            }
                            div(classes = "col-lg-2") {
                                style = "margin-top:auto; margin-bottom:auto; margin-top:auto; margin-bottom:auto;"

                                div {
                                    div(classes = "form-check") {
                                        style = "margin-top:8px;"
                                        input(classes = "form-check-input", type = InputType.checkBox) {
                                            value = ""
                                            id = "yesIAcceptId"
                                        }
                                        label(classes = "form-check-label") { +"Yes, I Accept" }
                                    }
                                    div(classes = "form-check") {
                                        style = "margin-top:8px;"
                                        input(classes = "form-check-input", type = InputType.checkBox) {
                                            value = ""
                                            id = "iDoNotAcceptId"
                                        }
                                        label(classes = "form-check-label") { +"I do not Accept" }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        suspend fun cardApplicationPage() {

            pageTemplate {

                portlet(title = "Request For Karum Card") {
                    form {
                        id = "application-form"
                        method = FormMethod.post

                        div(classes = "row") {
                            style = "margin-bottom:32px;"
                            div("col-lg-3") {
                                formInputWithLabel(label = "Store Number", type = InputType.number) {
                                    min = "0"
                                    name = "store_number"
                                }
                            }

                            div("col-lg-3") {
                                formInputWithLabel(label = "Promoter Key", type = InputType.text) {
                                    name = "promoter_key"
                                }
                            }

                            div("col-lg-3") {
                                formInputWithLabel(label = "Type of Request", type = InputType.text) {
                                    name = "type_of_request"
                                }
                            }
                        }

                        div("row") {
                            style = "border-style: dotted; padding:32px; "

                            div("col-lg-12") {
                                style = "margin-bottom:24px;"
                                h4 { +"Personal Information" }
                            }


                            div("col-lg-4") {
                                style = "margin-bottom:16px;"
                                formInputWithLabel(label = "First Name", type = InputType.text) {
                                    name = "first_name"
                                }
                            }

                            div("col-lg-4") {
                                style = "margin-bottom:16px;"
                                formInputWithLabel(label = "Last Name", type = InputType.text) {
                                    name = "first_name"
                                }
                            }

                            div("col-lg-4") {
                                style = "margin-bottom:16px;"
                                formInputWithLabel(label = "Mobile", type = InputType.text) {
                                    name = "mobile_number"
                                }
                            }

                            div(classes = "col-lg-4") {
                                style = "margin-bottom:16px;"
                                label { +"Gender" }
                                select(classes = "form-control") {
                                    name = "state_of_birth"
                                    option {
                                        value = ""
                                        +"Select"
                                    }
                                    option {
                                        value = "1"
                                        +"Men"
                                    }

                                    option {
                                        value = "2"
                                        +"Women"
                                    }


                                }
                            }

                            div(classes = "col-lg-4") {
                                style = "margin-bottom:16px;"
                                label { +"Fecha de Nacimento" }
                                dateInput(classes = "form-control") {
                                    name = "date_of_birth"
                                    placeholder = "Fecha de Nacimento"
                                    value = LocalDate.now().toString()
                                }
                            }

                            div(classes = "col-lg-4") {
                                style = "margin-bottom:16px;"
                                label { +"State of birth" }
                                select(classes = "form-control") {
                                    name = "state_of_birth"
                                    option {
                                        value = "1"
                                        +"California"
                                    }
                                    option {
                                        value = "2"
                                        +"Alaska"
                                    }

                                    option {
                                        value = "3"
                                        +"Texas"
                                    }

                                    option {
                                        value = "4"
                                        +"Arizona"
                                    }

                                    option {
                                        value = "5"
                                        +"Vermont"
                                    }
                                }
                            }

                            div(classes = "col-lg-4") {
                                button(classes = "btn btn-warning") {
                                    id = "generate_curp"
                                    style = "margin-top:15px;"
                                    +"Generate CURP"
                                }
                            }

                        }

                        div("row") {
                            style = "border-style: dotted; padding:32px; margin-top:32px"

                            div("col-lg-12") {
                                style = "margin-bottom:24px;"
                                h4 { +"Home Information" }
                            }

                            div("col-lg-4") {
                                style = "margin-bottom:16px;"
                                formInputWithLabel(label = "Street Number", type = InputType.text) {
                                    name = "street_number"
                                }
                            }

                            div("col-lg-2") {
                                style = "margin-bottom:16px;"
                                formInputWithLabel(label = "Ext No.", type = InputType.number) {
                                    name = "ext_no"
                                }
                            }

                            div("col-lg-2") {
                                style = "margin-bottom:16px;"
                                formInputWithLabel(label = "Int No.", type = InputType.number) {
                                    name = "int_no"
                                }
                            }

                            div("col-lg-4") {
                                style = "margin-bottom:16px;"
                                formInputWithLabel(label = "C.P", type = InputType.number) {
                                    name = "cp_number"
                                }
                            }

                            div(classes = "col-lg-4") {
                                style = "margin-bottom:16px;"
                                label { +"State" }
                                select(classes = "form-control") {
                                    disabled = true
                                    name = "state"
                                    option {
                                        value = ""

                                    }
                                    option {
                                        value = "1"
                                        +"Mexico"
                                    }

                                    option {
                                        value = "2"
                                        +"Arizona"
                                    }


                                }
                            }

                            div(classes = "col-lg-4") {
                                style = "margin-bottom:16px;"
                                label { +"Municipality" }
                                select(classes = "form-control") {
                                    disabled = true
                                    name = "state"
                                    option {
                                        value = ""

                                    }
                                    option {
                                        value = "1"
                                        +"Mexico"
                                    }

                                    option {
                                        value = "2"
                                        +"Arizona"
                                    }


                                }
                            }

                            div("col-lg-4") {
                                style = "margin-bottom:16px;"
                                formInputWithLabel(label = "Town", type = InputType.text) {
                                    name = "Town"
                                }
                            }


                            div("col-lg-4") {
                                style = "margin-bottom:16px;"
                                formInputWithLabel(label = "Cell Phone", type = InputType.text) {
                                    name = "cell_phone"
                                }
                            }

                            div("col-lg-4") {
                                style = "margin-bottom:16px;"
                                formInputWithLabel(label = "Confirm Cell Phone", type = InputType.text) {
                                    name = "confirm_cell_phone"
                                }
                            }

                            div("col-lg-4") {
                                style = "margin-bottom:16px;"
                                formInputWithLabel(label = "Email", type = InputType.text) {
                                    name = "email"
                                }
                            }

                        }

                        div("row") {
                            style = "border-style: dotted; padding:32px; margin-top:32px; "

                            div("col-lg-12") {
                                style = "margin-bottom:24px;"
                                h4 { +"Financial Information" }
                            }


                            div("col-lg-4") {
                                style = "margin-bottom:16px;"
                                formInputWithLabel(label = "Company Name", type = InputType.text) {
                                    name = "company_name"
                                }
                            }

                            div("col-lg-4") {
                                style = "margin-bottom:16px;"
                                formInputWithLabel(label = "Company Phone", type = InputType.text) {
                                    name = "company_phone"
                                    maxLength = "10"
                                }
                            }

                            div("col-lg-4") {
                                style = "margin-bottom:16px;"
                                formInputWithLabel(label = "Monthly Income", type = InputType.number) {
                                    name = "monthly_income"
                                }
                            }

                        }

                        div("row") {
                            style = "border-style: dotted; padding:32px; margin-top:32px; "

                            div("col-lg-12") {
                                style = "margin-bottom:24px;"
                                h4 { +"National Identification" }
                            }

                            div(classes = "col-lg-5") {
                                style = "margin-bottom:16px;"
                                label { +"Form of Identification" }
                                select(classes = "form-control") {
                                    name = "state_of_birth"
                                    option {
                                        value = "1"
                                        +"IFE/INE"
                                    }
                                }
                            }

                            div("col-lg-5") {
                                style = "margin-bottom:16px;"
                                formInputWithLabel(label = "Passport number", type = InputType.text) {
                                    name = "passport_number"
                                }
                            }

                            div(classes = "col-lg-5") {
                                label { +"Front Identification" }
                                fileInput(classes = "form-control") { name = "ine_front_side" }
                            }

                            div(classes = "col-lg-5") {
                                label { +"Back Identification" }
                                fileInput(classes = "form-control") { name = "ine_back_side" }
                            }

                        }

                        div(classes = "form-check") {
                            style = "margin-top:24px"
                            input(classes = "form-check-input", type = InputType.checkBox) {
                                value = ""
                                id = ""
                            }
                            label(classes = "form-check-label") { +"I Accept" }

                        }

                        div(classes = "form-check") {
                            style = "margin-top:16px;"
                            input(classes = "form-check-input", type = InputType.checkBox) {
                                value = ""
                                id = ""
                            }
                            label(classes = "form-check-label") { +"I Accept" }

                        }

                    }

                    modal(
                        "modal_otp_id",
                        "Authorization For",
                        "Send",
                        primaryButtonId = "modal_otp_id",
                        modalSize = ModalSize.LARGE,
                    ) {
                        form {
                            method = FormMethod.post
                            p {
                                style = "font-size:16px;"
                                +"Through this channel, I expressly authorize Karum Operadora de Pagos SAPI de CV to investigate my credit behavior in the credit information companies that it deems appropriate in accordance with the provisions of Art."
                            }

                            div {
                                style =
                                    "display:block; margin-left:auto; margin-right:auto; margin-top:32px; margin-bottom:32px;"

                                label { +"To Authorize enter OTP:" }
                                input(classes = "form-") { }
                                div(classes = "col-lg-4") {
                                    formInputWithLabel(label = "To Authorize enter OTP:", type = InputType.text) {
                                        name = "otp_num"
                                    }
                                }
                            }
                        }
                    }

                    div(classes = "row") {

                        button(classes = "btn btn-brand btn-primary", type = ButtonType.button) {
                            attributes.apply {
                                put("data-toggle", "modal")
                                put("data-target", "#modal_otp_id")
                            }
                            id = "settings-form-button"
                            style = "margin-top:15px; margin-top:24px;"
                            +"Send Application"
                        }
                    }

                }

                bottomBlock = {
                    jscript("assets/js/card_application.js")
                }
            }
        }

        suspend fun supplementaryDataPage() {
            call.respondHtml(HttpStatusCode.OK) {
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
                    css(href = "https://use.fontawesome.com/releases/v5.15.4/css/all.css")
                    css("https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css")
                    jscript(src = "https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js")

                    jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js")
                    jscript(src = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js")
                    jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.2/jquery.form.min.js")
                    jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.19.1/jquery.validate.min.js")

                }

                body {
                    section {
                        div("main-container") {
                            header(classes = "info-header") {
                                span("karum-logo2") {
                                    img {
                                        src = "/assets/media/karum-logo.png"
                                        height = "80px"
                                    }
                                }
                                h1(classes = "splash-title-left") {
                                    style = "display:inline-block;"
                                    +"APPROVA"
                                }
                                h2(classes = "splash-subtitle-left") {
                                    style = "display:inline-block;"
                                    +"- Pre - calificación"
                                }
                            }

                            div(classes = "container") {
                                form {
                                    div("row") {
                                        style = "padding:20px 25px;"

                                        h3("form-heading") { +"Datos Complementarios" }

                                        div(classes = "row") {
                                            style = "margin-bottom:15px; font-size:1rem;"
                                            div(classes = "col-md-4") {
                                                label { +"País de nacimiento" }
                                                p {
                                                    style = "color:#fff; font-size:1rem;"
                                                    +"MEXICO"
                                                }
                                            }

                                            div(classes = "col-md-4") {
                                                label { +"Nacionalidad" }
                                                p {
                                                    style = "color:#fff; font-size:1rem;"
                                                    +"MEXICANA"
                                                }
                                            }
                                        }

                                        div(classes = "row") {
                                            style = "margin-bottom:15px; font-size:1rem;"
                                            div(classes = "col-md-4") {
                                                label(classes = "form-custom") { +"Medio de envío del estado de cuenta" }
                                                p {
                                                    style = "color:#fff; font-size:1rem;"
                                                    +"ELECTRONICO"
                                                }
                                            }

                                            div(classes = "col-md-4") {
                                                label(classes = "form-custom") { +"Profesion" }
                                                input(classes = "custom-info-form", type = InputType.text) {
                                                    style = "width:85%"
                                                    required = true
                                                    placeholder = " "
                                                }
                                            }
                                        }

                                        div(classes = "row") {
                                            style = "margin-bottom:15px; font-size:1rem;"
                                            div(classes = "col-md-4") {
                                                label(classes = "form-custom") { +"Telefono de Casa" }
                                                input(classes = "custom-info-form", type = InputType.text) {
                                                    style = "width:85%"
                                                    required = true
                                                    placeholder = " "
                                                }
                                            }

                                            div(classes = "col-md-4") {
                                                label(classes = "form-custom") { +"Antiguedad Domicilio" }
                                                br
                                                label { +"Años:" }
                                                select(classes = "info-form-select-short") {
                                                    style = "width:25%;"
                                                    name = "year"
                                                    id = "year"
                                                }

                                                label {
                                                    style = "margin-left: 10px;"
                                                    +"Meses:"
                                                }
                                                select(classes = "info-form-select-short") {
                                                    style = "width:30%;"
                                                    name = "month"
                                                    id = "month"
                                                }
                                                select(classes = "info-form-select-short") {
                                                    id = "day"
                                                    name = "day"
                                                    style = "display:none;"
                                                }
                                            }
                                        }

                                    }
                                }

                                div {
                                    style = "text-align:left; display:inline-block;"
                                    a {
                                        style = "width:45px; height:45px; display:none;"
                                        onClick = "window.history.go(-1); return false;"

                                        img {
                                            src = "/assets/media/back.png"
                                            width = "60px"
                                            height = "60px"
                                        }
                                    }
                                }

                                div {
                                    style = "float:right;display:inline-block; margin-top:-48px;"
                                    a {
                                        style = "width:45px; height:45px;"
                                        href = "/economicData".withBaseUrl()
                                        img {
                                            src = "/assets/media/forward.png"
                                            width = "60px"
                                            height = "60px"
                                        }
                                    }
                                }

                            }

                        }
                    }
                    jscript(src = "assets/js/info-page.js")
                }
            }
        }

        suspend fun economicDataPage() {
            call.respondHtml(HttpStatusCode.OK) {
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
                    css(href = "https://use.fontawesome.com/releases/v5.15.4/css/all.css")
                    css("https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css")
                    jscript(src = "https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js")

                    jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js")
                    jscript(src = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js")
                    jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.2/jquery.form.min.js")
                    jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.19.1/jquery.validate.min.js")

                }

                body {
                    section {
                        div("main-container") {
                            header(classes = "info-header") {
                                span("karum-logo2") {
                                    img {
                                        src = "/assets/media/karum-logo.png"
                                        height = "80px"
                                    }
                                }
                                h1(classes = "splash-title-left") {
                                    style = "display:inline-block;"
                                    +"APPROVA"
                                }
                                h2(classes = "splash-subtitle-left") {
                                    style = "display:inline-block;"
                                    +"- Pre - calificación"
                                }
                            }

                            div(classes = "container") {
                                style = "position:relative;"
                                span {
                                    style = "position:absolute; right:10px; top:10px;"
                                    img {
                                        src = "/assets/media/qr-code.gif"
                                        width = "60px"
                                        height = "60px"
                                    }
                                }
                                form {
                                    div("row") {
                                        style = "padding:20px 25px;"

                                        h3("form-heading") { +"Datos laborales y económicos del solicitante " }

                                        div(classes = "row") {
                                            style = "margin-bottom:15px; font-size:1rem;"
                                            div(classes = "col-md-12") {
                                                label(classes = "form-custom") { +"Nombre de la empresa" }
                                                input(classes = "custom-info-form", type = InputType.text) {
                                                    style = "width:100%;"
                                                    placeholder = " "
                                                    maxLength = "12"
                                                }
                                            }
                                        }

                                        div(classes = "row") {
                                            style = "margin-bottom:15px; font-size:1rem;"
                                            div(classes = "col-md-12") {
                                                input(classes = "custom-info-form-0", type = InputType.checkBox) {
                                                    style = " display:inline-block;"
                                                }
                                                p {
                                                    style = "color:#ffb700; font-size:0.875rem; display:inline-block;"
                                                    +"Tu direccionade trabajo es la misma que la tu domicillio"
                                                }
                                            }
                                        }

                                        div(classes = "row") {
                                            style = "margin-bottom:15px; font-size:1rem;"
                                            div(classes = "col-md-4") {
                                                label(classes = "form-custom") { +"Calle" }
                                                input(classes = "custom-info-form", type = InputType.text) {
                                                    style = "width:85%;"
                                                    required = true
                                                    placeholder = " "
                                                }
                                            }

                                            div(classes = "col-md-2") {
                                                label(classes = "form-custom") { +"No Ext" }
                                                input(classes = "custom-info-form", type = InputType.text) {
                                                    style = "width:85%;"
                                                    required = true
                                                    placeholder = " "
                                                }
                                            }

                                            div(classes = "col-md-2") {
                                                label(classes = "form-custom") { +"No Int" }
                                                input(classes = "custom-info-form", type = InputType.text) {
                                                    style = "width:85%;"
                                                    required = true
                                                    placeholder = " "
                                                }
                                            }

                                            div(classes = "col-md-4") {
                                                label(classes = "form-custom") { +"C.P" }
                                                input(classes = "custom-info-form", type = InputType.text) {
                                                    style = "width:100%;"
                                                    required = true
                                                    placeholder = " "
                                                }
                                            }
                                        }

                                        div(classes = "row") {
                                            style = "margin-bottom:15px; font-size:1rem;"
                                            div(classes = "col-md-4") {
                                                label(classes = "form-custom") { +"Estado" }
                                                input(classes = "custom-info-form", type = InputType.text) {
                                                    style = "width:85%;"
                                                    required = true
                                                    placeholder = " "
                                                }
                                            }

                                            div(classes = "col-md-4") {
                                                label(classes = "form-custom") { +"Alcaldía / Municipio" }
                                                input(classes = "custom-info-form", type = InputType.text) {
                                                    style = "width:85%;"
                                                    required = true
                                                    placeholder = " "
                                                }
                                            }

                                            div(classes = "col-md-4") {
                                                label(classes = "form-custom") { +"Colonia" }
                                                input(classes = "custom-info-form", type = InputType.text) {
                                                    style = "width:85%;"
                                                    required = true
                                                    placeholder = " "
                                                }
                                            }
                                        }

                                        div(classes = "row") {
                                            style = "margin-bottom:15px; font-size:1rem;"
                                            div(classes = "col-md-4") {
                                                label(classes = "form-custom") { +"Ciudad" }
                                                input(classes = "custom-info-form", type = InputType.text) {
                                                    style = "width:85%;"
                                                    required = true
                                                    placeholder = " "
                                                }
                                            }

                                            div(classes = "col-md-4") {
                                                label(classes = "form-custom") { +"Antiguedad" }
                                                br
                                                label { +"Años:" }
                                                select(classes = "info-form-select-short") {
                                                    style = "width:25%"
                                                    name = "year"
                                                    id = "year"
                                                }

                                                label { +"Meses:" }
                                                select(classes = "info-form-select-short") {
                                                    style = "width:30%"
                                                    name = "month"
                                                    id = "month"
                                                }
                                                select {
                                                    style = "display:none"
                                                    name = "day"
                                                    id = "day"
                                                }
                                            }
                                            div(classes = "col-md-4") {
                                                label(classes = "form-custom") { +"Giro" }
                                                input(classes = "custom-info-form", type = InputType.text) {
                                                    style = "width:100%;"
                                                    required = true
                                                    placeholder = " "
                                                }
                                            }
                                        }

                                        div(classes = "row") {
                                            style = "margin-bottom:15px; font-size:1rem;"
                                            div(classes = "col-md-4") {
                                                label(classes = "form-custom") { +"Ocupación" }
                                                input(classes = "custom-info-form", type = InputType.text) {
                                                    style = "width:85%;"
                                                    required = true
                                                    placeholder = " "
                                                }
                                            }

                                            div(classes = "col-md-4") {
                                                label(classes = "form-custom") { +"Ingreso Mensual Bruto" }
                                                input(classes = "custom-info-form", type = InputType.text) {
                                                    style = "width:85%;"
                                                    required = true
                                                    placeholder = " "
                                                }
                                            }
                                            div(classes = "col-md-4") {
                                                label(classes = "form-custom") { +"Telefono de la Empresa" }
                                                input(classes = "custom-info-form", type = InputType.text) {
                                                    style = "width:100%;"
                                                    required = true
                                                    placeholder = " "
                                                }
                                            }
                                        }

                                        *//*div(classes = "row") {
                                        style = "margin-bottom:0px; font-size:16px;"
                                        div(classes = "col-md-12") {
                                            input(classes = "custom-info-form-0", type = InputType.checkBox) {
                                                style = " display:inline-block;"
                                            }
                                            p {
                                                style = "color:#ffb700; font-size:14px; display:inline-block;"
                                                +"La direccion de entrega del telefono movil es la misma que la del domicilio"
                                            }
                                        }
                                    }

                                    div(classes = "row") {
                                        style = "margin-bottom:15px; font-size:16px;"
                                        div(classes = "col-md-12") {
                                            input(classes = "custom-info-form-0", type = InputType.checkBox) {
                                                style = " display:inline-block;"
                                            }
                                            p {
                                                style = "color:#ffb700; font-size:14px; display:inline-block;"
                                                +"La direccion de entrega del telefono movil es la misma que la del trabajo"
                                            }
                                        }
                                    }*//*

                                }
                            }

                            div {
                                style = "text-align:left; display:inline-block;"
                                a {
                                    style = "width:45px; height:45px;"
                                    onClick = "window.history.go(-1); return false;"

                                    img {
                                        src = "/assets/media/back.png"
                                        width = "60px"
                                        height = "60px"
                                    }
                                }
                            }

                            div {
                                style = "float:right; display:inline-block;"
                                a {
                                    style = "width:45px; height:45px;"
                                    href = "/mobileData".withBaseUrl()
                                    img {
                                        src = "/assets/media/forward.png"
                                        width = "60px"
                                        height = "60px"
                                    }
                                }
                            }

                        }

                    }
                }

                jscript(src = "assets/js/info-page.js")
            }
        }
    }*/

}