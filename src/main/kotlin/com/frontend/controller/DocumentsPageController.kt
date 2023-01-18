package com.frontend.controller

import com.Env
import com.data.Estado
import com.helper.withBaseUrl
import com.model.DocumentModel
import com.model.UserModel
import com.plugins.UserSession
import com.plugins.userSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.html.*
import kotlinx.html.*

class DocumentsPageController(call: ApplicationCall) : TemplateController(call) {

    private fun HTML.mainHeaderFile() {
        val type = call.parameters["type"]?.takeIf { it != "null" } ?: ""
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

            css("assets/css/document-mobile.css")

            css("https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.2/css/all.min.css")
            css("https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css")
            jscript(src = "https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js")

            script {
                unsafe {
                    +"""const mGBaseUrl = '${Env.BASE_URL}';""".trimMargin()
                    +"""const mAuthToken = '${call.principal<UserSession>()?.authToken ?: ""}';""".trimMargin()
                    +"""const mType = '$type';""".trimMargin()
                    +"""const mIsHandoff = ${call.userSession.isHanddOff};""".trimMargin()
                    +"""const mIsReturning = ${call.userSession.isReturning};""".trimMargin()
                }
            }

            jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js")
            jscript(src = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js")
            jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.2/jquery.form.min.js")
            jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.19.1/jquery.validate.min.js")
            jscript(src = "https://cdn.jsdelivr.net/npm/davidshimjs-qrcodejs@0.0.2/qrcode.min.js")

            jscript(src = "assets/js/scripts.bundle.min.js")
        }
    }

