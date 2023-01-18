package com.frontend.controller

import com.model.UserModel
import com.plugins.UserSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import kotlinx.html.*

class AuthPageController(call: ApplicationCall) : HeaderPageController(call) {

    suspend fun handoff() {
        val params = call.parameters
        val type = params["type"] ?: ""
        val token = params["token"] ?: error("Invalid token")
        // verify token with database that received from parameters
        val user = UserModel.getUserByAuthToken(token) ?: error("Token expired!")
        // set user session for mobile screen
        call.sessions.set(UserSession(user.userId, user.mobileNumber, user.userAuth, true, user.status > 0))
        // redirect to upload document page on mobile screen
        call.respondRedirect("/uploadDocument?type=$type")
        // Update HanddOff Status in database
        UserModel.updateMobileHandOffFlag(true, user.userId)
    }

    suspend fun loginPage() {
        // clear all previously created sessions!
        call.sessions.clear("otp")
        call.sessions.clear("curp")
        call.sessions.clear<UserSession>()

//        call.sessions.clear("uploaddocuments")

        call.respondHtml(HttpStatusCode.OK) {
            // Head Part of Login Page
            headerLoginFile()
            // add same unsafe javascript and css code
            unsafe {
                +"""<style>
           
                |.iti{position: relative; display: block;}
                |.iti__arrow{display: none;}
    
                </style>""".trimMargin()
            }
            // Body Part of Login Page
            body {
                // include css file form mobile responsive login page
                css("assets/css/mobile-auth-style.css")
                div("main-container") {
                    header(classes = "header") {
                        span("karum-logo") {
                            img {
                                src = "/assets/media/instant-logo.png"
                                height = "65px"
                            }
                        }
                        /* h1(classes = "splash-title-left") {
                             style = "text-align:left; font-size:2.25rem; margin-left:20px;"
                             +"APPROVA"
                         }*/
                        h2(classes = "splash-subtitle-left") {
                            style = "text-align:left; font-size:2rem; padding:25px 0px;"
                            +"Proceso de originación de crédito "
                        }
                    }

                    div(classes = "container") {
                        id = "loginContainer"
                        form(method = FormMethod.post) {
                            div("col-lg-12 container_row") {

                                h4(classes = "container-sub-heading") {
                                    style = "padding-top:12px; margin-bottom:22px; text-align:center; color:#ff6700;"
                                    +"Información básica"
                                }

                                p(classes = "container-title") {
                                    +"Acepta y lee los avisos de privacidad para continuar con la solicitud de tu crédito "
                                }

                                div(classes = "row") {
                                    style = "width:80%; margin:40px auto; text-align:center; font-size:1.125rem;"
                                    div {
                                        div(classes = "col-lg-12 form-check form-check-inline") {
                                            style = "display:flex; justify-content:center;"
                                            input(classes = "form-check-input", type = InputType.checkBox) {
                                                style = "min-width: 20px; min-height: 20px;"
                                                id = "acceptPrivacyCheckId"
                                                name = "acceptPrivacyCheckBox"
                                                value = ""
                                            }
                                            label(classes = "checkbox_label") {
                                                id = "acceptPrivacyLinkId"
                                                style = "display:inline; text-align: left;"
                                                +"Acepto Aviso de Privacidad"
                                            }
                                        }
                                        span(classes = "error") {
                                            id = "acceptErrorId"
                                        }
                                    }
                                    div {
                                        div(classes = "col-lg-12 form-check form-check-inline") {
                                            style =
                                                "margin-top:25px; display:flex; justify-content:center;"
                                            input(classes = "form-check-input", type = InputType.checkBox) {
                                                style = "min-width: 20px; min-height: 20px;"
                                                id = "receiveMessageCheckId"
                                                name = "receiveMsgCheckBox"
                                                value = ""
                                            }
                                            label(classes = "checkbox_label") {
                                                id = "receiveMessageLinkId"
                                                style = "display:inline; text-align: left;"
                                                +"Acepta la compra de su teléfono celular"
                                            }
                                        }
                                        span(classes = "error") {
                                            id = "receiveMsgErrorId"
                                        }
                                    }
                                }

                                p(classes = "container-title") {
                                    +"Te enviaremos vía SMS un código para verificar tu número celular"
                                }

                                div(classes = "row login-input-container") {
                                    div(classes = "col-md-5 country-code-input") {
                                        input(classes = "login-form") {
                                            disabled = true
                                            id = "country_picker_id"
                                            placeholder = "Mexico   52"
                                            onKeyDown = "return false;"
                                            autoComplete = false
                                        }

                                        input(type = InputType.hidden) {
                                            id = "country"
                                            name = "country"
                                        }
                                    }
                                    div(classes = "col-md-5 mobile-phone-input") {
                                        input(
                                            classes = "login-form block-copy-paste numeric",
                                            type = InputType.number
                                        ) {
                                            id = "phone_number_id"
                                            name = "phone"
                                            placeholder = "Captura tu número de teléfono celular"
                                            autoComplete = false
                                            maxLength = "10"
                                            minLength = "10"
                                            pattern = "[0-9]"
                                            onKeyDown = "return event.keyCode !== 69"
                                        }
                                    }
                                }

                                div(classes = "login-btn-container") {
                                    button(classes = "btn btn-primary splash_iniciar_btn", type = ButtonType.submit) {
                                        id = "kt_login_signin_submit"
                                        +"Genera tu código SMS"
                                    }
                                }

                            }
                        }
                    }

                    div(classes = "container") {
                        id = "documentPrivacyContainer"
                        style = "display:none;"

                        div(classes = "row") {
                            style = "padding:16px 30px; font-size:1.125rem; text-align:left;"
                            div(classes = "col-md-12") {
                                h5 {
                                    style = "text-align:center; margin:20px 0px; color:#ff6700; font-weight:bold;"
//                                    +"AVISO DE PRIVACIDAD"
                                    +"Aviso de Privacidad"
                                }

                                unsafe {
                                    +"""<p style = "color: #182035">
                                        <b>Karum Operadora de Pagos S.A.P.I. de C.V., SOFOM E.N.R</b>. (en lo sucesivo “<b>KARUM</b>”), 
                                        con domicilio para oír y recibir notificaciones en Blvd. Manuel Ávila Camacho No. 
                                        5, Interior S 1000, Ed. Torre B, Piso 10, Of. 1045, Col. Lomas de Sotelo, Naucalpan 
                                        de Juárez, Estado de México, C.P. 53390, es el responsable del uso y protección de 
                                        sus datos personales, en ese contexto, le informa que los datos personales recabados, 
                                        serán utilizados para la operación de los productos que usted solicite o contrate, 
                                        así como para hacerle llegar información de promociones relacionadas con el mismo. 
                                        En el momento que usted solicita los servicios o productos de <b>KARUM</b> está de acuerdo 
                                        en adquirir el carácter de cliente de la Sociedad. Ponemos a su disposición el aviso 
                                        de privacidad integral en la página de internet <a href="https://www.karum.com/storage/operadora-aviso-de-privacidad-web.pdf" target="_blank">https://www.karum.com/storage/operadora-aviso-de-privacidad-web.pdf</a> 
                                        en donde se le da a conocer el detalle del tratamiento que se les dará a sus datos 
                                        personales, así como los derechos que usted puede hacer valer. En este acto el 
                                        titular de los datos personales otorga su consentimiento expreso a <b>KARUM</b> para tratar 
                                        sus datos personales y para transferirlos a sociedades que formen parte del grupo 
                                        económico de <b>KARUM</b> y/o terceros (con los que <b>KARUM</b> tenga celebrados acuerdos comerciales, 
                                        independientemente de que dicho acuerdo haya concluido, incluidas sociedades de 
                                        información crediticia, proveedores, subsidiarias, directas o indirectas y partes 
                                        relacionadas) que realicen operaciones de contratación de créditos y prestación de 
                                        servicios, promociones, publicidad, recompensas y demás contemplados en nuestro aviso 
                                        de privacidad, así como para el mantenimiento o cumplimiento de una relación 
                                        jurídica.
                                        </p>""".trimMargin()
                                }
                            }
                        }

                        div {
                            style = "text-align:right;"
                            a {
                                id = "documentPrivacyBackBtn"
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
                        id = "documentKarumContainer"
                        style = "display:none;"

                        div(classes = "row") {
                            style = "padding:16px 30px; font-size:1.125rem; text-align:left;"
                            div(classes = "col-md-12") {
                                h5 {
                                    style = "text-align:center; margin:20px 0px; color:#ff6700; font-weight:bold;"
//                                    +"Acepto recibir publicidad de productos y servicios Karum"
                                }

                                unsafe {
                                    +"""
                                        <p style = "color:#182035;">
                                        Por este conducto en términos de los artículos 273 y 285 del Código de Comercio 
                                        instruyo expresamente a <b>KARUM OPERADORA DE PAGOS S.A.P.I. DE C.V., SOFOM E.N.R. 
                                        (la “Comisionista”)</b>, para que una vez aprobada la solicitud de crédito que he 
                                        tramitado a través de esta plataforma y/o cualquiera de los Medios Electrónicos 
                                        descritos en la sección Términos y Condiciones del Uso de Medios 
                                        Electrónicos <a href="https://www.karum.com/storage/operadora-terminos-y-condiciones.pdf" target="_blank">https://www.karum.com/storage/operadora-terminos-y-condiciones.pdf</a>,
                                        la Comisionista adquiera con cargo a mi línea de crédito de forma inmediata e 
                                        irrevocable, por mi nombre y cuenta, en mi carácter de comitente, el equipo de 
                                        telefonía celular que previamente he elegido voluntariamente durante el proceso 
                                        de selección y compra de dicho equipo (proceso que ha dado origen a la solicitud 
                                        de crédito que nos ocupa). En ese sentido, acepto y reconozco expresamente que la 
                                        compraventa de dicho equipo se perfeccionará y surtirá plenos efectos legales tan 
                                        pronto como mi solicitud de crédito sea aprobada por la entidad acreditante y sin 
                                        necesidad de requerir una nueva aceptación o consentimiento adicional por mi parte; 
                                        razón por la cual una vez que sea aprobada mi solicitud de crédito (y por ende se 
                                        haya perfeccionado la compraventa del equipo de telefonía celular que yo he elegido), 
                                        en este acto manifiesto de forma expresa que me obligo a cumplir y pagar en favor de 
                                        mi acreditante todas y cada una de las contraprestaciones aplicables, de acuerdo a 
                                        los términos y condiciones establecidos en el Contrato de Apertura de Crédito de 
                                        Carácter Individual y todos sus anexos (el “Contrato”), mismo que habré conocido y 
                                        aceptado una vez concluido el proceso de otorgamiento de crédito a través de esta 
                                        plataforma y/o cualquiera de los Medios Electrónicos.
                                        </p>
                                    """.trimIndent()
                                }

                                p {
                                    style = "color:#182035;"
                                    +"""La Comisionista es ajena a las relaciones mercantiles o civiles existentes o que 
                                        surjan entre el comitente y los comercios (terceros) con los que se lleven a cabo 
                                        las operaciones de compraventa, o entre el comitente y aquéllos a quienes se 
                                        efectúen pagos por orden de éste y con cargo a su línea de crédito. Por lo que 
                                        la Comisionista no asumirá responsabilidad alguna por la calidad, cantidad, precio, 
                                        garantías, plazo de entrega o cualesquiera otras características de los bienes o 
                                        servicios que se adquieran por nombre y cuenta del comitente. En consecuencia, 
                                        cualquier derecho que llegare a asistir al comitente por los conceptos citados, 
                                        deberá hacerse valer directamente en contra de los terceros con los que se lleven 
                                        a cabo las operaciones de compraventa.""".trimMargin()
                                }
                            }
                        }

                        div {
                            style = "text-align:right;"
                            a {
                                style = "cursor: pointer;"
                                id = "documentKarumBackBtn"
                                img {
                                    src = "/assets/media/forward.png"
                                    width = "60px"
                                    height = "60px"
                                }
                            }
                        }
                    }
                }

                // include javascript classes for login page
                jscript(src = "assets/js/main.js")
                jscript(src = "assets/js/login-general.js")
            }
        }
    }

    suspend fun loginOtpPage() {
        // Login Otp web page response
        call.respondHtml(HttpStatusCode.OK) {
            // Head Part of Login Page
            headerLoginFile()
            // Body Part of Login Page
            body {
                div("main-container") {
                    header(classes = "header") {
                        span("karum-logo") {
                            img {
                                src = "/assets/media/instant-logo.png"
                                height = "65px"
                            }
                        }
                        /*h1(classes = "splash-title") { +"APPROVA" }*/
                        h2(classes = "splash-subtitle") {
                            style = "margin-top:0; padding:25px 0px;"
                            +"Proceso de originación de crédito "
                        }
                    }

                    div(classes = "container") {
                        style = "width:35%; margin-top:32px; padding-bottom:40px;"

                        form(method = FormMethod.post) {
                            id = "optFormId"
                            div("row otpContainer") {
                                style = "width:80%; margin:auto; padding-bottom:24px;"

                                h4(classes = "container-sub-heading") {
                                    style =
                                        "padding-top:40px; margin-bottom:15px; text-align:center; color:#ff6700; font-weight:normal;"
                                    +"Verifica tu número celular"
                                }

                                p(classes = "otp-paragraph") {
                                    +"Inserta tu código SMS a 6 dígitos "
                                }



                                div(classes = "row") {
                                    style = "width:70%; margin:auto; margin-top:16px;"
                                    div(classes = "userInput") {
                                        id = "otp"
                                        input(classes = "input-key text-center numeric", type = InputType.number, name = "first") {
                                            id = "first"
                                            maxLength = "1"
                                            minLength = "1"
                                            autoComplete = false
                                        }
                                        input(classes = "input-key text-center numeric", type = InputType.number, name = "second") {
                                            id = "sec"
                                            maxLength = "1"
                                            minLength = "1"
                                            autoComplete = false
                                        }
                                        input(classes = "input-key text-center numeric", type = InputType.number, name = "third") {
                                            id = "third"
                                            maxLength = "1"
                                            minLength = "1"
                                            autoComplete = false

                                        }
                                        input(classes = "input-key text-center numeric", type = InputType.number, name = "fourth") {
                                            id = "fourth"
                                            maxLength = "1"
                                            minLength = "1"
                                            autoComplete = false
                                        }
                                        input(classes = "input-key text-center numeric", type = InputType.number, name = "fifth") {
                                            id = "fifth"
                                            maxLength = "1"
                                            minLength = "1"
                                            autoComplete = false
                                        }
                                        input(classes = "input-key text-center numeric", type = InputType.number, name = "sixth") {
                                            id = "six"
                                            maxLength = "1"
                                            minLength = "1"
                                            autoComplete = false
                                        }
                                    }
                                }

                                p {
                                    id = "errorMessageId"
                                    style =
                                        "font-size:0.938rem; padding-left:16px; padding-right:16px; color:red; text-align:center; margin-top:16px"
                                    +" "
                                }

                            }
                        }

                        div {
                            style = "display:flex; justify-content:center; align-items:center; margin-top:30px;"
                            button(classes = "btn btn-primary otpContinueBtn", type = ButtonType.submit) {
                                onClick = "onOtpBtnClick(this)"
                                +"Continuar"
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
                                    onClick = "onLoginDismissDeclineModel()"
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

                // include javascript file for opt page
                jscript(src = "assets/js/login-general.js")
                jscript(src = "assets/js/main.js")
            }
        }
    }
}