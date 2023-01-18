package com.frontend.controller

import com.Env
import com.data.Estado
import com.data.FormDataList
import com.google.gson.Gson
import com.helper.withBaseUrl
import com.model.DocumentModel
import com.model.PersonInfoModel
import com.model.UserModel
import com.plugins.userSession
import com.tables.pojos.Tc41
import com.tables.pojos.Tc42
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import kotlinx.html.*

class InfoPageController(call: ApplicationCall) : HeaderPageController(call) {

    suspend fun personalInfoPage() {
        val userSession = call.userSession
        val user = UserModel.getUser(userSession.mobile) ?: error("Invalid session")
        if (!Env.DEBUG) {
            if (user.status < UserModel.DOCUMENT_COMPLETE || user.status > UserModel.SUMMARY_1_COMPLETE) {
                if (pageRedirect()) return
            }
        }

        val infoData = PersonInfoModel.getPersonInfoData(userSession.userId)
        val document = DocumentModel.getDocumentByUserId(userSession.userId)
        val isAuthenticate = user.status > UserModel.AUTH_CODE_TC42_ACCEPTED

//        REMOVE SESSION NOW DATA DIRECT STORE IN DB
        /*val summaryData = if (infoData == null) {
            call.sessions.get<IneSummaryData>()
        } else null*/

//        load curp data if not already available
        /*    val curpData = if (user.ine != null && user.curp != null) {
                kotlin.runCatching { CurpUtil.curpAPi.byCurp(user.curp).respuestaRENAPO?.curpStatus?.resultCURPS }
                    .onFailure { it.printStackTrace() }
                    .getOrNull()
            } else null*/

        call.respondHtml(HttpStatusCode.OK) {
            infoHeaderFile()

            body {
                if (infoData != null) {
                    val dob = "${infoData.year}-${infoData.month}-${infoData.day}"
                    script {
                        unsafe {
                            +"""const mGSelectedDob = '$dob';"""
                        }
                    }
                } /*else if (summaryData?.dob.isNullOrBlank().not()) {
                    script {
                        unsafe {
                            *//*+"""const mGSelectedDob = '${curpData?.formatInputDob()}';"""*//*
                            +"""const mGSelectedDob = '${
                                summaryData?.dob?.split("/")?.reversed()?.joinToString("-")
                            }';"""
                        }
                    }
                }*/
                div("main-container") {
                    header(classes = "info-header") {
                        span("karum-logo2") {
                            img {
                                src = "/assets/media/instant-logo.png"
                                height = "65px"
                            }
                        }
                        /* h1(classes = "splash-title-left") {
                             style = "display:inline-block; font-size:2.25rem;"
                             +"APPROVA"
                         }*/
                        h2(classes = "splash-subtitle-left") {
                            style = "display:inline-block; font-size:2rem; padding-top:20px;"
                            +"Pre – Calificación"
                        }
                    }

                    div(classes = "container") {
                        id = "personInfoContainer"
                        style = "position:relative;"

                        form(method = FormMethod.post) {
                            id = "personal_info_form"
                            div("row formRowContainer") {
                                style = "margin:0;"

                                h3("form-heading") { +"Información personal" }

                                div(classes = "row form-row-custom") {
                                    div(classes = "col-md-3") {
                                        label(classes = "form-custom") { +"Apellido(s) Paterno(s):" }
                                        input(
                                            classes = "custom-info-form nameValid",
                                            type = InputType.text,
                                            name = "parent_surname"
                                        ) {
                                            required = true
                                            id = "apellidoPaternoId"
                                            style = "width:85%;"
                                            placeholder = " "
                                            maxLength = "20"
                                            autoComplete = false
                                            /*if (summaryData?.parentSurname.isNullOrBlank().not()) {
                                                value = summaryData?.parentSurname ?: ""
                                            } else*/ if (infoData != null) {
                                            value = infoData.parentSurname ?: ""
                                        }
                                            if (isAuthenticate) readonly = true
                                        }
                                    }

                                    div(classes = "col-md-3") {
                                        label(classes = "form-custom") { +"Apellido(s) Materno(s):" }
                                        input(
                                            classes = "custom-info-form nameValid",
                                            type = InputType.text,
                                            name = "mother_surname"
                                        ) {
                                            id = "apellidoMaternoId"
                                            style = "width:85%;"
                                            placeholder = " "
                                            maxLength = "20"
                                            autoComplete = false
                                            /*if (summaryData?.motherSurname.isNullOrBlank().not()) {
                                                value = summaryData?.motherSurname ?: ""
                                            } else*/ if (infoData != null) {
                                            value = infoData.motherSurname ?: ""
                                        }
                                            if (isAuthenticate) readonly = true
                                        }
                                    }

                                    div(classes = "col-md-3") {
                                        label(classes = "form-custom") { +"Nombre(s):" }
                                        input(
                                            classes = "custom-info-form nameValid",
                                            type = InputType.text,
                                            name = "name"
                                        ) {
                                            id = "nombreId"
                                            style = "width:85%;"
                                            placeholder = " "
                                            maxLength = "20"
                                            autoComplete = false
                                            /*if (summaryData?.name.isNullOrBlank().not()) {
                                                value = summaryData?.name ?: ""
                                            } else*/ if (infoData != null) {
                                            value = infoData.name ?: ""
                                        }
                                            if (isAuthenticate) readonly = true
                                        }
                                    }

                                    div(classes = "col-md-3") {
                                        label(classes = "form-custom") { +"Estado de Nacimiento:" }
                                        br
                                        select(classes = "custom-info-form info-form-select") {
                                            id = "estadoId"
                                            name = "state_birth"
                                            style = "width:85%; border:none"
                                            if (isAuthenticate) disabled = true

                                            Estado().getStateList().forEach {
                                                option {
                                                    /*if (it.codeTC44 == summaryData?.birthState) {
                                                        selected = true
                                                    } else*/
                                                    if (infoData != null && it.codeTC44 == infoData.stateOfBirth) {
                                                        selected = true
                                                    }

                                                    attributes.apply {
                                                        put("curp_code", it.curpCode)
                                                    }
                                                    value = it.codeTC44
                                                    +it.title
                                                }
                                            }
                                        }
                                    }
                                }

                                div(classes = "row form-row-custom") {
                                    div(classes = "col-lg-4 col-12") {
                                        div {
                                            label(classes = "form-custom") { +"Fecha de nacimiento:" }
                                            br
                                            label(classes = "infoLabel") { +"Año:" }
                                            select(classes = "info-form-select-short yellowText") {
                                                style = "width:auto !important; border:none;"
                                                name = "year"
                                                id = "year"
                                                if (isAuthenticate) disabled = true
                                            }

                                            label(classes = "infoLabel") {
                                                style = "margin-left: 5px;"
                                                +"Mes:"
                                            }
                                            select(classes = "info-form-select-short yellowText") {
                                                style = "width:auto !important; border:none"
                                                name = "month"
                                                id = "month"
                                                if (isAuthenticate) disabled = true
                                            }

                                            label(classes = "infoLabel") {
                                                style = "margin-left: 5px;"
                                                +"Día:"
                                            }
                                            select(classes = "info-form-select-short yellowText") {
                                                style = "width:auto !important; border:none"
                                                name = "day"
                                                id = "day"
                                                if (isAuthenticate) disabled = true
                                            }
                                        }
                                        p {
                                            id = "age_error"
                                            style =
                                                "font-size:0.9rem; font-weight:bold; color:red; display: none;"
                                            +"La edad debe estar entre 21 Y 75"
                                        }
                                    }

                                    div(classes = "col-lg-8 col-12 genderContainer") {
                                        style = "display: flex;"
                                        div {
                                            style = "width: auto; margin-right:8px;"
                                            label(classes = "infoLabel") { +"Género:" }
                                            select(classes = "custom-info-form info-form-select") {
                                                id = "gender"
                                                name = "gender"
                                                style = "width:auto; border:none;"
                                                if (isAuthenticate) disabled = true
                                                option {
                                                    /*if (summaryData?.gender == "H") {
                                                        selected = true
                                                    } else*/ if (infoData != null && infoData.gender == "H") {
                                                    selected = true
                                                }
                                                    value = "H"
                                                    +"Hombre"
                                                }
                                                option {
                                                    /*if (summaryData?.gender == "M") {
                                                        selected = true
                                                    } else*/ if (infoData != null && infoData.gender == "M") {
                                                    selected = true
                                                }
                                                    value = "M"
                                                    +"Mujer"
                                                }
                                            }
                                        }

                                        div(classes = "curpContainer") {
                                            label(classes = "infoLabel") { +"CURP:" }
                                            /*input(classes = "custom-info-form correct-curp", type = InputType.text, name = "curpInput") {
                                                id = "curpInputId"
                                                style = "width:85%; border:none"
                                                placeholder = " "
                                                maxLength = "18"
                                                readonly = true
                                                if (curpSession != null) {
                                                    value = curpSession.curp
                                                }
                                            }*/
                                            var showCurpTick = false
                                            span(classes = "custom-info-form correct-curp") {
                                                style = "width:auto; border:none"
                                                input {
                                                    id = "curpInputHidden"
                                                    name = "curp"
                                                    type = InputType.hidden
                                                    if (user.curp != null) {
                                                        value = user.curp
                                                    }
                                                }
                                                input {
                                                    name = "ine"
                                                    type = InputType.hidden
                                                    if (user.ine != null) {
                                                        value = user.ine
                                                    }
                                                }
                                                span {
                                                    id = "curpInputId"
                                                    if (user.curp != null) {
                                                        showCurpTick = true
                                                        +user.curp
                                                    }
                                                }
                                                span("fa fa-check") {
                                                    id = "curpTickMark"
                                                    style =
                                                        "margin-left: 15px;color: #2EDB7C; visibility: ${if (showCurpTick) "visible" else "hidden"};"
                                                }
                                            }
                                            span(classes = "error") {
                                                id = "curpErrorId"
                                            }
                                        }
                                    }
                                }

                                h3("form-heading") { +"Domicilio" }

                                div(classes = "row form-row-custom") {
                                    div(classes = "col-md-3 col-8") {
                                        label(classes = "form-custom") { +"Calle:" }
                                        input(
                                            classes = "custom-info-form input-24-auto alphanumericspace",
                                            type = InputType.text
                                        ) {
                                            style = "width:75%;"
                                            placeholder = " "
                                            maxLength = "22"
                                            name = "street"
                                            autoComplete = false
                                            required = false
                                            if (infoData?.street != null) value = infoData.street ?: ""
                                            if (isAuthenticate) readonly = true
                                        }
                                    }

                                    div(classes = "col-md-1 col-4") {
                                        label(classes = "form-custom") { +"No Ext:" }
                                        input(
                                            classes = "custom-info-form block-copy-paste alphanumeric input-3-auto",
                                            type = InputType.text
                                        ) {
                                            style = "width:65%;"
                                            name = "ext_no"
                                            placeholder = " "
                                            maxLength = "5"
                                            minLength = "1"
                                            autoComplete = false
                                            required = true
                                            if (infoData?.extNo != null) value = infoData.extNo ?: ""
                                            if (isAuthenticate) readonly = true
                                        }
                                    }

                                    div(classes = "col-md-1 col-4") {
                                        label(classes = "simpleLabel") { +"No Int:" }
                                        input(
                                            classes = "custom-info-form block-copy-paste alphanumeric input-3-auto",
                                            type = InputType.text
                                        ) {
                                            style = "width:45%;"
                                            name = "int_no"
                                            placeholder = " "
                                            maxLength = "3"
                                            minLength = "1"
                                            autoComplete = false
                                            if (infoData?.intNo != null) value = infoData.intNo ?: ""
                                            if (isAuthenticate) readonly = true
                                        }
                                    }

                                    div(classes = "col-md-1 col-4") {
                                        label(classes = "form-custom") { +"C.P:" }
                                        input(
                                            classes = "custom-info-form input-5-auto block-copy-paste numeric numMaxLength",
                                            type = InputType.number
                                        ) {
                                            id = "cpCodeId"
                                            name = "zip"
                                            style = "width:60px;"
                                            placeholder = " "
                                            maxLength = "5"
                                            minLength = "5"
                                            autoComplete = false
                                            required = true
                                            onKeyDown = "return event.keyCode !== 69"
                                            if (infoData?.zipCode != null) value = infoData.zipCode ?: ""
                                            if (isAuthenticate) readonly = true
                                        }
                                    }

                                    div(classes = "col-md-2 col-4") {
                                        label(classes = "form-custom") { +"Estado:" }
                                        input(classes = "custom-info-form", type = InputType.text) {
                                            id = "stateEl"
                                            name = "state"
                                            style = "width:170px"
                                            placeholder = " "
                                            maxLength = "50"
                                            autoComplete = false
                                            readonly = true
                                            if (infoData?.state != null) value = infoData.state ?: ""
                                            if (isAuthenticate) readonly = true
                                        }
                                        input {
                                            id = "stateCode"
                                            name = "state_code"
                                            type = InputType.hidden
                                            if (infoData?.stateCode != null) value = infoData.stateCode ?: ""
                                        }
                                    }

                                    div(classes = "col-md-4 col-6") {
                                        label(classes = "form-custom") { +"Alcaldía o Municipio:" }
                                        br
                                        select(classes = "custom-info-form info-form-select") {
                                            id = "municipalityEl"
                                            name = "municipality"
                                            if (isAuthenticate) disabled = true
                                            if (infoData?.municipality != null) {
                                                style = "border-color:#2EDB7C;"
                                                option {
                                                    value = infoData.municipality
                                                    attributes["code"] = infoData.municipalityCode
                                                    +infoData.municipality
                                                }
                                            }
                                        }
                                        input {
                                            type = InputType.hidden
                                            id = "municipalityCode"
                                            name = "municipality_code"
                                            if (infoData?.municipalityCode != null) value = infoData.municipalityCode
                                        }
                                        /*input(classes = "custom-info-form", type = InputType.text) {
                                            style = "width:100%;"
                                            placeholder = " "
                                        }*/
                                    }
                                }

                                div(classes = "row form-row-custom") {
                                    div(classes = "col-md-5 col-6") {
                                        label(classes = "form-custom") { +"Colonia:" }
                                        br
                                        select(classes = "custom-info-form info-form-select") {
                                            id = "colonyEl"
                                            name = "colony"
                                            style = "width:85%;"
                                            if (isAuthenticate) disabled = true
                                            if (infoData?.colony != null) {
                                                style = "border-color:#2EDB7C;"
                                                option {
                                                    value = infoData.colony
                                                    attributes["code"] = infoData.colonyCode
                                                    +infoData.colony
                                                }
                                            }
                                        }
                                        input {
                                            type = InputType.hidden
                                            id = "colonyCode"
                                            name = "colony_code"
                                            if (infoData?.colonyCode != null) value = infoData.colonyCode
                                        }
                                    }
                                    div(classes = "col-md-4 col-6") {
                                        label(classes = "simpleLabel") { +"Ciudad:" }
                                        br
                                        select(classes = "custom-info-form info-form-select") {
                                            id = "cityEl"
                                            name = "city"
                                            style = "width:85%;"
                                            if (isAuthenticate) disabled = true
                                            if (infoData?.city != null) {
                                                style = "border-color:#2EDB7C;"
                                                option {
                                                    value = infoData.city
                                                    attributes["code"] = infoData.cityCode
                                                    +infoData.city
                                                }
                                            }
                                        }
                                        input {
                                            type = InputType.hidden
                                            id = "cityCode"
                                            name = "city_code"
                                            if (infoData?.cityCode != null) value = infoData.cityCode
                                        }
                                    }
                                }

                                div(classes = "row") {
                                    div(classes = "col-md-3 col-5") {
                                        label(classes = "form-custom") { +"Teléfono Celular :" }
                                        input(
                                            classes = "custom-info-form block-copy-paste numeric numMaxLength input-10-auto",
                                            type = InputType.number
                                        ) {
                                            id = "telephoneId"
                                            name = "telephone"
                                            style = "width:110px;"
                                            placeholder = " "
                                            maxLength = "10"
                                            minLength = "10"
                                            autoComplete = false
                                            required = true
                                            onKeyDown = "return event.keyCode !== 69"
//                                            if (infoData?.telephone != null) value = infoData.telephone ?: ""
                                            value = userSession.mobile.substring(3)
                                            readonly = true
                                        }

                                        span(classes = "error") {
                                            id = "numberErrorId"
                                        }
                                    }

                                    /*div(classes = "col-md-3 col-7") {
                                        label(classes = "form-custom") {
                                            style = "font-size: 0.96rem;"
                                            +"Confirmar Teléfono Celular:"
                                        }
                                        input(
                                            classes = "custom-info-form block-copy-paste numeric numMaxLength input-10-auto",
                                            type = InputType.number
                                        ) {
                                            id = "confirmTelephoneId"
                                            name = "confirm_telephone"
                                            style = "width:110px;"
                                            placeholder = " "
                                            maxLength = "10"
                                            minLength = "10"
                                            autoComplete = false
                                            required = true
                                            onKeyDown = "return event.keyCode !== 69"
                                            if (infoData?.telephone != null) value = infoData.telephone ?: ""
                                            if (isAuthenticate) readonly = true
                                        }
                                        span(classes = "error") {
                                            id = "confirmNumberErrorId"
                                        }
                                    }*/

                                    div(classes = "col-md-5 ") {
//                                        label(classes = "form-custom") { +"Correo electrónico  (Si este excede 40 caracteres  favor de omitir):" }
                                        label(classes = "form-custom") { +"Correo electrónico  (máximo 40 caracteres):" }
                                        input(classes = "custom-info-form input-40-auto", type = InputType.email) {
                                            name = "email"
                                            style = "width:100%;"
                                            placeholder = " "
                                            maxLength = "40"
                                            autoComplete = false
                                            required = true
                                            if (infoData?.email != null) value = infoData.email ?: ""
                                            if (isAuthenticate) readonly = true
                                        }
                                    }
                                }

                                h3("form-heading heading-top-margin") {
                                    style = "padding-top:8px;"
                                    +"Datos Laborales"
                                }

                                div(classes = "row form-row-custom") {
                                    div(classes = "col-md-3") {
                                        label(classes = "form-custom") { +"Nombre de la empresa:" }
                                        input(
                                            classes = "custom-info-form nameValid input-20-auto",
                                            type = InputType.text
                                        ) {
                                            name = "company_name"
                                            id = "companyName"
                                            style = "width:170px;"
                                            placeholder = " "
                                            maxLength = "20"
                                            autoComplete = false
                                            required = true
                                            if (infoData?.companyName != null) value = infoData.companyName ?: ""
                                            if (isAuthenticate) readonly = true
                                        }
                                    }

                                    div(classes = "col-md-3") {
                                        label(classes = "simpleLabel") { +"Teléfono de la empresa:" }
                                        input(
                                            classes = "custom-info-form block-copy-paste numeric numMaxLength input-10-auto",
                                            type = InputType.number
                                        ) {
                                            name = "company_phone"
                                            id = "company_phone"
                                            style = "width:95px;"
                                            placeholder = " "
                                            maxLength = "10"
                                            minLength = "10"
                                            autoComplete = false
                                            onKeyDown = "return event.keyCode !== 69"
                                            if (infoData?.companyPhone != null) value = infoData.companyPhone ?: ""
                                            if (isAuthenticate) readonly = true
                                        }
                                    }

                                    div(classes = "col-md-3") {
                                        label(classes = "form-custom") { +"Ingreso Mensual Bruto:" }
                                        input(
                                            classes = "custom-info-form numMaxLength numeric input-10-auto",
                                            type = InputType.number
                                        ) {
                                            name = "monthly_income"
                                            style = "width:75px;"
                                            placeholder = " "
                                            maxLength = "8"
                                            autoComplete = false
                                            required = true
                                            onKeyDown = "return event.keyCode !== 69"
                                            if (infoData?.monthlyIncome != null) value = infoData.monthlyIncome ?: ""
                                            if (isAuthenticate) readonly = true
                                        }
                                    }
                                    div(classes = "col-md-1")
                                    div(classes = "col-md-2") {
                                        h6(classes = "required-text-bottom") {
                                            +"*Campos Obligatorios"
                                        }
                                    }
                                }

                                if (userSession.isReturning && user.status > UserModel.DOCUMENT_COMPLETE) {
                                    hr(classes = "infoHorizontalLine") {
                                        style =
                                            "width:80%; margin:auto; height:3px; color:#000; opacity:.7; margin-top:15px;"
                                    }

                                    div(classes = "row info-last-row") {
                                        style = "margin-top:30px; font-weight:400;"
                                        div(classes = "col-md-12") {
                                            style = "display:flex; justify-content:center"
                                            div(classes = "col-md-8 col-12 returningUploadContainer") {
                                                style = "text-align:center;"
                                                if (document?.ineFront != null) {
                                                    span(classes = "col-md-4 col-4") {
                                                        a {
                                                            style = "cursor:pointer;"
                                                            href = "/documentPreview?type=".withBaseUrl()
                                                            img {
                                                                src = "/assets/media/document-view.png"
                                                                width = "50px"
                                                                height = "65px"
                                                            }
                                                        }
                                                        h6 {
                                                            style = "font-size:0.9rem; font-weight:bold;"
                                                            +"INE"
                                                        }
                                                    }
                                                }
                                                if (document?.passport != null) {
                                                    span(classes = "col-md-4 col-4") {
                                                        a {
                                                            style = "cursor:pointer;"
                                                            href = "/documentPreview?type=p".withBaseUrl()
                                                            img {
                                                                src = "/assets/media/document-view.png"
                                                                width = "50px"
                                                                height = "65px"
                                                            }
                                                        }
                                                        h6 {
                                                            style = "font-size:0.9rem; font-weight:bold;"
                                                            +"Pasaporte"
                                                        }
                                                    }
                                                }
                                                span(classes = "col-md-4 col-4") {
                                                    a {
                                                        style = "cursor:pointer;"
                                                        href = "/documentPreview?type=a".withBaseUrl()
                                                        img {
                                                            src = "/assets/media/document-view.png"
                                                            width = "50px"
                                                            height = "65px"
                                                        }
                                                    }
                                                    h6 {
                                                        style = "font-size:0.9rem; font-weight:bold;"
                                                        +"Comprobante de domicilio (opcional)"
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                // Forward Button
                                div {
                                    a(classes = "infoForwardBtn") {
                                        id = "submitPersonalInfo"
                                        if (isAuthenticate) {
                                            href = "/supplementaryData".withBaseUrl()
                                        } else {
                                            onClick = "onClickGenerateCurp(this)"
                                        }
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
                modalLoader()
                jscript(src = "assets/js/main.js")
                jscript(src = "assets/js/info-page.js")
            }
        }
    }

    suspend fun referenceSummaryPage() {
        val userSession = call.userSession
        val user = UserModel.getUser(userSession.mobile) ?: error("Invalid session")
        if (user.status != UserModel.AUTH_CODE_TC42_ACCEPTED) {
            if (pageRedirect()) return
        }
        val tc41Data = PersonInfoModel.getPersonInfoData(userSession.userId)

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
                css(href = "https://use.fontawesome.com/releases/v5.15.4/css/all.css")
                css("https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css")
                jscript(src = "https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js")

                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js")
                jscript(src = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js")
                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.2/jquery.form.min.js")
                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.19.1/jquery.validate.min.js")
                if (tc41Data != null) {
                    script {
                        unsafe {
                            +"""const mTc41Data = ${Gson().toJson(tc41Data)};""".trimMargin()
                        }
                    }
                }

                script {
                    unsafe {
                        +"""const mGBaseUrl = '${Env.BASE_URL}';""".trimMargin()
                        +"""const mUserStatus = '${user.status}';""".trimMargin()
                    }
                }
            }

            body {
                style = "background-color:#2f393f;"
                div("main-container-custom") {
                    div(classes = "container container-custom") {
                        form {
                            method = FormMethod.post
                            div("row formRowContainer") {
                                h3("form-heading margin-16-top sup-heading") {
                                    +"Datos Complementarios"
                                }

                                div(classes = "row form-row-custom") {
                                    style = "padding:0;"
                                    div(classes = "col-md-2 col-6") {
                                        label(classes = "infoLabel") { +"País de nacimiento " }
                                        p(classes = "yellowText") {
                                            style = "color:#000; font-size:1rem;"
                                            +"MEXICO"
                                        }
                                    }
                                    div(classes = "col-md-2 col-6") {
                                        label(classes = "infoLabel") { +"Nacionalidad" }
                                        p(classes = "yellowText") {
                                            style = "color:#000; font-size:1rem;"
                                            +"MEXICANA"
                                        }
                                    }
                                    div(classes = "col-md-2 col-12") {
                                        label(classes = "form-custom") { +"Medio de envío del estado de cuenta" }
                                        p(classes = "yellowText") {
                                            style = "color:#000 font-size:1rem;"
                                            +"ELECTRONICO"
                                        }
                                    }
                                    div(classes = "col-md-2 col-6") {
                                        label(classes = "form-custom") { +"Profesión" }
                                        input(
                                            classes = "custom-info-form alphanumeric input-15-auto",
                                            type = InputType.text
                                        ) {
                                            style = "width:85%;"
                                            placeholder = " "
                                            maxLength = "14"
                                            name = "profession"
                                            autoComplete = false
                                        }
                                    }
                                    div(classes = "col-md-2 col-6") {
                                        label(classes = "form-custom") { +"Teléfono de Casa" }
                                        input(
                                            classes = "custom-info-form block-copy-paste phoneNumber numMaxLength input-10-auto",
                                            type = InputType.number
                                        ) {
                                            style = "width:85%;"
                                            placeholder = " "
                                            maxLength = "10"
                                            minLength = "10"
                                            autoComplete = false
                                            name = "home_phone"
                                            id = "home_phone"
                                            onKeyDown = "return event.keyCode !== 69"
                                        }
                                        span(classes = "error") {
                                            style = "display:none;"
                                            +"Diferente a celular"
                                        }
                                    }
                                    div(classes = "col-md-2") {
                                        label(classes = "form-custom") { +"Antigüedad de domicilio" }
                                        div(classes = "domicilioContainer") {
                                            style = "display:flex"
                                            div("col-md-6 col-6 domicilioInputContainer") {

                                                label { +"Años:" }
                                                input(
                                                    classes = "info-form-select-short numeric numMaxLength yellowText",
                                                    type = InputType.number
                                                ) {
                                                    style = "text-align:center; width:35px;"
                                                    placeholder = " "
                                                    maxLength = "2"
                                                    autoComplete = false
                                                    name = "supYear"
                                                    onKeyDown = "return event.keyCode !== 69"
                                                }
                                            }
                                            div("col-md-6 col-6 domicilioInputContainer") {
                                                label { +"Meses:" }
                                                input(
                                                    classes = "info-form-select-short numeric numMaxLength meses yellowText",
                                                    type = InputType.number
                                                ) {
                                                    style = "text-align:center;"
                                                    placeholder = " "
                                                    maxLength = "2"
                                                    min = "1"
                                                    max = "11"
                                                    autoComplete = false
                                                    name = "supMonth"
                                                    onKeyDown = "return event.keyCode !== 69"
                                                }
                                            }
                                        }
                                    }
                                }

                                h3("form-heading sup-heading") {
                                    +"Datos laborales y económicos del solicitante"
                                }

                                div(classes = "row form-row-custom") {
                                    style = "padding:0;"
                                    div(classes = "col-md-3") {
                                        label(classes = "form-custom") { +"Nombre de la empresa" }
                                        p(classes = "yellowText") {
                                            style = "color:#000;"
                                            +(tc41Data?.companyName ?: "")
                                        }
                                    }

                                    div(classes = "col-md-9 col-12") {
                                        div(classes = "col-lg-12 form-check form-check-inline") {
                                            input(classes = "form-check-input", type = InputType.checkBox) {
                                                id = "load_from_home"
                                                style = " display:inline-block;"
                                            }
                                            label(classes = "domicileCheckLabel") {
                                                style = "display:inline; text-align: left;"
                                                +"¿Tu dirección  de trabajo es la misma que tu domicilio?"
                                            }
                                        }
                                    }
                                }

                                div(classes = "row form-row-custom") {
                                    style = "padding:0;"
                                    div(classes = "col-md-2 col-8") {
                                        label(classes = "form-custom") { +"Calle" }
                                        input(
                                            classes = "custom-info-form input-24-auto alphanumericspace",
                                            type = InputType.text
                                        ) {
                                            name = "street"
                                            style = "width:100%;"
                                            placeholder = " "
                                            maxLength = "22"
                                            autoComplete = false
                                        }
                                    }

                                    div(classes = "col-md-2 col-4") {
                                        label(classes = "form-custom") { +"No Ext" }
                                        input(
                                            classes = "custom-info-form block-copy-paste alphanumeric input-3-auto",
                                            type = InputType.text
                                        ) {
                                            name = "ext_no"
                                            style = "width:35%; padding-right:0px;"
                                            placeholder = " "
                                            maxLength = "5"
                                            minLength = "1"
                                            autoComplete = false
                                        }
                                    }

                                    div(classes = "col-md-1 col-6") {
                                        label(classes = "simpleLabel") { +"No Int" }
                                        input(
                                            classes = "custom-info-form block-copy-paste alphanumeric input-3-auto",
                                            type = InputType.text
                                        ) {
                                            style = "width:55%; padding-right:0px;"
                                            name = "int_no"
                                            placeholder = " "
                                            maxLength = "3"
                                            minLength = "1"
                                            autoComplete = false
                                        }
                                    }

                                    div(classes = "col-md-2 col-6") {
                                        label(classes = "form-custom") { +"C.P" }
                                        input(
                                            classes = "custom-info-form input-5-auto block-copy-paste numeric numMaxLength",
                                            type = InputType.number
                                        ) {
                                            id = "cpCodeId"
                                            name = "zip"
                                            style = "width:60px;"
                                            placeholder = " "
                                            maxLength = "5"
                                            minLength = "5"
                                            autoComplete = false
                                            onKeyDown = "return event.keyCode !== 69"
                                        }
                                    }
                                }

                                div(classes = "row form-row-custom") {
                                    style = "padding:0;"
                                    div(classes = "col-md-5 col-5") {
                                        label(classes = "form-custom") { +"Estado:" }
                                        input(classes = "custom-info-form", type = InputType.text) {
                                            id = "stateEl"
                                            name = "state"
                                            style = "width:85%;"
                                            placeholder = " "
                                            maxLength = "50"
                                            autoComplete = false
                                            readonly = true
                                        }
                                        input {
                                            id = "stateCode"
                                            name = "state_code"
                                            type = InputType.hidden
                                        }
                                    }

                                    div(classes = "col-md-5 col-7") {
                                        label(classes = "form-custom") { +"Alcaldía / Municipio" }
                                        br
                                        select(classes = "custom-info-form info-form-select") {
                                            id = "municipalityEl"
                                            name = "municipality"
                                            style = "width:100%;"
                                            option {
                                                value = ""
                                                +""
                                            }
                                        }
                                        input {
                                            id = "municipalityCode"
                                            name = "municipality_code"
                                            type = InputType.hidden
                                        }
                                    }
                                }

                                div(classes = "row form-row-custom") {
                                    style = "padding:0;"
                                    div(classes = "col-md-5 col-6") {
                                        label(classes = "form-custom") { +"Colonia:" }
                                        br
                                        select(classes = "custom-info-form info-form-select") {
                                            id = "colonyEl"
                                            name = "colony"
                                            style = "width:85%;"
                                        }
                                        input {
                                            type = InputType.hidden
                                            id = "colonyCode"
                                            name = "colony_code"
                                        }
                                    }

                                    div(classes = "col-md-2 col-6") {
                                        label(classes = "simpleLabel") { +"Ciudad:" }
                                        br
                                        select(classes = "custom-info-form info-form-select") {
                                            id = "cityEl"
                                            name = "city"
                                            style = "width:100%;"
                                            name = "city"
                                        }
                                        input {
                                            id = "cityCode"
                                            name = "city_code"
                                            type = InputType.hidden
                                        }
                                    }

                                    div(classes = "col-md-3 col-7") {
                                        label(classes = "form-custom") { +"Antigüedad" }
                                        br
                                        label(classes = "infoLabel") { +"Años:" }
                                        input(
                                            classes = "info-form-select-short numeric numMaxLength",
                                            type = InputType.number
                                        ) {
                                            style = "text-align:center;"
                                            placeholder = " "
                                            maxLength = "2"
                                            autoComplete = false
                                            name = "labYear"
                                            onKeyDown = "return event.keyCode !== 69"
                                        }

                                        label(classes = "infoLabel") { +"Meses:" }
                                        input(
                                            classes = "info-form-select-short meses numeric numMaxLength",
                                            type = InputType.number
                                        ) {
                                            style = "text-align:center;"
                                            placeholder = " "
                                            maxLength = "2"
                                            min = "1"
                                            max = "11"
                                            autoComplete = false
                                            name = "labMonth"
                                            onKeyDown = "return event.keyCode !== 69"
                                        }
                                    }

                                    div(classes = "col-md-2 col-5") {
                                        label(classes = "form-custom") { +"Giro" }
                                        br
                                        select(classes = "info-form-select") {
                                            id = "giroElementId"
                                            name = "giro"
                                            option {
                                                value = ""
                                                +"SELECCIONE"
                                            }
                                            FormDataList().giroList.entries.forEach {
                                                option {
                                                    value = it.key
                                                    +it.value
                                                }
                                            }
                                        }
                                    }

                                }

                                div(classes = "row form-row-custom") {
                                    style = "padding:0;"

                                    div(classes = "col-md-3 col-12") {
                                        label(classes = "form-custom") { +"Ocupación" }
                                        br
                                        select(classes = "info-form-select") {
                                            style = "width:100%;"
                                            id = "occupationElementId"
                                            name = "occupation"
                                            option {
                                                value = ""
                                                +"SELECCIONE"
                                            }
                                            FormDataList().occupationList.entries.forEach {
                                                option {
                                                    value = it.key
                                                    +it.value
                                                }
                                            }
                                        }
                                    }
                                    div(classes = "col-md-2 col-6") {
                                        label(classes = "form-custom") { +"Ingreso mensual bruto" }
                                        input(
                                            classes = "custom-info-form numeric numMaxLength input-10-auto",
                                            type = InputType.number
                                        ) {
                                            style = "width:100%;"
                                            placeholder = " "
                                            maxLength = "10"
                                            autoComplete = false
                                            onKeyDown = "return event.keyCode !== 69"
                                            if (tc41Data?.monthlyIncome.isNullOrBlank().not()) {
                                                value = tc41Data?.monthlyIncome ?: ""
                                                readonly = true
                                            }
                                        }
                                    }
                                    div(classes = "col-md-3 col-6") {
                                        label(classes = "simpleLabel") {
                                            style = "font-size: 0.9rem;"
                                            +"Teléfono de la empresa"
                                        }
                                        input(
                                            classes = "custom-info-form block-copy-paste phoneNumber numMaxLength input-10-auto",
                                            type = InputType.number
                                        ) {
                                            style = "width:60%;"
                                            placeholder = " "
                                            maxLength = "10"
                                            onKeyDown = "return event.keyCode !== 69"
                                            id = "company_phone"
                                            if (tc41Data?.companyPhone.isNullOrBlank().not()) {
                                                value = tc41Data?.companyPhone ?: ""
                                                readonly = true
                                            }
                                        }
                                    }
                                }

                                h3("form-heading sup-heading") {
                                    style = "margin-top:18px;"
                                    +"Referencia familiar"
                                }

                                div(classes = "row form-row-custom") {
                                    style = "padding:0"
                                    div(classes = "col-md-auto") {
                                        style = "padding-right:0;"
                                        label(classes = "form-custom") { +"Nombre completo de la referencia #1" }
                                        input(
                                            classes = "custom-info-form nameValid input-20-auto",
                                            type = InputType.text
                                        ) {
                                            name = "name_reference_one"
                                            id = "name_reference_one"
                                            style = "width:60%;"
                                            placeholder = " "
                                            maxLength = "18"
                                            required = true
                                            autoComplete = false
                                        }
                                        span(classes = "error") {
                                            style = "display:none;"
                                            +"Referencia duplicada"
                                        }
                                    }

                                    div(classes = "col-md-3") {
                                        label(classes = "form-custom") { +"Teléfono (con clave LADA) #1" }
                                        input(
                                            classes = "custom-info-form block-copy-paste phoneNumber numMaxLength input-10-auto",
                                            type = InputType.number
                                        ) {
                                            id = "telephone_reference_one"
                                            name = "telephone_reference_one"
                                            style = "width:40%;"
                                            placeholder = " "
                                            maxLength = "10"
                                            minLength = "10"
                                            required = true
                                            autoComplete = false
                                            onKeyDown = "return event.keyCode !== 69"
                                        }
                                        span(classes = "error") {
                                            style = "display:none;"
                                            +"Referencia duplicada"
                                        }
                                    }

                                    div(classes = "col-md-3") {
                                        label(classes = "form-custom") { +"Parentesco #1" }
                                        br
                                        select(classes = "info-form-select") {
                                            id = "familyRelation1ElementId"
                                            name = "familyRelation1"
                                            style = "width:50%;"
                                            option {
                                                value = ""
                                                +"SELECCIONE"
                                            }
                                            FormDataList().familyRelationList.entries.forEach {
                                                option {
                                                    value = it.key
                                                    +it.value
                                                }
                                            }
                                        }
                                    }
                                }

                                div(classes = "row form-row-custom") {
                                    style = "padding:0"
                                    div(classes = "col-md-auto") {
                                        label(classes = "form-custom") { +"Nombre completo de la referencia #2" }
                                        input(
                                            classes = "custom-info-form nameValid input-20-auto",
                                            type = InputType.text
                                        ) {
                                            name = "name_reference_two"
                                            id = "name_reference_two"
                                            style = "width:60%;"
                                            placeholder = " "
                                            maxLength = "18"
                                            required = true
                                            autoComplete = false
                                        }
                                        span(classes = "error") {
                                            style = "display:none;"
                                            +"Referencia duplicada"
                                        }
                                    }

                                    div(classes = "col-md-3") {
                                        label(classes = "form-custom") { +"Teléfono (con clave LADA) #2" }
                                        input(
                                            classes = "custom-info-form block-copy-paste phoneNumber numMaxLength input-10-auto",
                                            type = InputType.number
                                        ) {
                                            name = "telephone_reference_two"
                                            id = "telephone_reference_two"
                                            style = "width:40%;"
                                            placeholder = " "
                                            maxLength = "10"
                                            minLength = "10"
                                            required = true
                                            autoComplete = false
                                            onKeyDown = "return event.keyCode !== 69"
                                        }
                                        span(classes = "error") {
                                            style = "display:none;"
                                            +"Referencia duplicada"
                                        }
                                    }

                                    div(classes = "col-md-3") {
                                        label(classes = "form-custom") { +"Parentesco #2" }
                                        br
                                        select(classes = "info-form-select") {
                                            style = "width:50%;"
                                            id = "familyRelation2ElementId"
                                            name = "familyRelation2"
                                            option {
                                                value = ""
                                                +"SELECCIONE"
                                            }
                                            FormDataList().familyRelationList.entries.forEach {
                                                option {
                                                    value = it.key
                                                    +it.value
                                                }
                                            }
                                        }
                                    }
                                }

                                h3("form-heading sup-heading") {
                                    style = "margin-top:18px;"
                                    +"Referencias personales"
                                }

                                div(classes = "row form-row-custom") {
                                    style = "padding:0"
                                    div(classes = "col-md-3") {
                                        label(classes = "form-custom") { +"Nombre completo de la Referencia" }
                                        input(
                                            classes = "custom-info-form nameValid input-20-auto",
                                            type = InputType.text
                                        ) {
                                            style = "width:60%;"
                                            placeholder = " "
                                            maxLength = "18"
                                            name = "personalReferenceName"
                                            id = "personal_reference_name"
                                            required = true
                                            autoComplete = false
                                        }
                                        span(classes = "error") {
                                            style = "display:none;"
                                            +"Referencia duplicada"
                                        }
                                    }
                                    div(classes = "col-md-3") {
                                        label(classes = "form-custom") { +"Teléfono (con clave LADA)" }
                                        input(
                                            classes = "custom-info-form block-copy-paste phoneNumber numMaxLength input-10-auto",
                                            type = InputType.number
                                        ) {
                                            style = "width:40%;"
                                            placeholder = " "
                                            maxLength = "10"
                                            minLength = "10"
                                            name = "personalReferencePhone"
                                            id = "personal_reference_phone"
                                            required = true
                                            onKeyDown = "return event.keyCode !== 69"
                                        }
                                        span(classes = "error") {
                                            style = "display:none;"
                                            +"Referencia duplicada"
                                        }
                                    }
                                    div(classes = "col-md-3")
                                    div(classes = "col-md-2") {
                                        style = "padding:0; text-align:right;"
                                        h6(classes = "required-text-bottom") {
                                            style = "padding-top:20px;"
                                            +"*Campos Obligatorios"
                                        }
                                    }
                                    div(classes = "col-md-1 supplementaryForwardBtn") {
                                        a {
                                            style = "width:45px; height:45px;"
                                            onClick = "onSubmitForm(this)"
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

                modalLoader()
                jscript(src = "assets/js/main.js")
                jscript(src = "assets/js/supplementary-page.js")
                script {
                    unsafe {
                        +"""
                            validateDuplicateMain([document.querySelector('#telephone_reference_one'), document.querySelector('#telephone_reference_two'), document.querySelector('#personal_reference_phone'), document.querySelector('#home_phone'), document.querySelector('#company_phone')], ["${tc41Data?.telephone}"], "", [document.querySelector('#home_phone'), document.querySelector('#company_phone')]);
                            validateDuplicateMain([document.querySelector('#name_reference_one'), document.querySelector('#name_reference_two'), document.querySelector('#personal_reference_name')], ["abc"]);
                        """.trimIndent()

                    }
                }
            }
        }
    }

    suspend fun mobileDataPage() {
        val userSession = call.userSession
        UserModel.updateUserStatus(UserModel.MOBILE_DATA_COMPLETE, userSession.userId)
        call.respondRedirect("/declaration")
        val tc41Data = PersonInfoModel.getPersonInfoData(userSession.userId)
        val tc42Data = PersonInfoModel.getSupplementaryData(userSession.userId)
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
                if (tc41Data != null) {
                    script {
                        unsafe {
                            +"""const mTc41Data = ${Gson().toJson(tc41Data)};""".trimMargin()
                            +"""const mTc42Data = ${Gson().toJson(tc42Data)};""".trimMargin()
                        }
                    }
                }

            }
            headerInfoFile()
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
                            style = "display:inline-block;"
                            +"APPROVA"
                        }
                        h2(classes = "splash-subtitle-left") {
                            style = "display:inline-block;"
                            +"- Pre - calificación"
                        }
                    }

                    div(classes = "container") {
                        form(method = FormMethod.post) {
                            div("row") {
                                style = "padding:20px 25px;"
                                h3("form-heading") { +"Teléfono móvil" }

                                div(classes = "row form-row-custom") {
                                    div(classes = "col-md-4") {
                                        label(classes = "form-custom") { +"Apellido(s) Paterno(s):" }
                                        p {
                                            style = "color: white;"
                                            +(tc41Data?.parentSurname ?: "")
                                        }
                                    }
                                    div(classes = "col-md-4") {
                                        label(classes = "form-custom") { +"Apellido(s) Materno(s):" }
                                        p {
                                            style = "color: white;"
                                            +(tc41Data?.motherSurname ?: "")
                                        }
                                    }
                                    div(classes = "col-md-4") {
                                        label(classes = "form-custom") { +"Nombre(s):" }
                                        p {
                                            style = "color: white;"
                                            +(tc41Data?.name ?: "")
                                        }
                                    }
                                }

                                div(classes = "row") {
                                    style = "margin-bottom:15px;"
                                    div(classes = "col-md-12") {
                                        h4 {
                                            style = "color:#ffb700; font-size:14px; display:inline-block;"
                                            +"¿Dirección de entrega del teléfono móvil?"
                                        }
                                    }
                                }

                                div(classes = "row") {
                                    div(classes = "col-md-12") {
                                        input(classes = "form-check-input", type = InputType.checkBox) {
                                            style = " display:inline-block;"
                                            id = "load_from_home"
                                        }
                                        p {
                                            style = "color:#000; font-size:17px; display:inline-block;"
                                            +"La dirección de entrega del teléfono móvil es la misma que la del domicilio"
                                        }
                                    }
                                }

                                div(classes = "row") {
                                    style = "margin-bottom:15px;"
                                    div(classes = "col-md-12") {
                                        input(classes = "form-check-input", type = InputType.checkBox) {
                                            style = " display:inline-block;"
                                            id = "load_from_work"
                                        }
                                        p {
                                            style = "color:#000; font-size:17px; display:inline-block;"
                                            +"La dirección de entrega del teléfono móvil es la misma que la del trabajo"
                                        }
                                    }
                                }

                                div(classes = "row form-row-custom") {
                                    div(classes = "col-md-4") {
                                        label(classes = "form-custom") { +"Calle" }
                                        input(classes = "custom-info-form alphanumericspace", type = InputType.text) {
                                            name = "street"
                                            style = "width:85%;"
                                            required = true
                                            placeholder = " "
                                            maxLength = "22"
                                            autoComplete = false
                                        }
                                    }

                                    div(classes = "col-md-2") {
                                        label(classes = "form-custom") { +"No Ext" }
                                        input(
                                            classes = "custom-info-form block-copy-paste alphanumeric",
                                            type = InputType.text
                                        ) {
                                            name = "ext_no"
                                            style = "width:55%;"
                                            required = true
                                            placeholder = " "
                                            maxLength = "3"
                                            minLength = "1"
                                            autoComplete = false
                                        }
                                    }

                                    div(classes = "col-md-2") {
                                        label(classes = "form-custom") { +"No Int" }
                                        input(
                                            classes = "custom-info-form block-copy-paste alphanumeric",
                                            type = InputType.text
                                        ) {
                                            name = "int_no"
                                            style = "width:55%;"
                                            placeholder = " "
                                            maxLength = "3"
                                            minLength = "1"
                                            autoComplete = false
                                        }
                                    }

                                    div(classes = "col-md-4") {
                                        label(classes = "form-custom") { +"C.P" }
                                        input(
                                            classes = "custom-info-form block-copy-paste phoneValid",
                                            type = InputType.number
                                        ) {
                                            id = "cpCodeId"
                                            name = "zip"
                                            style = "width:55px;"
                                            required = true
                                            maxLength = "5"
                                            minLength = "5"
                                            placeholder = " "
                                            onKeyDown = "return event.keyCode !== 69"
                                        }
                                    }
                                }

                                div(classes = "row form-row-custom") {
                                    div(classes = "col-md-4") {
                                        label(classes = "form-custom") { +"Estado" }
                                        input(classes = "custom-info-form", type = InputType.text) {
                                            id = "stateEl"
                                            style = "width:85%;"
                                            required = true
                                            placeholder = " "
                                            name = "state"
                                        }
                                        input {
                                            type = InputType.hidden
                                            id = "stateCode"
                                            name = "state_code"
                                        }
                                    }

                                    div(classes = "col-md-4") {
                                        label(classes = "form-custom") { +"Alcaldía / Municipio" }
                                        br
                                        select(classes = "info-form-select") {
                                            id = "municipalityEl"
                                            name = "municipality"
                                            style = "width:100%;"
                                            option {
                                                value = ""
                                                +""
                                            }
                                        }
                                        input {
                                            id = "municipalityCode"
                                            name = "municipality_code"
                                            type = InputType.hidden
                                        }
                                    }

                                    div(classes = "col-md-4") {
                                        label(classes = "form-custom") { +"Colonia:" }
                                        br
                                        select(classes = "custom-info-form info-form-select") {
                                            id = "colonyEl"
                                            name = "colony"
                                            style = "width:85%;"
                                        }
                                        input {
                                            type = InputType.hidden
                                            id = "colonyCode"
                                            name = "colony_code"
                                        }
                                    }
                                }

                                div(classes = "row form-row-custom") {
                                    div(classes = "col-md-4") {
                                        label(classes = "form-custom") { +"Ciudad:" }
                                        br
                                        select(classes = "custom-info-form info-form-select") {
                                            id = "cityEl"
                                            name = "city"
                                            style = "width:85%;"
                                        }
                                        input {
                                            type = InputType.hidden
                                            id = "cityCode"
                                            name = "city_code"
                                        }
                                    }
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
                                    style = "width:45px; height:45px;cursor:pointer"
//                                href = "/declaration".withBaseUrl()
                                    onClick = "onSubmitMobileDataForm(this)"
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

                jscript(src = "assets/js/mobile-data-page.js")
                //jscript(src = "assets/js/info-page.js")
            }
        }
    }

    suspend fun infoDataViewPage() {
        val userSession = call.userSession
        val user = UserModel.getUser(userSession.mobile) ?: error("Invalid session")
        if (user.status < UserModel.TC44_API_COMPLETE) {
            if (pageRedirect()) return
        }
        val infoData = PersonInfoModel.getPersonInfoData(userSession.userId)
        val tc42 = PersonInfoModel.getSupplementaryData(userSession.userId)

        call.respondHtml(HttpStatusCode.OK) {
            infoHeaderFile()

            body {
                if (infoData != null) {
                    val dob = "${infoData.year}-${infoData.month}-${infoData.day}"
                    script {
                        unsafe {
                            +"""const mGSelectedDob = '$dob';"""
                        }
                    }
                }
                div("main-container") {
                    header(classes = "info-header") {
                        span("karum-logo2") {
                            img {
                                src = "/assets/media/instant-logo.png"
                                height = "65px"
                            }
                        }
                        h1(classes = "splash-title-left") {
                            style = "display:inline-block; font-size:2.25rem;"
                            +"APPROVA"
                        }
                        h2(classes = "splash-subtitle-left") {
                            style = "display:inline-block; font-size:2rem;"
                            +"- Pre - calificación"
                        }
                    }

                    div(classes = "container") {
                        style = "position:relative; width:80%; height:80vh !important; overflow:auto;"
                        form {
                            div("row") {
                                style = "padding:20px;"
                                h3("form-heading") { +"Informacion Personal" }

                                div(classes = "row form-row-custom") {
                                    div(classes = "col-md-3 col-6") {
                                        label(classes = "form-custom") { +"Apellido(s) Paterno(s):" }
                                        p {
                                            style = "color:white;"
                                            +(infoData?.parentSurname ?: "")
                                        }
                                    }

                                    div(classes = "col-md-3 col-6") {
                                        label(classes = "form-custom") { +"Apellido(s) Materno(s):" }
                                        p {
                                            style = "color:white;"
                                            +(infoData?.motherSurname ?: "")
                                        }
                                    }

                                    div(classes = "col-md-3 col-6") {
                                        label(classes = "form-custom") { +"Nombre(s):" }
                                        p {
                                            style = "color:white;"
                                            +(infoData?.name ?: "")
                                        }
                                    }

                                    div(classes = "col-md-3 col-6") {
                                        label(classes = "form-custom") { +"Estado de Nacimiento:" }
                                        br
                                        select(classes = "custom-info-form info-form-select") {
                                            style = "width:85%; border:none"
                                            disabled = true

                                            Estado().getStateList().forEach {
                                                option {
                                                    if (it.codeTC44 == infoData?.stateOfBirth) {
                                                        selected = true
                                                    }
                                                    +it.title
                                                }
                                            }
                                        }
                                    }
                                }

                                div(classes = "row") {
                                    div(classes = "col-md-4 col-12") {
                                        div {
                                            label(classes = "form-custom") { +"Fecha de Nacimento:" }
                                            br
                                            label(classes = "infoLabel") { +"Ano:" }
                                            select(classes = "info-form-select-short") {
                                                style = "width:18%; border:none"
                                                name = "year"
                                                id = "year"
                                                disabled = true
                                            }

                                            label(classes = "infoLabel") {
                                                style = "margin-left: 10px;"
                                                +"Mes:"
                                            }
                                            select(classes = "info-form-select-short") {
                                                style = "width:24%;border:none"
                                                name = "month"
                                                id = "month"
                                                disabled = true
                                            }

                                            label(classes = "infoLabel") {
                                                style = "margin-left: 10px;"
                                                +"Dia:"
                                            }
                                            select(classes = "info-form-select-short") {
                                                style = "width:14%; border:none"
                                                name = "day"
                                                id = "day"
                                                disabled = true
                                            }
                                        }
                                    }

                                    div(classes = "col-md-2 col-4 margin-16-top") {
                                        label(classes = "infoLabel") { +"Genero:" }
                                        p {
                                            style = "color:white;"
                                            +if (infoData?.gender == "H") "Hombre" else "Mujer"
                                        }
                                    }

                                    div(classes = "col-md-4 col-8 margin-16-top") {
                                        label(classes = "infoLabel") { +"CURP:" }
                                        var showCurpTick = false
                                        span(classes = "custom-info-form correct-curp") {
                                            style = "width:85%; border:none"
                                            input {
                                                id = "curpInputHidden"
                                                name = "curp"
                                                type = InputType.hidden
                                                if (user.curp != null) {
                                                    value = user.curp
                                                }
                                            }
                                            input {
                                                name = "ine"
                                                type = InputType.hidden
                                                if (user.ine != null) {
                                                    value = user.ine
                                                }
                                            }
                                            span {
                                                id = "curpInputId"
                                                if (user.curp != null) {
                                                    showCurpTick = true
                                                    +user.curp
                                                }
                                            }
                                            span("fa fa-check") {
                                                id = "curpTickMark"
                                                style =
                                                    "margin-left: 15px;color: #2EDB7C; visibility: ${if (showCurpTick) "visible" else "hidden"};"
                                            }
                                        }
                                        span(classes = "error") {
                                            id = "curpErrorId"
                                        }
                                    }
                                }

                                h3("form-heading") { +"Domicilio" }

                                div(classes = "row form-row-custom") {
                                    div(classes = "col-md-3 col-4") {
                                        label(classes = "form-custom") { +"Calle:" }
                                        p {
                                            style = "color:white;"
                                            +(infoData?.street ?: "")
                                        }
                                    }

                                    div(classes = "col-md-1 col-4") {
                                        label(classes = "form-custom") { +"No Ext:" }
                                        p {
                                            style = "color:white;"
                                            +(infoData?.extNo ?: "")
                                        }
                                    }

                                    div(classes = "col-md-1 col-4") {
                                        label(classes = "infoLabel margin-16-top") { +"No Int:" }
                                        p {
                                            style = "color:white;"
                                            +(infoData?.intNo ?: "")
                                        }
                                    }

                                    div(classes = "col-md-1 col-3") {
                                        label(classes = "form-custom") { +"C.P:" }
                                        p {
                                            style = "color:white;"
                                            +(infoData?.zipCode ?: "")
                                        }
                                    }

                                    div(classes = "col-md-2 col-3") {
                                        label(classes = "form-custom") { +"Estado:" }
                                        p {
                                            style = "color:white;"
                                            +(infoData?.state ?: "")
                                        }
                                    }

                                    div(classes = "col-md-5 col-12") {
                                        label(classes = "form-custom") { +"Alcaldia o Municipio:" }
                                        p {
                                            style = "color:white;"
                                            +(infoData?.municipality ?: "")
                                        }
                                    }
                                }

                                div(classes = "row form-row-custom") {
                                    div(classes = "col-md-5 col-6") {
                                        label(classes = "form-custom") { +"Colonia:" }
                                        p {
                                            style = "color:white;"
                                            +(infoData?.colony ?: "")
                                        }
                                    }
                                    div(classes = "col-md-4 col-6") {
                                        label(classes = "form-custom") { +"Ciudad:" }
                                        p {
                                            style = "color:white;"
                                            +(infoData?.city ?: "")
                                        }
                                    }
                                }

                                div(classes = "row") {
                                    div(classes = "col-md-2 col-12") {
                                        label(classes = "form-custom") { +"Telefono Celular:" }
                                        p {
                                            style = "color:white;"
                                            +(infoData?.telephone ?: "")
                                        }
                                    }

                                    div(classes = "col-md-3 col-12") {
                                        label(classes = "form-custom") { +"Confirmar Telefono Celular:" }
                                        p {
                                            style = "color:white;"
                                            +(infoData?.telephone ?: "")
                                        }
                                    }

                                    div(classes = "col-md-5 col-12") {
                                        label(classes = "form-custom") { +"E-Mail (Si su Mail excede de 40 caracteres favor de omitir):" }
                                        p {
                                            style = "color:white;"
                                            +(infoData?.email ?: "")
                                        }
                                    }
                                }

                                h3("form-heading") {
                                    style = "padding-top:8px;"
                                    +"Datos Laborales"
                                }

                                div(classes = "row form-row-custom") {
                                    div(classes = "col-md-3 col-6") {
                                        label(classes = "form-custom") { +"Nombre da la Empresa:" }
                                        p {
                                            style = "color:white;"
                                            +(infoData?.companyName ?: "")
                                        }
                                    }

                                    div(classes = "col-md-3 col-6") {
                                        label(classes = "form-custom") { +"Telefono da la Empressa:" }
                                        p {
                                            style = "color:white;"
                                            +(infoData?.companyPhone ?: "")
                                        }
                                    }

                                    div(classes = "col-md-3 col-12") {
                                        label(classes = "form-custom") { +"Ingreso Mensual Bruto:" }
                                        p {
                                            style = "color:white;"
                                            +(infoData?.monthlyIncome ?: "")
                                        }
                                    }
                                }

                                supplementaryDataView(infoData, tc42)
                            }
                        }
                    }
                }

                jscript(src = "assets/js/info-page.js")
            }
        }
    }

    suspend fun familyReferencePage() {
        call.respondHtml(HttpStatusCode.OK) {
            infoHeaderFile()

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
                                h3("form-heading") { +"Referencia familiar" }

                                div(classes = "row") {
                                    style = "margin-bottom:15px; font-size:16px;"
                                    div(classes = "col-md-4") {
                                        label(classes = "form-custom") { +"Nombre completo de la referencia #1" }
                                        input(classes = "custom-info-form", type = InputType.text) {
                                            style = "width:85%;"
                                            placeholder = " "
                                            autoComplete = false
                                        }
                                    }

                                    div(classes = "col-md-4") {
                                        label(classes = "form-custom") { +"Teléfono (con clave LADA) #1" }
                                        input(classes = "custom-info-form", type = InputType.text) {
                                            style = "width:85%;"
                                            placeholder = " "
                                            autoComplete = false
                                        }
                                    }
                                }

                                div(classes = "row") {
                                    style = "margin-bottom:15px; font-size:16px;"
                                    div(classes = "col-md-4") {
                                        label(classes = "form-custom") { +"Parentesco #1" }
                                        br
                                        select(classes = "info-form-select") {
                                            style = "width:85%;"
                                            option { +"SELECCIONE" }
                                        }
                                    }
                                }

                                div(classes = "row") {
                                    style = "margin-bottom:15px; font-size:16px; margin-top:30px;"
                                    div(classes = "col-md-4") {
                                        label(classes = "form-custom") { +"Nombre completo de la referencia #2" }
                                        input(classes = "custom-info-form", type = InputType.text) {
                                            style = "width:85%;"
                                            placeholder = " "
                                            autoComplete = false
                                        }
                                    }

                                    div(classes = "col-md-4") {
                                        label(classes = "form-custom") { +"Teléfono (conclave LADA) #2" }
                                        input(classes = "custom-info-form", type = InputType.text) {
                                            style = "width:85%;"
                                            placeholder = " "
                                        }
                                    }
                                }

                                div(classes = "row") {
                                    style = "margin-bottom:15px; font-size:16px;"
                                    div(classes = "col-md-4") {
                                        label(classes = "form-custom") { +"Parentesco #2" }
                                        input(classes = "custom-info-form", type = InputType.text) {
                                            style = "width:85%;"
                                            placeholder = " "
                                        }
                                    }
                                }

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
                                href = "/personalReference".withBaseUrl()
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

    fun DIV.supplementaryDataView(infoData: Tc41?, tc42: Tc42?) {
        h3("form-heading") { +"Datos Complementarios" }

        div(classes = "row form-row-custom") {
            div(classes = "col-md-2 col-6") {
                label(classes = "infoLabel") { +"País de nacimiento" }
                p {
                    style = "color:#fff; font-size:1rem;"
                    +"MEXICO"
                }
            }
            div(classes = "col-md-2 col-6") {
                label(classes = "infoLabel") { +"Nacionalidad" }
                p {
                    style = "color:#fff; font-size:1rem;"
                    +"MEXICANA"
                }
            }
            div(classes = "col-md-2 col-12") {
                label(classes = "form-custom") { +"Medio de envío del estado de cuenta " }
                p {
                    style = "color:#fff; font-size:1rem;"
                    +"ELECTRONICO"
                }
            }
            div(classes = "col-md-2 col-6") {
                label(classes = "form-custom") { +"Profesion" }
                p {
                    style = "color:white;"
                    +(tc42?.profession ?: "")
                }
            }
            div(classes = "col-md-2 col-6") {
                label(classes = "form-custom") { +"Telefono de Casa" }
                p {
                    style = "color:white;"
                    +(tc42?.homePhone ?: "")
                }
            }
            div(classes = "col-md-2 col-6") {
                label(classes = "form-custom") { +"Antiguedad Domicilio" }
                div("row") {
                    div("col-md-6 col-5 margin-12-left") {
                        style = "padding:0;"
                        label(classes = "infoLabel") { +"Años:" }
                        p {
                            style = "color:white;"
                            +(tc42?.supYear.toString() ?: "")
                        }
                    }
                    div("col-md-6 col-5 ") {
                        style = "padding:0;"
                        label(classes = "infoLabel") { +"Meses:" }
                        p {
                            style = "color:white;"
                            +(tc42?.supMonth.toString() ?: "")
                        }
                    }
                }
            }
        }

        h3("form-heading") { +"Datos laborales y económicos del solicitante " }

        div(classes = "row form-row-custom") {
            div(classes = "col-md-3") {
                label(classes = "form-custom") { +"Nombre de la empresa" }
                p {
                    style = "color:white;"
                    +(infoData?.companyName ?: "")
                }
            }
        }

        div(classes = "row form-row-custom") {
            div(classes = "col-md-1 col-4") {
                label(classes = "form-custom") { +"Calle" }
                p {
                    style = "color:white;"
                    +(tc42?.street ?: "")
                }
            }

            div(classes = "col-md-1 col-4") {
                label(classes = "form-custom") { +"No Ext" }
                p {
                    style = "color:white;"
                    +(tc42?.extNo ?: "")
                }
            }

            div(classes = "col-md-1 col-4") {
                label(classes = "form-custom") { +"No Int" }
                p {
                    style = "color:white;"
                    +(tc42?.intNo ?: "")
                }
            }
        }

        div(classes = "row form-row-custom") {
            div(classes = "col-md-2 col-3") {
                label(classes = "form-custom") { +"C.P" }
                p {
                    style = "color:white;"
                    +(tc42?.zipCode ?: "")
                }
            }

            div(classes = "col-md-2 col-3") {
                label(classes = "form-custom") { +"Estado:" }
                p {
                    style = "color:white;"
                    +(tc42?.state ?: "")
                }
            }

            div(classes = "col-md-2 col-6") {
                label(classes = "form-custom") { +"Alcaldía / Municipio" }
                p {
                    style = "color:white;"
                    +(tc42?.municipality ?: "")
                }
            }
        }

        div(classes = "row form-row-custom") {
            div(classes = "col-md-5 col-6") {
                label(classes = "form-custom") { +"Colonia:" }
                p {
                    style = "color:white;"
                    +(tc42?.colony ?: "")
                }
            }

            div(classes = "col-md-2 col-6") {
                label(classes = "form-custom") { +"Ciudad:" }
                p {
                    style = "color:white;"
                    +(tc42?.city ?: "")
                }
            }

            div(classes = "col-md-3 col-6") {
                label(classes = "form-custom") { +"Antiguedad" }
                div("row") {
                    div("col-md-6 col-5 margin-12-left") {
                        style = "padding:0;"
                        label(classes = "infoLabel") { +"Años:" }
                        p {
                            style = "color:white;"
                            +(tc42?.labYear.toString() ?: "")
                        }
                    }
                    div("col-md-6 col-5") {
                        style = "padding:0;"
                        label(classes = "infoLabel") { +"Meses:" }
                        p {
                            style = "color:white;"
                            +(tc42?.labMonth.toString() ?: "")
                        }
                    }
                }
            }

            div(classes = "col-md-2 col-6") {
                label(classes = "form-custom") { +"Giro" }
                br
                select(classes = "info-form-select") {
                    style = "width:80%; border:0;"
                    disabled = true
                    FormDataList().giroList.entries.forEach {
                        option {
                            if (it.key == tc42?.giro) {
                                selected = true
                            }
                            value = it.key
                            +it.value
                        }
                    }
                }
            }
        }

        div(classes = "row form-row-custom") {
            div(classes = "col-md-3") {
                label(classes = "form-custom") { +"Ocupación" }
                br
                select(classes = "info-form-select") {
                    style = "width:100%; border:0;"
                    disabled = true
                    FormDataList().occupationList.entries.forEach {
                        option {
                            if (it.key == tc42?.occupation) {
                                selected = true
                            }
                            value = it.key
                            +it.value
                        }
                    }
                }
            }

            div(classes = "col-md-2") {
                label(classes = "form-custom") { +"Ingreso Mensual Bruto" }
                p {
                    style = "color:white;"
                    +(infoData?.monthlyIncome ?: "")
                }
            }
            div(classes = "col-md-3") {
                label(classes = "form-custom") { +"Telefono de la Empresa" }
                p {
                    style = "color:white;"
                    +(infoData?.companyPhone ?: "")
                }
            }
        }

        h3("form-heading") {
            style = "padding-top:18px;"
            +"Referencia familiar"
        }

        div(classes = "row form-row-custom") {
            div(classes = "col-md-auto") {
                style = "padding-right:0;"
                label(classes = "form-custom") { +"Nombre completo de la referencia #1" }
                p {
                    style = "color:white;"
                    +(tc42?.familyReferenceName1 ?: "")
                }
            }

            div(classes = "col-md-3") {
                label(classes = "form-custom") { +"Teléfono (con clave LADA) #1" }
                p {
                    style = "color:white;"
                    +(tc42?.familyReferencePhone1 ?: "")
                }
            }

            div(classes = "col-md-3") {
                label(classes = "form-custom") { +"Parentesco #1" }
                br
                select(classes = "info-form-select") {
                    style = "width:45%; border:0;"
                    disabled = true
                    FormDataList().familyRelationList.entries.forEach {
                        option {
                            if (it.key == tc42?.familyRelation1) {
                                selected = true
                            }
                            value = it.key
                            +it.value
                        }
                    }
                }
            }
        }

        div(classes = "row form-row-custom") {
            div(classes = "col-md-auto") {
                label(classes = "form-custom") { +"Nombre completo de la referencia #2" }
                p {
                    style = "color:white;"
                    +(tc42?.familyReferenceName2 ?: "")
                }
            }

            div(classes = "col-md-3") {
                label(classes = "form-custom") { +"Teléfono (conclave LADA) #2" }
                p {
                    style = "color:white;"
                    +(tc42?.familyReferencePhone2 ?: "")
                }
            }

            div(classes = "col-md-3") {
                label(classes = "form-custom") { +"Parentesco #2" }
                br
                select(classes = "info-form-select") {
                    style = "width:45%; border:0;"
                    disabled = true
                    FormDataList().familyRelationList.entries.forEach {
                        option {
                            if (it.key == tc42?.familyRelation2) {
                                selected = true
                            }
                            value = it.key
                            +it.value
                        }
                    }
                }
            }
        }

        h3("form-heading") {
            style = "padding-top:18px;"
            +"Referencias personales"
        }

        div(classes = "row form-row-custom") {
            div(classes = "col-md-3") {
                label(classes = "form-custom") { +"Nombre completo de la Referencia" }
                p {
                    style = "color:white;"
                    +(tc42?.personalReferenceName ?: "")
                }
            }
            div(classes = "col-md-3") {
                label(classes = "form-custom") { +"Teléfono (con clave LADA)" }
                p {
                    style = "color:white;"
                    +(tc42?.personalReferencePhone ?: "")
                }
            }
            div(classes = "col-md-4")
            div(classes = "col-md-2") {
                style = "text-align:right; margin-bottom:-10px; cursor:pointer"
                a {
                    style = "width:45px; height:45px;"
//                      Call Complementos API on Click
//                      onClick = "onClickReviewData(this)"
                    href = "/goodBye".withBaseUrl()
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