    suspend fun identificationPage() {
        val userSession = call.userSession
        val user = UserModel.getUser(userSession.mobile) ?: error("Invalid Session")

        if (user.status != UserModel.NEW_USER) {
            if (pageRedirect()) return
        }

        var ine = false
        var passport = false
        var address = false

        val documentData = DocumentModel.getDocumentByUserId(userSession.userId)
        if (documentData != null) {
            if (documentData.ineFront != null) ine = true
            if (documentData.passport != null) passport = true
            if (documentData.proofOfAddress != null) address = true
        }

        call.respondHtml(HttpStatusCode.OK) {
            val type = call.parameters["type"]?.takeIf { it != "null" } ?: ""
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

                css("assets/css/identification-style.css")
                css("assets/css/document-mobile.css")

                css("https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css")
                jscript(src = "https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js")

                script {
                    unsafe {
                        +"""const mGBaseUrl = '${Env.BASE_URL}';""".trimMargin()
                        +"""const mAuthToken = '${call.principal<UserSession>()?.authToken ?: ""}';""".trimMargin()
                        +"""const mType = '$type';""".trimMargin()
                        +"""const mIsHandoff = ${call.userSession.isHanddOff};""".trimMargin()
                        +"""const mIsReturning = ${call.userSession.isReturning};""".trimMargin()
                    }
                }

                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js")
                jscript(src = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js")
                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.2/jquery.form.min.js")

                jscript(src = "assets/js/scripts.bundle.min.js")
            }

            body {
                div("main-container") {
                    header(classes = "header") {
                        span("karum-logo2") {
                            img {
                                src = "/assets/media/instant-logo.png"
                                height = "65px"
                            }
                        }
                        h1(classes = "splash-title") {
                            +"Centro de carga de documentos"
                        }
                    }

                    div(classes = "container") {
                        style = "max-width:600px; margin-top:60px;"
                        div {
                            h4 {
                                style = "color:#ff6700; text-align:center; font-size:1.125rem; margin-top:30px;"
                                /*+"Haga clic en la casilla junto al documento que desea cargar"*/
                                +"Da clic en la casilla junto al documento que desea cargar"
                            }
                        }

                        div {
                            style = "display:flex; justify-content:center; margin-top:50px;"
                            form {
                                table {
                                    tr {
                                        td {
                                            h4(classes = "document-table-heading") {
                                                +"Tipo de documento"
                                            }
                                        }
                                        td {
                                            style = "padding-left:60px;"
                                            h4(classes = "document-table-heading") {
                                                +"Cargar"
                                            }
                                        }
                                    }


                                    tr {
                                        td {
                                            p(classes = "document-table-paragraph") {
                                                +"INE / IFE"
                                            }
                                        }
                                        documentUploadCheckBox(
                                            "ineCheckBoxId",
                                            ine,
                                            passport
                                        )
                                    }

                                    tr {
                                        td {
                                            p(classes = "document-table-paragraph") {
                                                +"Pasaporte"
                                            }
                                        }
                                        documentUploadCheckBox(
                                            "passportCheckBoxId",
                                            passport,
                                            ine
                                        )
                                    }

                                }
                            }
                        }

                        div(classes = "row") {
                            style = "margin-top:20px;"
                            div(classes = "col-md-10") {
                                div(classes = "col-md-12") {
                                    style = "display:flex; justify-content:center; margin-top:20px;"
                                    p(classes = "container-title") {
                                        id = "identificationErrorMsgId"
                                        style = "width:72%; color:red; text-align:center;margin-left:93px;display:none"
                                        +"Debe proporcionar su INE o Pasaporte, MAS Comprobante de Domicilio para continuar."
                                    }
                                }

                            }
                            div(classes = "col-md-2") {
                                style = "padding-top:15px; padding-left:10px; display:none;"
                                a {
                                    onClick = "onClickForwardBtn(this)"
                                    id = "forward-btn"
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

                /*input {
                    id = "backProtected"
                    value = "no"
                    type = InputType.text
                    style = "display:none;"
                }*/

                jscript(src = "assets/js/main.js")
                jscript(src = "assets/js/login-general.js")
                jscript(src = "assets/js/identification.js")
            }
        }
    }

    private fun TR.documentUploadCheckBox(elId: String, uploaded: Boolean, disable: Boolean = false) {
        td {
            style = "padding-left:60px; position:relative;"
            val extraCss = if (uploaded) "visibility: hidden; display: inline-block;" else "";
            input(
                classes = "form-check-input custom-checkbox",
                type = InputType.checkBox
            ) {
                style = "margin: auto; $extraCss"
                id = elId
                name = "ine"
                value = ""
                if (uploaded) {
                    checked = true
                    disabled = true
                } else if (disable) {
                    disabled = true
                }
            }
            span("checkmark")
            if (uploaded) {
                span("fa fa-check") {
                    style = "margin:auto; color:green;"
                }
            }
        }
    }

    suspend fun uploadDocumentPage() {
        // Clear summary data from previous session

        val userSession = call.userSession
        if (userSession.isHanddOff.not()) {
            // Update HandOff Flag at the start of document page
            UserModel.updateMobileHandOffFlag(false, userSession.userId)
        }
        val user = UserModel.getUser(userSession.mobile) ?: error("Invalid session")
        /*if (user.status >= 2) {
            if (pageRedirect()) return
        }*/

        val type = call.parameters["type"]
        val containerTitle = when (type) {
            "a" -> "Recibo de nómina"
            "i" -> "Comprobante de Ingresos"
            else -> {
//                call.sessions.clear("summary")
                "Identificación"
            }
        }

        call.respondHtml(HttpStatusCode.OK) {
            mainHeaderFile()

            body {
                // Include this file to make upload document Page mobile responsive
                css("assets/css/app.css")
                div("main-container") {
                    header(classes = "header") {
                        span("karum-logo") {
                            img {
                                src = "/assets/media/instant-logo.png"
                                height = "65px"
                            }
                        }
                        h1(classes = "splash-title-left") {
                            style = "text-align:left;"
                            +"APPROVA"
                        }
                    }

                    div(classes = "container centro-container") {
                        style = "position:relative;"
                        div {
                            h3(classes = "load_center_text") {
                                +"CENTRO DE CARGA"
                            }

                            span(classes = "barcode_img") {
                                style = "position:absolute; right:12px; top:12px;"
                                a {
                                    style = "cursor:pointer;"
                                    onClick = "onClickGenerateQRCode()"
                                    img {
                                        src = "/assets/media/qr-code.gif"
                                        width = "55px"
                                        height = "55px"
                                    }
                                }
                            }

                            span(classes = "barcode_i") {
                                style = "position:absolute; right:30px; top:90px;"
                                a {
                                    style = "cursor:pointer;"
                                    onClick = "onClickQRCodeInfo()"
                                    img {
                                        src = "/assets/media/info-icon.png"
                                        width = "25px"
                                        height = "25px"
                                    }
                                }
                            }

                            h4 {
                                style =
                                    "color:#ff6700; text-align:center; margin-bottom:25px;text-decoration: underline;"
                                +containerTitle
                            }

                            val subTitleVisibility = if (type.isNullOrBlank().not() && type != "p") {
                                "hidden"
                            } else {
                                "visible"
                            }
                            h5 {
                                style =
                                    "color: #ff6700; text-align: center; margin-bottom:60px;visibility: $subTitleVisibility;"
                                val subTitle = when (type) {
                                    "a" -> "Comprobante de domicilio (opcional)"
                                    "p" -> "Pasaporte"
                                    "i" -> "Comprobante de Ingresos"
                                    else -> "IFE/INE"
                                }
                                /* val subTitle = if (type == "p") {
                                     "Pasaporte"
                                 } else {
                                     "IFE/INE"
                                 }*/
                                +subTitle
                            }

                            h5("upload_text") { +"Favor de subir tu identificación" }
                        }

                        a(classes = "upload-btn-link cameraBtnLink") {
                            href = "/upload/document/instructions?type=$type".withBaseUrl()
                            div("upload-btn") {
                                span {
                                    style = "margin-left:12px;"
                                    img(classes = "camera_icon") {
                                        src = "/assets/media/camera.png"
                                    }
                                }
                                span("upload-btn-text") { +"Tomar una foto" }
                                span {
                                    style = "float:right; padding:8px 15px;"
                                    i(classes = "fas fa-chevron-right") {
                                        style = "color:red; font-size:2rem;"
                                    }
                                }
                            }
                        }

                        a(classes = "upload-btn-link") {
                            href = "/upload/document?type=$type".withBaseUrl()
                            div("upload-btn") {
                                span {
                                    style = "margin-left:12px;"
                                    img(classes = "upload_icon") {
                                        src = "/assets/media/upload.png"
                                    }
                                }
                                span("upload-btn-text") { +"Cargar Foto" }
                                span("upload-chevron") {
                                    style = "float:right; padding:8px 15px;"
                                    i(classes = "fas fa-chevron-right") {
                                        style = "color:red; font-size:2rem;"
                                    }
                                }
                            }
                        }

                        div(classes = "row back_btn") {
                            div(classes = "col-md-2") {
                                style = "margin-bottom:12px;"
                                a {
                                    href = if (user.status >= UserModel.TC44_API_COMPLETE) {
                                        "/document".withBaseUrl()
                                    } else {
                                        "/identification".withBaseUrl()
                                    }
                                    img {
                                        src = "/assets/media/back.png"
                                        width = "60px"
                                        height = "60px"
                                    }
                                }
                            }
                            div(classes = "col-md-8")
                            if (type == "a") {
                                div(classes = "col-md-2") {
                                    style = "text-align:right; cursor:pointer;"
                                    a {
                                        onClick = "userStatusToPoa()"
//                                        href = "/person_info".withBaseUrl()
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

                div(classes = "modal qrModal") {
                    id = "modal-qrCode"
                    role = "dialog"

                    div(classes = "modal-dialog") {
                        style = "border:0; transform: translate(0%,50%); border:none;"
                        div("modal-content") {
                            style = "background:transparent; border:none"
                            div("modal-body") {
                                div {
                                    style = "width:80%; margin:auto;"
                                    div {
                                        style = "width:60%; margin:auto; display:flex;"
                                        id = "qrcode"
                                    }

                                    h5 {
                                        style =
                                            "color:#000; text-align:center; margin-top:16px; font-size: 1rem; padding: 0px 40px;"
                                        +"Escanea el código QR con la cámara de tu móvil para tomar el control"
                                    }

                                    a {
                                        onClick = "onClickDismissQR()"
                                        style = "z-index = 1; float:left; cursor:pointer;"

                                        img {
                                            src = "/assets/media/back.png"
                                            width = "60px"
                                            height = "60px"
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                div(classes = "modal fade") {
                    id = "modal-scan-qr"
                    role = "dialog"

                    div(classes = "modal-dialog") {
                        style = "transform: translate(0%,50%);"
                        div("modal-content") {
                            style = "background:transparent; border:none"
                            div("modal-body") {
                                div {
                                    style = "margin-top:350px; display: flex; justify-content: center;"

                                    button(classes = "btn btn-lg btn-primary") {
                                        onClick = "window.location='${"/identification".withBaseUrl()}';"
                                        style = "padding-left:42px; padding-right:42px;"
                                        +"Control de devolución"
                                    }
                                }
                            }
                        }
                    }
                }

                div(classes = "modal fade") {
                    id = "modal-info-qr"
                    role = "dialog"

                    div(classes = "modal-dialog") {
                        style = "transform: translate(0%,50%);"
                        div("modal-content") {
                            style = "background:transparent; border:2px solid red; border-radius:15px; padding:6px 4px;"
                            div("modal-body") {
                                style = "color:#ffb700; font-style:italic;"
                                p {
                                    +"""Haga clic o toque el código QR y luego escanee el código QR con la cámara de su 
                                        teléfono móvil para transferir el control a su teléfono móvil.""".trimMargin()
                                }

                                br
                                p {
                                    +"""Desde aquí puede fotografiar documentos o cargar documentos almacenados en su 
                                        teléfono móvil.""".trimMargin()
                                }

                                br
                                p {
                                    +"""Una vez que se complete la carga, se devolverá el control a su computadora de 
                                        escritorio para continuar con el proceso de solicitud.""".trimMargin()
                                }

                                a {
                                    onClick = "onClickDismissQRInfo()"
                                    style = "z-index = 1; float:left; cursor:pointer;"

                                    img {
                                        src = "/assets/media/back.png"
                                        width = "60px"
                                        height = "60px"
                                    }
                                }
                            }
                        }
                    }
                }

                jscript(src = "assets/js/main.js")
                jscript(src = "assets/js/login-general.js")
                jscript(src = "assets/js/handOff-document.js")
            }
        }
    }

    suspend fun goodByeMobilePage() {
        val userSession = call.userSession
        UserModel.updateMobileHandOffFlag(false, userSession.userId)
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
                div("goodByeMobile")
            }
        }
    }

    suspend fun dragAndDropUploadPage() {
        val uploadType = call.parameters["type"]?.takeIf { it != "null" } ?: ""
        val userSession = call.userSession

        /*   val userSession = call.userSession
             val user = UserModel.getUser(userSession.mobile)
            val curpData = if (user?.ine != null) {
                 kotlin.runCatching { CurpUtil.curpAPi.byCurp(user.curp)?.respuestaRENAPO?.curpStatus?.resultCURPS }
                     .onFailure { it.printStackTrace() }
                     .getOrNull()
             } else null*/

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
                css("assets/css/upload-style.css")
                css("assets/css/app.css")
                css("assets/css/document-mobile.css")
                css(href = "https://use.fontawesome.com/releases/v5.15.4/css/all.css")
                css("https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css")

                jscript(src = "https://kit.fontawesome.com/a076d05399.js")

                jscript(src = "https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js")

                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js")
                jscript(src = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js")
                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.2/jquery.form.min.js")
                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.19.1/jquery.validate.min.js")


                script {
                    unsafe {
                        +"""const mGBaseUrl = '${Env.BASE_URL}';""".trimMargin()
                        +"""const mType = '$uploadType';""".trimMargin()
                        +"""const mIsHandoff = ${call.userSession.isHanddOff};""".trimMargin()
                        +"""const mIsReturning = ${call.userSession.isReturning};""".trimMargin()
                    }
                }
            }

            body {
                div("main-container") {
                    header(classes = "info-header") {
                        span("karum-logo2") {
                            img {
                                src = "/assets/media/instant-logo.png"
                                height = "65px"
                            }
                        }
                        h1(classes = "splash-title-left") {
                            style = "text-align:left; padding:0;"
                            +"APPROVA"
                        }
                    }

                    div(classes = "container") {
                        id = "containerForSummary"
//                        style = "position:relative;"
                        div {
                            style = "margin:0 auto; text-align:center;"
                            h3(classes = "container-main-heading") {
                                +"CENTRO DE CARGA"
                            }

                            if (uploadType == "i") {
                                h4 {
                                    style =
                                        "color:#ffb700; text-align:center; margin-bottom:25px;text-decoration: underline;"
                                    +"Identificación"
                                }
                            }

                            p(classes = "identification_p_tage") {
                                id = "identificationText"
                                a(classes = "container-sub-heading identificacion_heading") {
                                    style = "color:#ff6700; font-weight:bold;"
                                    val title = when (uploadType) {
                                        "a" -> "Comprobante de domicilio (opcional)"
                                        "p" -> "Pasaporte"
                                        "i" -> "Comprobante de Ingresos"
                                        else -> "Identificación"
                                    }
                                    +title
                                }
                            }

                            if (uploadType.isBlank()) {
                                p {
                                    style = "text-align:center; margin-top:32px; margin-bottom:24px"
                                    id = "ineHeadText"
                                    a(classes = "container-title") {
                                        style = "color:#ff6700; font-weight:bold;"
                                        +"IFE/INE"
                                    }
                                }
                            }
                        }

//                        Document Summary Row
                        div(classes = "row") {
                            id = "summary-container"
                            style = "width:85%; margin:auto; display: none;"
                            div("col-md-12") {
                                id = "summary-images-preview"
                                style =
                                    "margin-top:50px; display:flex; flex-direction:column; align-items:center; position:relative;"

                                div("error-cross") {
                                    id = "crossAreaId"
                                    img {
                                        src = "/assets/media/cross.png"
                                    }
                                }
                            }
                            ineSideSummaryData()

                        }

                        div(classes = "row") {
                            id = "uploadContainer"
                            style = "display: flex; justify-content: center; align-items: center; flex-direction: row;"

                            div(classes = "col-md-2 frenteTextBlock") {
                                if (uploadType.isBlank()) {
                                    h6 {
                                        style = "font-size:1.5rem; font-weight:bold; color:white; margin-top:20px"
                                        +"Frente"
                                    }
                                }
                            }
                            div(classes = "col-md-2 uploadBlock") {
                                id = "uploadTextBlock"
                                if (uploadType.isBlank()) {
                                    h6 {
                                        style = "font-weight:bold; font-size: 0.8rem;"
                                        id = "documentTitle"
                                        +"Subir parte frontal del INE / IFE"
                                    }

                                    h6 {
                                        style = "font-weight:bold; color:#000;"
                                        id = "pageIndicator"
                                        +"Paso 1 de 2"
                                    }
                                }
                            }
                            div(classes = "col-md-6") {
                                div(classes = "progress-area") {
                                    id = "progressAreaId"
                                    style = "display: none;"
                                }

                                div {
                                    id = "images-preview"
                                    style =
                                        "display:flex; justify-content:space-around; margin-top:60px; align-items:center;"
                                }

                                documentPassportInput()

                                div(classes = "drag-area") {
                                    id = "dragAreaId"
                                    onClick = "onClickUploadImg()"
                                    if (uploadType == "a" || uploadType == "i") {
                                        style = "height:360px;"
                                    }
                                    /*div(classes = "icon") {
                                        i(classes = "fas fa-cloud-upload-alt") { }
                                    }*/
                                    img {
                                        id = "imagePlaceholder"
                                        height = "280px"
                                        width = "450px"
                                        val sampleImage = when (uploadType) {
                                            "p" -> "/assets/media/passport.png"
                                            "a" -> "/assets/media/address.png"
                                            "i" -> "/assets/media/poi-sample.png"
                                            else -> "/assets/media/curp_front.png"
                                        }
                                        src = sampleImage
                                    }
                                    input(type = InputType.file) {
                                        name = "front"
                                        hidden = true
                                    }
                                }

                                div(classes = "continueBtnBlock") {
                                    style =
                                        "display: flex; justify-content: center; align-items: center; margin-top:30px;"
                                    a(classes = "btn btn-primary splash_iniciar_btn") {
                                        id = "continue_btn"
                                        onClick = "onDocumentSelectContinue(this)"
                                        style = "width:50%; margin:auto;visibility: hidden;"
                                        +"Continuar"
                                    }
                                }
                            }
                            div(classes = "col-md-2 uploadMobileBlock") {
                                id = "uploadTextBlockOnMobile"
                                if (uploadType.isBlank()) {
                                    h6 {
                                        style = "font-weight:bold; color:white"
                                        id = "pageIndicatorOnMobile"
                                        +"Paso 1 de 2"
                                    }
                                }
                            }
                            div(classes = "col-md-2 ") {
                                div(classes = "uploadBtnDesktop") {
                                    id = "uploadBlockBtn"
                                    onClick = "onClickUploadBlockBtn()"
                                    img {
                                        src = "/assets/media/upload.png"
                                        height = "80px"
                                    }

                                    h6 {
                                        style = "margin-top:16px; font-weight:bold;"
                                        +"Da clic aquí para ir al centro de carga"
                                    }
                                }
                                div(classes = "uploadBtnMobile") {
                                    h6(classes = "identification_p_tage") {
                                        style = "color:#ff6700; font-weight:bold; margin-top:30px; font-size:1.5rem;"
                                        +"Procesamiento completo"
                                    }
                                }
                            }
                        }

                        div(classes = "row forwardBackBtnRow") {
                            div(classes = "col-md-2 backBtnDesktop") {
                                id = "back-btn"
                                a(href = "#") {
                                    onClick = "window.location = '/uploadDocument?type=$uploadType'".withBaseUrl()
                                    img {
                                        src = "/assets/media/back.png"
                                        width = "60px"
                                        height = "60px"
                                    }
                                }
                            }
                            div(classes = "col-md-8") {
                                id = "bottom-message"
                            }
                            div(classes = "col-md-2") {
                                id = "forward-btn"
                                style =
                                    "text-align:right; visibility:${if (uploadType == "a") "visible" else "hidden"}; cursor:pointer;"
                                a {
                                    onClick = "onClickSubmit()"
                                    /*href =
                                     if (type == "a") "/person_info".withBaseUrl() else "/upload/document?type=a".withBaseUrl()*/
                                    img {
                                        src = "/assets/media/forward.png"
                                        width = "60px"
                                        height = "60px"
                                    }
                                }
                            }
                            if (uploadType != "a") {
                                div(classes = "uploadBtnMobile") {
                                    div("col-md-2") {
                                        id = "forward-btn"
                                        style = "visibility:visible; float:right; text-align:right;"
                                        a {
                                            style = "margin-top:20px; text-align:right;"
                                            id = "uploadBlockBtn"
                                            onClick = "onClickUploadBlockBtn()"
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

                }

                input {
                    id = "backProtected"
                    value = "no"
                    type = InputType.text
                    style = "display:none;"
                }

                jscript(src = "assets/js/main.js")
                jscript(src = "assets/js/document-upload-common.js")
                jscript(src = "assets/js/drag_upload.js")
            }
        }
    }

    private fun DIV.ineSideSummaryData() {
        div("col-md-6") {
            id = "summary-data"
            style = "margin-top:50px; text-align:left; display:none;"
            form(method = FormMethod.post) {
                id = "summary_data_form"
                table("summaryTable") {
                    style = "width:100%;"
                    tr {
                        td {
                            label(classes = "form-custom") { +"APELLIDO(S) PATERNO(S) : " }
                        }
                        td {
                            input(
                                classes = "custom-summary-form yellowInput",
                                type = InputType.text,
                                name = "parent_surname"
                            ) {
                                style = "width:53%;"
                                required = true
                                id = "apellidoPaternoId"
                                placeholder = " "
                                maxLength = "20"
                                autoComplete = false
                            }
                        }
                    }

                    tr {
                        td {
                            label(classes = "form-custom") { +"APELLIDO(S) MATERNO(S) : " }
                        }
                        td {
                            input(
                                classes = "custom-summary-form yellowInput",
                                type = InputType.text,
                                name = "mother_surname"
                            ) {
                                style = "width:53%;"
                                id = "apellidoMaternoId"
                                placeholder = " "
                                maxLength = "20"
                                autoComplete = false
                            }
                        }
                    }

                    tr {
                        td {
                            label(classes = "form-custom") { +"NOMBRE(S) : " }
                        }
                        td {
                            input(
                                classes = "custom-summary-form yellowInput",
                                type = InputType.text,
                                name = "name"
                            ) {
                                style = "width:53%;"
                                id = "nombreId"
                                placeholder = " "
                                maxLength = "20"
                                autoComplete = false
                            }
                        }
                    }

                    tr {
                        td {
                            label(classes = "form-custom") { +"GENERO : " }
                        }
                        td {
                            select(classes = "summary-form-select genderInputSelect yellowText") {
                                style = "width:20%; color:white;"
                                id = "gender"
                                name = "gender"
                                option {
                                    value = "H"
                                    +"Hombre"
                                }
                                option {
                                    value = "M"
                                    +"Mujer"
                                }
                            }
                        }
                    }
                    tr {
                        td {
                            label(classes = "form-custom") { +"ESTADO DE NACIMIENTO : " }
                        }
                        td {
                            select(classes = "summary-form-select yellowText") {
                                required = true
                                style = "width:auto%; color:white;"
                                id = "stateOfBirth"
                                name = "stateOfBirth"
                                option {
                                    value = ""
                                    +"SELECCIONE"
                                }
                                Estado().getStateList().forEach {
                                    option {
                                        value = it.codeTC44
                                        +it.title
                                    }
                                }
                            }
                        }
                    }

                    tr {
                        td {
                            label(classes = "whiteLabel") { +"FECHA DE NACIMENTO : " }
                        }
                        td {
                            dateInput(
                                classes = "custom-summary-form yellowInput",
                                name = "date_of_birth"
                            ) {
                                style = "width:45%;"
                                id = "dateOfBirthId"
                                placeholder = " "
                            }
                        }
                    }

                    tr {
                        td {
                            label(classes = "whiteLabel") { +"DIRECCION LINEA 1 : " }
                        }
                        td {
                            input(
                                classes = "custom-summary-form yellowInput",
                                type = InputType.text,
                                name = "address_one"
                            ) {
                                style = "width:60%;"
                                id = "address1"
                                name = "address_one"
                                placeholder = " "
                                autoComplete = false
                            }
                        }
                    }

                    tr {
                        td {
                            label(classes = "whiteLabel") { +"DIRECCION LINEA 2 : " }
                        }
                        td {
                            input(
                                classes = "custom-summary-form yellowInput",
                                type = InputType.text,
                                name = "address_two"
                            ) {
                                style = "width:60%;"
                                id = "address2"
                                placeholder = " "
                                autoComplete = false
                            }
                        }
                    }

                    tr {
                        td {
                            label(classes = "whiteLabel") { +"DIRECCION LINEA 3 : " }
                        }
                        td {
                            input(
                                classes = "custom-summary-form yellowInput",
                                type = InputType.text,
                                name = "address_three"
                            ) {
                                style = "width:60%;"
                                id = "address3"
                                placeholder = " "
                                autoComplete = false
                            }
                        }
                    }

                    tr {
                        td {
                            label(classes = "whiteLabel") { +"CURP : " }
                        }
                        td {
                            input(
                                classes = "custom-summary-form yellowInput",
                                type = InputType.text,
                                name = "curp_id"
                            ) {
                                style = "width:55%;"
                                id = "generatedCurpId"
                                placeholder = " "
                                maxLength = "18"
                                autoComplete = false
                            }
                        }
                    }

                    tr {
                        td {
                            label(classes = "whiteLabel") { +"ANO DE REGISTRO : " }
                        }
                        td {
                            input(
                                classes = "custom-summary-form yellowInput",
                                type = InputType.text,
                                name = "register_year"
                            ) {
                                style = "width:16%;"
                                id = "registerYear"
                                placeholder = " "
                                maxLength = "4"
                                autoComplete = false
                            }
                        }
                    }

                    tr {
                        td {
                            label(classes = "whiteLabel") { +"NUMERO DE EMISION : " }
                        }
                        td {
                            input(
                                classes = "custom-summary-form yellowInput",
                                type = InputType.text,
                                name = "issu_number"
                            ) {
                                style = "width:16%;"
                                id = ""
                                placeholder = " "
                                maxLength = "4"
                                autoComplete = false
                            }
                        }
                    }

                    tr {
                        td {
                            label(classes = "whiteLabel") { +"CLAVE DE ELECTOR : " }
                        }
                        td {
                            input(
                                classes = "custom-summary-form yellowInput",
                                type = InputType.text,
                                name = "summary_ine"
                            ) {
                                style = "width:50%;"
                                id = "summaryIne"
                                placeholder = " "
                                maxLength = "18"
                                autoComplete = false
                            }
                        }
                    }

                    tr {
                        td {
                            label(classes = "whiteLabel") { +"ANO DE EMISION : " }
                        }
                        td {
                            input(
                                classes = "custom-summary-form yellowInput",
                                type = InputType.text,
                                name = "year_of_issue"
                            ) {
                                style = "width:16%;"
                                id = ""
                                placeholder = " "
                                maxLength = "4"
                                autoComplete = false
                            }
                        }
                    }

                    tr {
                        td {
                            label(classes = "whiteLabel") { +"ANO DE VENCIMIENTO : " }
                        }
                        td {
                            input(
                                classes = "custom-summary-form yellowInput",
                                type = InputType.text,
                                name = "year_of_expiry"
                            ) {
                                style = "width:16%;"
                                id = "yearOfExpire"
                                placeholder = " "
                                maxLength = "4"
                                autoComplete = false
                            }
                        }
                    }

                    tr {
                        td {
                            label(classes = "whiteLabel") { +"OCR : " }
                        }
                        td {
                            input(
                                classes = "custom-summary-form yellowInput",
                                type = InputType.text,
                                name = "ocr"
                            ) {
                                style = "width:50%;"
                                id = "ocrInput"
                                placeholder = " "
                                maxLength = "15"
                                autoComplete = false
                            }
                        }
                    }

                    tr {
                        td {
                            label(classes = "whiteLabel") { +"CIC : " }
                        }
                        td {
                            input(
                                classes = "custom-summary-form yellowInput",
                                type = InputType.text,
                                name = "cic"
                            ) {
                                style = "width:45%;"
                                id = "cicInput"
                                placeholder = " "
                                maxLength = "10"
                                autoComplete = false
                            }
                        }
                    }

                }
            }
        }
    }

    suspend fun documentUploadInstructionsPage() {
        val type = call.parameters["type"]
        call.respondHtml(HttpStatusCode.OK) {
            mainHeaderFile()

            body {
                section {
                    div("main-container") {
                        header(classes = "header") {
                            span("karum-logo") {
                                img {
                                    src = "/assets/media/instant-logo.png"
                                    height = "65px"
                                }
                            }
                            h1(classes = "splash-title-left") {
                                style = "text-align:left;"
                                +"APPROVA"
                            }
                        }

                        div(classes = "container") {
                            style = "position:relative;max-width: 1200px;"
                            div {
                                h4(classes = "container-main-heading") {
                                    style = "color:#000; text-align:center;width:60%; margin:20px auto;"
                                    +"CENTRO DE CARGA"
                                }

                                /*span(classes = "barcode_img") {
                                    style = "position:absolute; right:10px; top:10px;"
                                    img {
                                        src = "/assets/media/qr-code.gif"
                                        width = "60px"
                                        height = "60px"
                                    }
                                }*/

                                h4(classes = "container-sub-heading identificacion_heading") {
                                    style =
                                        "color:#ff6700; text-align:center; margin-bottom:25px;text-decoration: underline;"
                                    val title = when (type) {
                                        "a" -> "Comprobante de domicilio (opcional)"
                                        "p" -> "Pasaporte"
                                        "i" -> "Comprobante de Ingresos"
                                        else -> "Identificación"
                                    }
                                    +title
                                }


                                if (type.isNullOrBlank()) {
                                    h5(classes = "container-title") {
                                        style = "color: #ff6700; text-align: center; margin-bottom:40px;"
                                        +"IFE/INE"
                                    }
                                }

                                p(classes = "prepare_img_text") {
                                    +"Preparese para la fotografia del anverso de su terjeta INE"
                                }

                                h5(classes = "container-sub-heading instrucciones_heading") {
                                    style = "color: #ff6700; text-align: center; font-weight:400;"
                                    +"Instrucciones"
                                }
                            }

                            div {
                                style = "display: flex;"
                                div(classes = "inner-container") {
                                    style = "background: #F5F6F8; padding: 20px 35px; width: auto;"
                                    p {
                                        style = "font-size: 1.5em;"
                                        img {
                                            src = "/assets/media/tick.png"
                                            height = "35px"
                                        }
                                        +"Mostrar todo el documento"
                                    }
                                    p {
                                        style = "font-size: 1.5em;"
                                        img {
                                            src = "/assets/media/tick.png"
                                            height = "35px"
                                        }
                                        +"No dobles el documento"
                                    }
                                    p {
                                        style = "font-size: 1.5em;"
                                        img {
                                            src = "/assets/media/tick.png"
                                            height = "35px"
                                        }
                                        +"Evite el deslumbramiento"
                                    }
                                    p(classes = "last_instruction_text") {
                                        style = "font-size: 1.5em;"
                                        img {
                                            src = "/assets/media/tick.png"
                                            height = "35px"
                                        }
                                        +"Ninguna foto de otra imagen o dispositivo"
                                    }
                                }
                            }

                            div(classes = "Captura_upload_btn") {
                                a(classes = "btn btn-primary splash_iniciar_btn") {
                                    href = "/capture?type=$type".withBaseUrl()
                                    style = "width:80%; border: solid;"
                                    +"Captura"
                                }
                            }

                            div(classes = "row") {
                                div(classes = "col-md-8") {
                                    style = "margin-bottom:20px; padding-left:15px;"
                                    a {
                                        href = "/uploadDocument?type=$type".withBaseUrl()
//                                        onClick = "window.history.go(-1); return false;"
                                        img {
                                            src = "/assets/media/back.png"
                                            width = "60px"
                                            height = "60px"
                                        }
                                    }
                                }
                                div(classes = "col-md-4 forward_btn_block") {
                                    style = "text-align:right;"
                                    style = "margin-bottom:20px; padding-right:15px;"
                                    a {
                                        style = "float: right; margin-top:5px; margin-right:10px;"
                                        href = "/capture?type=$type".withBaseUrl()
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

                jscript(src = "assets/js/main.js")
                jscript(src = "assets/js/login-general.js")
            }
        }
    }

    suspend fun captureImageWithCameraPage() {
        val userSession = call.userSession
        val uploadType = call.parameters["type"]?.takeIf { it != "null" } ?: ""
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
                css("assets/css/upload-style.css")
                css("assets/css/document-mobile.css")
                css("assets/css/app.css")
                css(href = "https://use.fontawesome.com/releases/v5.15.4/css/all.css")
                css("https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css")

                jscript(src = "https://kit.fontawesome.com/a076d05399.js")

                jscript(src = "https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js")

                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js")
                jscript(src = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js")
                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.2/jquery.form.min.js")
                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.19.1/jquery.validate.min.js")

                script {
                    unsafe {
                        +"""const mGBaseUrl = '${Env.BASE_URL}';""".trimMargin()
                        +"""const mType = '$uploadType';""".trimMargin()
                        +"""const mIsHandoff = ${call.userSession.isHanddOff};""".trimMargin()
                        +"""const mIsReturning = ${call.userSession.isReturning};""".trimMargin()
                    }
                }
            }

            body {
                div("main-container") {
                    header(classes = "info-header") {
                        span("karum-logo2") {
                            img {
                                src = "/assets/media/instant-logo.png"
                                height = "65px"
                            }
                        }
                        h1(classes = "splash-title-left") {
                            style = "text-align:left;"
                            +"APPROVA"
                        }
                    }

                    div(classes = "container") {
                        style = "position:relative;"
                        div {
                            style = "margin:0 auto; text-align:center;"
                            h3(classes = "container-main-heading") {
                                +"CENTRO DE CARGA"
                            }

                            /*span {
                                style = "position:absolute; right:10px; top:10px;"
                                img {
                                    src = "/assets/media/qr-code.gif"
                                    width = "60px"
                                    height = "60px"
                                }
                            }*/

                            p {
                                style = "text-align:center; margin-top:32px; margin-bottom:24px"
                                id = "identificationText"
                                a(classes = "container-sub-heading") {
                                    style = "color:#ff6700; font-weight:bold;"
                                    val title = when (uploadType) {
                                        "a" -> "Comprobante de domicilio (opcional)"
                                        "p" -> "Pasaporte"
                                        "i" -> "Comprobante de Ingresos"
                                        else -> "Identificación"
                                    }
                                    +title
                                }
                            }

                            if (uploadType.isBlank()) {
                                p {
                                    style = "text-align:center; margin-top:32px; margin-bottom:24px"
                                    id = "ineHeadText"
                                    a(classes = "container-title") {
                                        style = "color:#ff6700;"
                                        +"IFE/INE"
                                    }
                                }
                            }

                            p(classes = "container-title") {
                                id = "capture-documents-title"
                                style = "text-align:center; margin-top:32px;;color:#ff6700;"
                                +"Alinear el documento dentro del marco, cuando se presiona el boton de captura"
                            }

                        }

                        //  Document Summary Row
                        div(classes = "row") {
                            id = "summary-container"
                            style = "width:85%; margin:auto; display: none;"
                            div("col-md-12") {
                                id = "summary-images-preview"
                                style =
                                    "margin-top:50px; display:flex; flex-direction:column; align-items:center; position:relative;"

                                div("error-cross") {
                                    id = "crossAreaId"
                                    img {
                                        src = "/assets/media/cross.png"
                                    }
                                }
                            }

                            ineSideSummaryData()
                        }

                        div(classes = "row cameraContainer") {
                            id = "uploadContainer"
                            val blockMarginAdjust = if (uploadType.isBlank()) {
                                "-250px"
                            } else {
                                "0px"
                            }
                            style =
                                "display: flex; justify-content: center; align-items: center; flex-direction: row;margin-left:$blockMarginAdjust;"
                            if (uploadType.isBlank()) {
                                div(classes = "col-md-2") {
                                    id = "uploadTextBlock"
                                    style =
                                        "display:flex; justify-content:center; align-items:center; flex-direction:column;"
                                    h6 {
                                        style = "font-weight:bold;"
                                        id = "documentTitle"
                                        +"Captura frente al INE"
                                    }

                                    h6 {
                                        style = "font-weight:bold; color:white"
                                        id = "pageIndicator"
                                        +"Paso 1 de 2"
                                    }
                                }
                            }
                            div(classes = "col-md-5") {
//                                modalLoader()
                                div(classes = "progress-area") {
                                    id = "progressAreaId"
                                    style = "display:none;"
                                }
                                div {
                                    id = "images-preview"
                                    style = "display:flex; justify-content:space-around; margin-top:60px;"
                                }
                                documentPassportInput()

                                div(classes = "camera") {
                                    style = "display:flex; justify-content:center;"
                                    video {
                                        id = "video"
                                        height = "276px"
                                        width = "446px"
                                        +"Video stream not available."
                                    }
                                    /*val flipBtnRightAdjust = if (uploadType.isBlank()) "90px" else "38px"
                                    button {
                                        style =
                                            "position:absolute; top:0px; right:$flipBtnRightAdjust; padding:1px 3px; border:0;"
                                        id = "flipBtn"
                                        img {
                                            src = "assets/media/flip-icon.png"
                                            width = "30px"
                                            height = "30px"
                                        }
                                    }*/
                                    div {
                                        id = "camera-preview"
                                        style = "display: none;"
                                        canvas {
                                            id = "canvas"
                                        }
                                    }
                                }
                                div(classes = "mobileCameraBtnContainer") {
                                    a(classes = "btn splash_iniciar_btn retryMobileBtn") {
                                        id = "retry_btn"
                                        onClick = "onRetry(this)"
                                        +"No, intentalo de nuevo"
                                    }

                                    a(classes = "btn btn-primary splash_iniciar_btn captureMobileBtn") {
                                        id = "continue_btn"
                                        /*style = "width:40%; margin-left:15px;;border: solid;"*/
                                        +"Captura"
                                    }
                                }
                            }
                        }


                        div(classes = "row") {
                            div(classes = "col-md-2") {
                                id = "back-btn"
                                a(href = "#") {
//                                    onClick = "window.history.go(-1); return false;"
                                    href = "/upload/document/instructions?type=$uploadType".withBaseUrl()
                                    img {
                                        src = "/assets/media/back.png"
                                        width = "60px"
                                        height = "60px"
                                    }
                                }
                            }
                            div(classes = "col-md-8") {
                                id = "bottom-message"
                            }
                            div(classes = "col-md-2 forwardMobileBtn") {
                                id = "forward-btn"
                                style = "text-align:right;visibility:hidden; cursor:pointer;"
                                a {
                                    onClick = "onClickSubmit()"
//                                    href = "/person_info".withBaseUrl()
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

                input {
                    id = "backProtected"
                    value = "no"
                    type = InputType.text
                    style = "display:none;"
                }

                jscript(src = "assets/js/main.js")
                jscript(src = "assets/js/document-upload-common.js")
                jscript(src = "assets/js/camera-document-upload.js")
            }
        }
    }

    private fun DIV.documentPassportInput() {
        div {
            id = "passport-number-preview"
            style = "display:none; justify-content:center; flex-direction:column;align-items:center;"

            input(type = InputType.text, classes = "passportNoInput phoneValid") {
                id = "passportNumberId"
                name = "passport_number"
                minLength = "12"
                maxLength = "12"
            }

            p {
                id = "passport_number_instructions"
                style = "font-size:0.9rem;  color:#ff6700; text-align:center; margin-top:16px"
                +"Número de pasaporte:  12 dígitos ingresando al principio '000' a la izquierda"
            }

            span(classes = "error") {
                style = "font-size:1.2rem; display:none;"
                id = "passportNumberError"
                +"Pasaporte faltante o incompleto no."
            }

        }
    }
}