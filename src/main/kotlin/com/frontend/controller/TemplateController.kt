package com.frontend.controller

import com.Env
import com.ServerSession
import com.controller.Controller
import com.frontend.Icons
import com.helper.CircularIterator
import com.helper.withBaseUrl
import com.mActiveRoute

import io.ktor.server.application.*
import io.ktor.server.auth.*

import io.ktor.server.html.respondHtml
import kotlinx.html.*
import kotlinx.html.stream.createHTML

import java.util.*

import io.ktor.util.pipeline.PipelineContext

open class TemplateController(call: ApplicationCall) : Controller(call) {

    suspend fun pageTemplate(bodyContent: MetroScaffold.() -> Unit) {

        html {
            metroHeader {
                unsafe {
                    +"""<script>var KTAppOptions = {
                        "colors": {
                        "state": {
                        "brand": "#5d78ff",
                        "light": "#ffffff",
                        "dark": "#282a3c",
                        "primary": "#5867dd",
                        "success": "#34bfa3",
                        "info": "#36a3f7",
                        "warning": "#ffb822",
                        "danger": "#fd3995"
                    },
                        "base": {
                        "label": ["#c5cbe3", "#a1a8c3", "#3d4465", "#3e4466"],
                        "shape": ["#f0f3ff", "#d9dffa", "#afb4d4", "#646c9a"]
                    }
                    }
                    };</script>"""
                }

                //<!-- end::Global Config -->

                //<!--begin::Global Theme Bundle(used by all pages) -->

                //<!--begin:: Vendor Plugins -->
                jscript(src = "https://cdn.jsdelivr.net/npm/js-cookie@2.2.1/src/js.cookie.min.js")
                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery.perfect-scrollbar/1.4.0/perfect-scrollbar.min.js")
                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/sticky-js/1.2.0/sticky.min.js")
                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/wnumb/1.1.0/wNumb.min.js")
                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery.blockUI/2.70/jquery.blockUI.min.js")
                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/bootstrap-maxlength/1.7.0/bootstrap-maxlength.min.js")
                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.11/js/bootstrap-select.min.js")
                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/bootstrap-switch/3.3.4/js/bootstrap-switch.min.js")
                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/es6-promise/4.1.1/es6-promise.min.js")
                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/dompurify/2.0.3/purify.min.js")

                //<!--begin:: Vendor Plugins for custom pages -->
                jscript(src = "https://cdn.datatables.net/1.10.20/js/jquery.dataTables.min.js")
                jscript(src = "https://cdn.datatables.net/fixedcolumns/4.0.1/js/dataTables.fixedColumns.min.js")
                jscript(src = "https://cdn.datatables.net/1.10.20/js/dataTables.bootstrap4.min.js")
                jscript(src = "https://cdn.datatables.net/buttons/1.6.0/js/dataTables.buttons.min.js")
                jscript(src = "https://cdn.datatables.net/rowreorder/1.2.7/js/dataTables.rowReorder.min.js")
                jscript(src = "https://cdn.datatables.net/buttons/1.6.0/js/buttons.bootstrap4.min.js")
                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.53/pdfmake.min.js")
                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.53/vfs_fonts.js")
                jscript(src = "https://cdn.datatables.net/buttons/1.6.0/js/buttons.flash.min.js")
                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jszip/3.1.3/jszip.min.js")
                jscript(src = "https://cdn.datatables.net/buttons/1.6.0/js/buttons.html5.min.js")
                jscript(src = "https://cdn.datatables.net/buttons/1.6.0/js/buttons.print.min.js")
                jscript(src = "https://cdn.datatables.net/responsive/2.2.3/js/dataTables.responsive.min.js")
                jscript(src = "https://cdn.datatables.net/responsive/2.2.3/js/responsive.bootstrap4.min.js")
                //<!--end:: Vendor Plugins for custom pages -->
                //<!--end::Global Theme Bundle -->

                jscript(src = "https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js")
                jscript(src = "https://cdn.datatables.net/buttons/1.6.1/js/buttons.colVis.min.js")

                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.13/js/select2.min.js")
                jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/image-picker/0.3.1/image-picker.min.js")
                jscript(src = "https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/js/bootstrap-select.min.js")

                //<!--end::Page Vendors -->

                //<!--begin::Page Scripts(used by this page) -->

            }
            metroScaffold(call.principal()!!) {
                modal("modal_change_password", "Change Password", "UPDATE", "update-password-btn") {
                    form(classes = "kt-form") {
                        method = FormMethod.post
                        id = "password-change-form"
                        formGroupRow {
                            formInputWithLabel("Old Password", type = InputType.password) {
                                name = "old_password"
                            }
                        }

                        formGroupRow {
                            formInputWithLabel(
                                label = "New Password",
                                name = "new_password"
                            )
                        }
                    }
                }
                bodyContent()
            }
        }
    }

    suspend fun html(block: HTML.() -> Unit) {
        call.respondHtml { block(this) }
    }
}

open class MetroBODY(initialAttributes: Map<String, String>, override val consumer: TagConsumer<*>) :
    BODY(initialAttributes, consumer) {
    var bottomBlock: (BODY.() -> Unit)? = null
}

class MetroScaffold(initialAttributes: Map<String, String>, override val consumer: TagConsumer<*>) :
    MetroBODY(initialAttributes, consumer) {
    var mobileToolbar: BODY.() -> Unit = {}
    var toolbar: BODY.() -> Unit = {}
}

suspend fun PipelineContext<*, ApplicationCall>.html(block: HTML.() -> Unit) {
    call.respondHtml { block() }
}

fun HTML.metroScaffold(mAppSession: ServerSession, block: MetroScaffold.() -> Unit) {
    body("kt-quick-panel--right kt-demo-panel--right kt-offcanvas-panel--right kt-header--fixed kt-header-mobile--fixed kt-subheader--enabled kt-subheader--solid kt-aside--enabled kt-aside--fixed kt-aside--minimize kt-page--loading") {
        val metroScaffold = MetroScaffold(emptyMap, consumer)

        metroScaffold.mobileToolbar(this@body)

        metroMobileToolbar()

        div("kt-grid kt-grid--hor kt-grid--root") {
            div("kt-grid__item kt-grid__item--fluid kt-grid kt-grid--ver kt-page") {
                //<!-- begin:: Aside -->
                ktAsideMenu(mAppSession)
                //<!-- end:: Aside -->
                div("kt-grid__item kt-grid__item--fluid kt-grid kt-grid--hor kt-wrapper") {
                    id = "kt_wrapper"
                    //begin header
                    metroToolbar(mAppSession)
                    //end header
                    div(classes = "kt-content  kt-grid__item kt-grid__item--fluid kt-grid kt-grid--hor") {
                        id = "kt_content"
                        div(classes = "kt-container  kt-container--fluid  kt-grid__item kt-grid__item--fluid") {
                            style = "padding-top:25px;"
                            block(metroScaffold)
                        }
                    }
                }
            }
        }

        jscript(src = "/assets/js/main.js")
        jscript(src = "/assets/js/app.js")

        metroScaffold.bottomBlock?.invoke(this)
    }
}

fun HTML.metroHeader(
    title: String = "${Env.Data.SHORT_TITLE} | Dashboard",
    defaultStyleSheets: Boolean = true,
    block: HEAD.() -> Unit = {}
) {
    head {

        meta(charset = "utf-8")
        title(title)
        meta(name = "description", content = "Sportise Admin Panel")
        meta(name = "viewport", content = "width=device-width, initial-scale=1, shrink-to-fit=no")

        //<!--begin::Fonts -->
        link(
            rel = "stylesheet",
            href = "https://fonts.googleapis.com/css?family=Poppins:300,400,500,600,700|Asap+Condensed:500"
        )

        meta(name = "theme-color", content = "#ffffff")

        //<!--begin::Global Theme Styles(used by all pages) -->
        //<!--begin:: Vendor Plugins -->


        if (defaultStyleSheets) {
            headerStylesheets()
        }

        script {
            unsafe {
                +"""const mGBaseUrl = '${Env.BASE_URL}';"""
            }
        }
//                    for login only
        //<!--end:: Vendor Plugins for custom pages -->

        //<!--end::Global Theme Styles -->

        jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js")
        jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js")
        jscript(src = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js")
        jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.24.0/moment.min.js")
        jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.2/jquery.form.min.js")
        jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.19.1/jquery.validate.min.js")
        jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.19.1/additional-methods.min.js")
        jscript(src = "https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js")
        jscript(src = "/assets/js/scripts.bundle.min.js")

        block()

//        link(rel = "shortcut icon", href = "${Env.BASE_URL}/assets/media/logos/favicon.ico")

    }
}

fun DIV.metroSubHeader() {
    div(classes = "kt-subheader kt-grid__item") {
        id = "kt_subheader"
        div(classes = "kt-container kt-container--fluid ") {
            div(classes = "kt-subheader__main") {
                h3(classes = "kt-subheader__title") {
                    +"Dashboard"
                }
                span(classes = "kt-subheader__separator kt-hidden")
                div(classes = "kt-subheader__breadcrumbs") {
                    a(href = "#", classes = "kt-subheader__breadcrumbs-home") {
                        i(
                            classes = "flaticon2-shelter"
                        )
                    }
                    span(classes = "kt-subheader__breadcrumbs-separator")
                    a(href = "", classes = "kt-subheader__breadcrumbs-link") {
                        +"Application"
                    }


                }
            }
            div(classes = "kt-subheader__toolbar") {
                div(classes = "kt-subheader__wrapper") {
                    a(href = "#", classes = "btn kt-subheader__btn-daterange") {
                        id = "kt_dashboard_daterangepicker"
//                                data-toggle="kt-tooltip" title="Select dashboard daterange" data-placement="left"
                        span(classes = "kt-subheader__btn-daterange-title") {
                            id = "kt_dashboard_daterangepicker_title"
                            +"Today"
                        }
                        unsafe { +"&nbsp" }
                        span(
                            classes = "kt-subheader__btn-daterange-date"
                        ) {
                            id = "kt_dashboard_daterangepicker_date"
                            +"Aug 16"
                        }

                        unsafe {
                            +"""<svg xmlns="http://www.w3.org/2000/svg"
                                                 xmlns:xlink="http://www.w3.org/1999/xlink" width="24px" height="24px"
                                                 viewBox="0 0 24 24" version="1.1" classes="kt-svg-icon kt-svg-icon--sm"){
                                                <g stroke="none" stroke-width="1" fill="none" fill-rule="evenodd"){
                                                    <rect x="0" y="0" width="24" height="24"/>
                                                    <path d="M4.875,20.75 C4.63541667,20.75 4.39583333,20.6541667 4.20416667,20.4625 L2.2875,18.5458333 C1.90416667,18.1625 1.90416667,17.5875 2.2875,17.2041667 C2.67083333,16.8208333 3.29375,16.8208333 3.62916667,17.2041667 L4.875,18.45 L8.0375,15.2875 C8.42083333,14.9041667 8.99583333,14.9041667 9.37916667,15.2875 C9.7625,15.6708333 9.7625,16.2458333 9.37916667,16.6291667 L5.54583333,20.4625 C5.35416667,20.6541667 5.11458333,20.75 4.875,20.75 Z"
                                                          fill="#000000" fill-rule="nonzero" opacity="0.3"/>
                                                    <path d="M2,11.8650466 L2,6 C2,4.34314575 3.34314575,3 5,3 L19,3 C20.6568542,3 22,4.34314575 22,6 L22,15 C22,15.0032706 21.9999948,15.0065399 21.9999843,15.009808 L22.0249378,15 L22.0249378,19.5857864 C22.0249378,20.1380712 21.5772226,20.5857864 21.0249378,20.5857864 C20.7597213,20.5857864 20.5053674,20.4804296 20.317831,20.2928932 L18.0249378,18 L12.9835977,18 C12.7263047,14.0909841 9.47412135,11 5.5,11 C4.23590829,11 3.04485894,11.3127315 2,11.8650466 Z M6,7 C5.44771525,7 5,7.44771525 5,8 C5,8.55228475 5.44771525,9 6,9 L15,9 C15.5522847,9 16,8.55228475 16,8 C16,7.44771525 15.5522847,7 15,7 L6,7 Z"
                                                          fill="#000000"/>
                                                </g>
                                            </svg>"""
                        }
                    }
                }
            }
        }
    }
}

fun DIV.metroToolbar(mAppSession: ServerSession, title: String = "Karum Card Managment System") {
    div("kt-header kt-grid kt-grid--ver  kt-header--fixed") {
        id = "kt_header"
        attributes["data-ktheader-minimize"] = "on"
        //<!-- begin:: Brand -->
        div(classes = "kt-header__brand kt-grid__item  ") {
            id = "kt_header_brand"
            style = "background-color:#fff;"
            div(classes = "kt-header__brand-logo") {
                a(href = "/".withBaseUrl()) {
                    img(alt = "Logo", src = "/assets/media/app_logo_short.png".withBaseUrl()) {
                        height = "55px"
                        width = "60px"
                    }
                }
            }
        }

        h3(classes = "kt-header__title kt-grid__item") { +title }
        //<!-- end:: Brand -->

        div(classes = "kt-header__topbar") {

            //<!--begin: User bar -->
            div(classes = "kt-header__topbar-item kt-header__topbar-item--user show") {
                div(classes = "kt-header__topbar-wrapper") {
                    attributes.apply {
                        put("data-toggle", "dropdown")
                        put("data-offset", "10px,0px")
                        put("aria-expanded", "true")
                    }

                    span(classes = "kt-hidden kt-header__topbar-welcome") { +"Hi," }
                    span(classes = "kt-hidden kt-header__topbar-username") { +mAppSession.username }
//                    img(classes = "kt-hidden", alt = "Pic", src = "/assets/media/users/300_21.jpg")
                    span(classes = "kt-header__topbar-icon kt-hidden-") { i(classes = "flaticon2-user-outline-symbol") }
                }
                div(classes = "dropdown-menu dropdown-menu-fit dropdown-menu-right dropdown-menu-anim dropdown-menu-xl") {
                    style =
                        "position: absolute; will-change: transform; top: 0px; left: 0px; transform: translate3d(767px, -5px, 0px);"
                    attributes["x-placement"] = "top-end"

                    //<!--begin: Head -->
                    div(classes = "kt-user-card kt-user-card--skin-dark kt-notification-item-padding-x") {
                        style = "background-image: url(assets/media/misc/bg-1.jpg)"
                        div(classes = "kt-user-card__avatar") {
                            //                            img(classes = "kt-hidden", alt = "Pic", src = "/assets/media/users/300_25.jpg")

                            //<!--use below badge element instead the user avatar to display username's first letter(remove kt-hidden class to display it) -->
                            span(classes = "kt-badge kt-badge--lg kt-badge--rounded kt-badge--bold kt-font-success") {
                                +"${
                                    mAppSession.username.first().toString().uppercase()
                                }"
                            }
                        }
                        div(classes = "kt-user-card__name") {
                            +"${mAppSession.username}"
                        }

                    }

                    //<!--end: Head -->

                    //<!--begin: Navigation -->
                    div(classes = "kt-notification") {


                        div(classes = "kt-notification__custom kt-space-between") {
                            a(
                                href = "/logout".withBaseUrl(),
                                target = "_self",
                                classes = "btn btn-label btn-label-brand btn-sm btn-bold"
                            ) {
                                +"Sign Out"
                            }

                            /* a(href = "#", classes = "btn btn-clean btn-sm btn-bold") {
                                 attributes.apply {
                                     put("data-toggle", "modal")
                                     put("data-target", "#modal_change_password")
                                 }
                                 +"Change Password"
                             }*/
                        }
                    }

                    //<!--end: Navigation -->
                }
            }
            //<!--end: User bar -->
        }
    }
}

fun BODY.metroMobileToolbar() {
    div("kt-header-mobile  kt-header-mobile--fixed") {
        id = "kt_header_mobile"
        /*  div("kt-header-mobile__logo") {
              a("/") {
                  img("Logo", src = "${Env.BASE_URL}/assets/media/logos/anime-logo.png")
              }
          }*/

        div(classes = "kt-header-mobile__toolbar") {
            button(classes = "kt-header-mobile__toolbar-toggler kt-header-mobile__toolbar-toggler--left") {
                id = "kt_aside_mobile_toggler"
                span()
            }
            button(classes = "kt-header-mobile__toolbar-topbar-toggler") {
                id = "kt_header_mobile_topbar_toggler"
                i(classes = "flaticon-more-1")
            }
        }
    }
}

object BgColor {
    const val GREEN = "bg-green"
    const val BLUE = "bg-blue"
    const val LIGHT_BLUE = "bg-light-blue"
    const val YELLOW = "bg-yellow"
    const val RED = "bg-red"
    const val CHOCLATE = "bg-choclate"
    const val MAROON = "bg-maroon"
    const val AQUA = "bg-aqua"
    const val PURPLE = "bg-purple"
    const val ORANGE = "bg-orange"
    const val BLACK = "bg-black"
    const val DARK = "bg-dark"
    const val LIME = "bg-lime"

    val colors = CircularIterator(PURPLE, GREEN, CHOCLATE, BLUE, ORANGE, MAROON, YELLOW, LIGHT_BLUE, DARK, AQUA, LIME)
}


fun DIV.ktAsideMenu(mAppSession: ServerSession) {
    //<!-- begin:: Aside -->
    button(classes = "kt-aside-close ") {
        id = "kt_aside_close_btn"
        i(classes = "la la-close")
    }
    div(classes = "kt-aside  kt-aside--fixed  kt-grid__item kt-grid kt-grid--desktop kt-grid--hor-desktop") {
        id = "kt_aside"
        //<!-- begin:: Aside Menu -->
        div(classes = "kt-aside-menu-wrapper kt-grid__item kt-grid__item--fluid") {
            id = "kt_aside_menu_wrapper"
            div(classes = "kt-aside-menu") {
                id = "kt_aside_menu"
                attributes.apply {
                    put("data-ktmenu-vertical", "1")
                    put("data-ktmenu-scroll", "1")
                    put("data-ktmenu-dropdown-timeout", "500")
//                    put("data-ktmenu-dropdown", "1")
                }

                ul(classes = "kt-menu__nav ") {
                    li(classes = "kt-menu__item" + if (mActiveRoute == "/") " kt-menu__item--active" else "") {
                        a(href = "/".withBaseUrl(), classes = "kt-menu__link ") {
                            i(classes = "kt-menu__link-icon ${Icons.FontAwesome.home}")
                            span(classes = "kt-menu__link-text") { +"Dash Board" }
                        }
                    }


                    li(classes = "kt-menu__item" + if (mActiveRoute == "channel") " kt-menu__item--active" else "") {
                        a(href = "/card_application".withBaseUrl(), classes = "kt-menu__link ") {
//                            i(classes = "kt-menu__link-icon flaticon-buildings")
                            i(classes = "kt-menu__link-icon ${Icons.FontAwesome.list}")
                            span(classes = "kt-menu__link-text") { +"Apply Card" }
                        }
                    }

                    li(classes = "kt-menu__item" + if (mActiveRoute == "channel") " kt-menu__item--active" else "") {
                        a(href = "/pending_application".withBaseUrl(), classes = "kt-menu__link ") {
//                            i(classes = "kt-menu__link-icon flaticon-buildings")
                            i(classes = "kt-menu__link-icon ${Icons.FontAwesome.list}")
                            span(classes = "kt-menu__link-text") { +"Pending Application" }
                        }
                    }

                    li(classes = "kt-menu__item" + if (mActiveRoute == "channel") " kt-menu__item--active" else "") {
                        a(href = "/pending_application".withBaseUrl(), classes = "kt-menu__link ") {
//                            i(classes = "kt-menu__link-icon flaticon-buildings")
                            i(classes = "kt-menu__link-icon ${Icons.FontAwesome.list}")
                            span(classes = "kt-menu__link-text") { +"Approved Application" }
                        }
                    }

                    li(classes = "kt-menu__item" + if (mActiveRoute == "users") " kt-menu__item--active" else "") {
                        a(href = "/search".withBaseUrl(), classes = "kt-menu__link ") {
                            i(classes = "kt-menu__link-icon ${Icons.FontAwesome.search}")
                            span(classes = "kt-menu__link-text") { +"Search" }
                        }
                    }

                    /*               li(classes = "kt-menu__item kt-menu__item--submenu") {
                                       attributes["data-ktmenu-submenu-toggle"] = "hover"
                                       a(href = "javascript:;", classes = "kt-menu__link kt-menu__toggle") {
                                           i(classes = "kt-menu__link-icon ${Icons.FontAwesome.chart_line}")
                                           span(classes = "kt-menu__link-text") { +"Reports" }
                                           i(classes = "kt-menu__ver-arrow fa fa-chevron-right")
                                       }
                                       div(classes = "kt-menu__submenu ") {
                                           span(classes = "kt-menu__arrow")
                                           ul(classes = "kt-menu__subnav") {
                                               li(classes = "kt-menu__item ") {
                                                   a(href = "/report".withBaseUrl(), classes = "kt-menu__link ") {
                                                       i(classes = "kt-menu__link-bullet kt-menu__link-bullet--line") { span() }
                                                       span(classes = "kt-menu__link-text") { +"Employee Report" }
                                                   }
                                               }
                                               li(classes = "kt-menu__item ") {
                                                   a(href = "/report/monthly".withBaseUrl(), classes = "kt-menu__link ") {
                                                       i(classes = "kt-menu__link-bullet kt-menu__link-bullet--line") { span() }
                                                       span(classes = "kt-menu__link-text") { +"Employee Status" }
                                                   }
                                               }
                                           }
                                       }
                                   }

                                   li(classes = "kt-menu__item  kt-menu__item") {
                                       a(href = "/settings".withBaseUrl(), classes = "kt-menu__link ") {
                                           i(classes = "kt-menu__link-icon ${Icons.FontAwesome.cog}")
                                           span(classes = "kt-menu__link-text") { +"Settings" }
                                       }
                                   }*/


                }
            }
        }
    }

//<!-- end:: Aside Menu -->
}

open class MODAL(initialAttributes: Map<String, String>, override val consumer: TagConsumer<*>) :
    DIV(initialAttributes, consumer) {
    var footer: (DIV.() -> Unit)? = null
    var onFooterPrimaryClick: String? = null
}

enum class ModalSize {
    SMALL, LARGE, FULL
}

fun FlowContent.modal(
    modalId: String,
    title: String,
    primaryButtonText: String? = null,
    primaryButtonId: String? = null,
    modalSize: ModalSize = ModalSize.SMALL,
    extraButtonText: String? = null,
    extraButtonId: String? = null,
    fullHeight: Boolean = false,
    showFooter: Boolean = true,
    block: MODAL.() -> Unit = {}
) {
    val modal = MODAL(emptyMap, consumer)
    div(classes = "modal fade") {
        id = modalId
        tabIndex = "-1"
        role = "dialog"
        attributes.apply {
//            put("aria-labelledby", "exampleModalLabel")
            put("aria-hidden", "true")
        }
        div(classes = "modal-dialog ${if (modalSize == ModalSize.SMALL) "modal-sm" else if (modalSize == ModalSize.LARGE) "modal-lg" else "modal-full"}") {
            role = "document"
            div(classes = "modal-content") {
                if (fullHeight) {
                    style = "height: 100%; overflow: auto;"
                }
                div(classes = "modal-header") {
                    h5(classes = "modal-title") {
//                        id = "exampleModalLabel"
                        +title
                    }
                    button(type = ButtonType.button, classes = "close") {
                        attributes.apply {
                            put("data-dismiss", "modal")
                            put("aria-label", "Close")
                        }
                    }
                }
                div(classes = "modal-body") {
                    /*form {
                        div(classes = "form-group") {
                            label(classes = "form-control-label") {
                                htmlFor = "recipient-name"
                                +"Recipient:"
                            }
                            input(type = InputType.text, classes = "form-control") { id = "recipient-name" }
                        }
                        div(classes = "form-group") {
                            label(classes = "form-control-label") {
                                htmlFor = "message-text"
                                +"Message:"
                            }
                            textArea(classes = "form-control") { id = "message-text" }
                        }
                    }*/
                    block(modal)
                }
                if (showFooter) {
                    div(classes = "modal-footer") {
                        modal.footer?.invoke(this) ?: run {
                            button(type = ButtonType.button, classes = "btn btn-secondary") {
                                attributes["data-dismiss"] = "modal"
                                +"Close"
                            }
                            if (primaryButtonText != null) {
                                button(type = ButtonType.button, classes = "btn btn-primary") {
                                    if (primaryButtonId != null) {
                                        id = primaryButtonId
                                    }
                                    modal.onFooterPrimaryClick?.let { onClick = it }
                                    +primaryButtonText
                                }
                            }

                            if (extraButtonText != null) {
                                button(type = ButtonType.button, classes = "btn btn-info") {
                                    if (extraButtonId != null) {
                                        id = extraButtonId
                                    }
                                    +extraButtonText
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun FlowContent.formGroup(classes: String? = null, block: DIV.() -> Unit = {}) {
    div(classes = "form-group ${classes ?: ""}") {
        block()
    }
}

/*fun FlowContent.formGroupRow(classes: String? = null, groupId: String? = null, block: DIV.() -> Unit = {}) {
    div(classes = "form-group row ${classes ?: ""}") {
        if (groupId != null) {
            id = groupId
        }
        block()
    }
}*/

fun FlowContent.formGroupRow(classes: String? = null, block: FlowContent.() -> Unit = {}) {
    div(classes = "form-group row ${classes ?: ""}") {
        block()
    }
}

fun FlowContent.formLabel(classes: String? = null, htmlFor: String = "", block: LABEL.() -> Unit = {}) {
    label(classes = "form-control-label ${classes ?: ""}") {
        this.htmlFor = htmlFor
        block()
    }
}

fun FlowContent.formInput(
    name: String? = null,
    classes: String? = null,
    type: InputType = InputType.text,
    inputId: String? = null,
    inputDataList: List<String> = emptyList(),
    block: INPUT.() -> Unit = {}
) {
    val uuid = UUID.randomUUID().toString()
    input(name = name, type = type, classes = "form-control ${classes ?: ""}") {
        if (inputId != null) {
            this.id = inputId
        }
        if (inputDataList.isNotEmpty()) {
            list = uuid
        }
        block(this)
    }

    if (inputDataList.isNotEmpty()) {
        dataList {
            id = uuid
            inputDataList.forEach {
                option { value = it }
            }
        }
    }
}

fun FlowContent.phoneNumberInputWithLabel(
    label: String,
    inputName: String? = null,
    prependText: String? = "05",
    inputMaxLength: Int = 9
) {
    formInputWithLabel(
        label,
        inputClasses = "hide-number-input-arrows",
        type = InputType.number,
        prependText = prependText
    ) {
        maxLength = "$inputMaxLength"
        onInput =
            "javascript: if (this.value.length > this.maxLength && this.maxLength > 0) this.value = this.value.slice(0, this.maxLength);"
        if (inputName != null) {
            name = inputName
        }
    }
}

fun FlowContent.formInputWithLabel(
    label: String,
    name: String? = null,
    inputClasses: String? = null,
    labelClasses: String? = null,
    type: InputType = InputType.text,
    prependText: String? = null,
    dataList: List<String> = emptyList(),
    labelId: String? = null,
    block: INPUT.() -> Unit = {}
) {
    formLabel(classes = labelClasses, htmlFor = name ?: "") {
        if (labelId != null) {
            id = "stock_quantity_place_holder"
        }
        +label
    }
    if (prependText != null) {
        div("input-group") {
            div("input-group-prepend") {
                span("input-group-text") {
                    +prependText
                }
            }
            formInput(name, inputClasses, type, inputDataList = dataList) {
                block()
            }
        }
    } else {
        formInput(name, inputClasses, type, inputDataList = dataList) {
            block()
        }
    }
}

fun FlowContent.formGroupLabel(title: String, block: DIV.() -> Unit = {}) {
    formGroup {
        formLabel {
            +title
        }
        block()
    }
}

fun FlowContent.portlet(
    title: String? = null,
    classes: String? = null,
    headToolbar: (DIV.() -> Unit)? = null,
    portletId: String? = null,
    portletStyle: String? = null,
    block: DIV.() -> Unit = {}
) {
    div(classes = "kt-portlet kt-portlet--mobile ${classes ?: ""}") {
        if (portletStyle != null) {
            style = portletStyle
        }
        if (portletId != null) {
            id = portletId
        }
        title?.let {
            div(classes = "kt-portlet__head") {
                div(classes = "kt-portlet__head-label") {
                    h3(classes = "kt-portlet__head-title") {
                        +it
                    }
                }
                if (headToolbar != null) {
                    div("kt-portlet__head-toolbar") {
                        headToolbar.invoke(this)
                    }
                }
            }
        }
        div(classes = "kt-portlet__body") {
            block()
        }
    }
}

fun FlowContent.ktSelectPicker(
    placeHolder: String? = null,
    liveSearch: Boolean = false,
    multipleSelect: Boolean = false,
    onChangeFunc: String? = null,
    selectClasses: String? = null,
    block: SELECT.() -> Unit
) {
    select(classes = "kt-selectpicker form-control ${selectClasses ?: ""}") {
        //        style = "display:block;"
        multiple = multipleSelect
        if (placeHolder != null) {
            title = placeHolder
        }
        if (onChangeFunc != null) {
            onChange = onChangeFunc
        }
        if (liveSearch) {
            attributes["data-live-search"] = "true"
        }
        block()
    }
}

fun FlowContent.ktSelect2(
    placeHolder: String? = null,
    multipleSelect: Boolean = false,
    onChangeFunc: String? = null,
    selectClasses: String? = null,
    url: String? = null,
    tags: Boolean = false,
    templateResult: String? = null,
    templateSelection: String? = null,
    apiDataCallback: String? = null,
    block: SELECT.() -> Unit = {}
) {
    select(classes = "form-control kt-select2 ${selectClasses ?: ""}") {
        style = "width:100%, padding-top:.5rem"
        //        style = "display:block;"
        multiple = multipleSelect
        if (placeHolder != null) {
            attributes["data-placeholder"] = placeHolder
        }
        if (onChangeFunc != null) {
            onChange = onChangeFunc
        }
//        attributes["data-live-search"] = liveSearch.toString()
        if (url.isNullOrBlank().not()) {
            attributes["data-ajax--url"] = url ?: ""
            attributes["data-ajax--delay"] = "500"
            attributes["data-ajax--dataType"] = "json"
            attributes["data-ajax--dataType"] = "json"
            if (apiDataCallback != null) {
                attributes["data-ajax-callback"] = apiDataCallback
            }
            attributes["data-minimum-input-length"] = "2"
            attributes["data-tags"] = "true"
        }
        if (tags) {
            attributes["data-tags"] = "true"
        }
        if (templateResult.isNullOrBlank().not()) {
            attributes["data-template-parser"] = templateResult!!
        }
        if (templateSelection.isNullOrBlank().not()) {
            attributes["data-template-view"] = templateSelection!!
        }
        block()
    }
}

fun FlowContent.inlineRadioGroupWithLabel(
    label: String,
    inputName: String?,
    values: List<String>,
    checkedValue: String? = null,
    onChangeFunc: String? = null,
    labelStyle: String? = null,
    formGroupStyle: String? = null
) {
    formGroup {
        if (formGroupStyle != null) {
            style = formGroupStyle
        }
        label {
            if (labelStyle != null) {
                style = labelStyle
            }
            +label
        }
        inlineRadioGroup(values, inputName, checkedValue, onChangeFunc)
    }
}

fun FlowContent.inlineRadioGroup(
    values: List<String>,
    inputName: String? = null,
    checkedValue: String? = null,
    onChangeFunc: String? = null
) {
    div("kt-radio-inline") {
        values.forEach {
            label("kt-radio") {
                input {
                    type = InputType.radio
                    if (inputName != null) {
                        name = inputName
                    }
                    if (onChangeFunc != null) {
                        onChange = onChangeFunc
                    }
                    if (checkedValue != null && checkedValue == it) {
                        checked = true
                    }
                    value = it
                }
                +it
                span { }
            }
        }
    }
}

fun FlowContent.inlineCheckBox(
    values: List<Pair<String, String>>
) {
    div("kt-checkbox-inline") {
        values.forEach {
            label("kt-checkbox") {
                input {
                    type = InputType.checkBox
                    name = it.second
                    value = "on"
                }
                +it.first
                span { }
            }
        }
    }
}

fun FlowContent.inlineCheckBoxWithLabel(
    label: String,
    values: List<Pair<String, String>>,
    labelStyle: String? = null,
    formGroupStyle: String? = null
) {
    formGroup {
        if (formGroupStyle != null) {
            style = formGroupStyle
        }
        label {
            if (labelStyle != null) {
                style = labelStyle
            }
            +label
        }
        inlineCheckBox(values)
    }
}

fun HEAD.headerStylesheets() {
    css(href = "/assets/css/main.css")
    css(href = "https://cdnjs.cloudflare.com/ajax/libs/jquery.perfect-scrollbar/1.4.0/css/perfect-scrollbar.min.css")
    css(href = "https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.11/css/bootstrap-select.min.css")
    css(href = "https://cdnjs.cloudflare.com/ajax/libs/bootstrap-switch/3.3.4/css/bootstrap3/bootstrap-switch.min.css")
    css(href = "https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.13/css/select2.min.css")
    css(href = "https://cdnjs.cloudflare.com/ajax/libs/animate.css/3.7.2/animate.min.css")
    css(href = "/assets/plugins/general/socicon/css/socicon.css")
    css(href = "/assets/plugins/general/plugins/line-awesome/css/line-awesome.css")
    css(href = "/assets/plugins/general/plugins/flaticon/flaticon.css")
    css(href = "/assets/plugins/general/plugins/flaticon2/flaticon.css")
    css(href = "/assets/plugins/general/@fortawesome/fontawesome-free/css/all.min.css")

    //<!--end:: Vendor Plugins -->

    css(href = "/assets/css/style.bundle.min.css")

    //<!--begin:: Vendor Plugins for custom pages -->
    css(href = "https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.css")
    css(href = "https://cdn.datatables.net/1.10.20/css/dataTables.bootstrap4.min.css")
    css(href = "https://cdn.datatables.net/buttons/1.6.0/css/buttons.bootstrap4.min.css")
    css(href = "https://cdn.datatables.net/responsive/2.2.3/css/responsive.bootstrap4.min.css")
    css(href = "https://transloadit.edgly.net/releases/uppy/v1.5.2/uppy.min.css")
    css(href = "https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css")
    css(href = "https://cdnjs.cloudflare.com/ajax/libs/image-picker/0.3.1/image-picker.min.css")
    css(href = "https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/css/bootstrap-select.min.css")

    unsafe {
        +"""<style>
            
            |.red { background-color: red; color: white;}
            |.dataTables_filter{display: inline-block;float:right;}
            |.portlet-absolute{position: absolute}
            |/*.form-control{min-width:230px;}*/ .bootstrap-select{display:block !important;}
            |.tether-element, .tether-element:after, .tether-element:before, .tether-element *, .tether-element *:after, .tether-element *:before {
  box-sizing: border-box; }

.tether-element {
  position: absolute;
  display: none; }
  .tether-element.tether-open {
    display: block; }
.bootstrap-select {
|width: 100% !important;
|}
</style>""".trimMargin()
    }
}

fun FlowOrMetaDataOrPhrasingContent.css(href: String) {
    link(
        href = (if (href.startsWith("assets")) "/$href" else href).let {
            if (it.startsWith("/assets")) {
                it.withBaseUrl()
            } else {
                it
            }
        },
        rel = ARel.stylesheet,
        type = LinkType.textCss
    )
}

fun FlowOrMetaDataOrPhrasingContent.jscript(src: String) {
    script(
        src = (if (src.startsWith("assets")) "/$src" else src).let {
            if (it.startsWith("/assets")) {
                it.withBaseUrl()
            } else {
                it
            }
        },
        type = LinkType.textJavaScript
    ) {}
}

fun FlowContent.ktWidget(topBlock: (FlowContent.() -> Unit)? = null, bottomBlock: (FlowContent.() -> Unit)? = null) {
    div(classes = "kt-widget kt-widget--user-profile-3") {
        if (topBlock != null) {
            div(classes = "kt-widget__top") {
                topBlock.invoke(this)
            }
        }

        if (bottomBlock != null) {
            div(classes = "kt-widget__bottom") {
                bottomBlock.invoke(this)
            }
        }
    }
}

fun FlowContent.formSelectWithLabel(
    label: String,
    selectClasses: String? = null,
    labelClasses: String? = null,
    block: SELECT.() -> Unit = {}
) {

    formGroup {
        formLabel(classes = labelClasses) {
            +label
        }
        select(classes = "form-control ${selectClasses ?: ""}") {
            block()
        }
    }
}

fun HTMLTag.nbsp() {
    unsafe { +"&nbsp;" }
}

fun FlowContent.modalLoader(loadingText: String = "Espere por favor...") {
    div(classes = "modal fade") {
        style = "padding-top:100px;"
        id = "loaderModal"
        role = "dialog"

        div(classes = "modal-dialog") {
            style = "width:75%; margin:auto;"
            div(classes = "progress-area")
        }
        p {
            style = "color: #ffb700; text-align:center;"
            +loadingText
        }
    }
}

class DataColumn(
    val title: String,
    val key: String,
    val template: String? = null,
    val colSpan: Int = 0,
    val colClass: String? = null,
    val visible: Boolean = true,
    val sortAble: Boolean = true,
    val detailColumn: Boolean = false
)

class DataTableColumn(
    val title: String,
    val template: String? = null,
    val colSpan: Int = 0,
    val colClass: String? = null,
    val visible: Boolean = true,
    val sortAble: Boolean = true,
    val detailColumn: Boolean = false
)

class TableButton(
    val extend: String,
    var exportColumnIndices: List<Int>? = null,
    var customize: String? = null
) {
    companion object {
        val PRINT = TableButton("print")
        val PDF = TableButton("pdf")
        val EXCEL = TableButton("excel")
        val COLVIS = TableButton("colvis")

        val DEFAULT_LIST = listOf(PRINT, EXCEL, PDF, COLVIS)
    }
}

fun FlowContent.metroButton(
    title: String? = null,
    icon: String? = null,
    modalTarget: String? = null,
    onclick: String? = null,
    buttonStyle: String? = null,
    buttonId: String? = null,
    outlined: Boolean = false
) {
    div(classes = "dropdown dropdown-inline") {
        if (buttonId != null) {
            id = buttonId
        }
        if (buttonStyle != null) {
            style = buttonStyle
        }
        button(
            type = ButtonType.button,
            classes = "btn ${if (outlined) "btn-outline-brand" else "btn-brand"} btn-icon-sm"
        ) {
            onclick?.let {
                this.onClick = it
            }

            modalTarget?.let {
                attributes.apply {
                    put("data-toggle", "modal")
                    put("data-target", "#$it")
                }
            }
            //    data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"
//                            data-toggle="modal" data-target="#kt_modal_5"
            icon?.let {
                i(classes = it) {
                    if (title.isNullOrEmpty()) {
                        style = "padding:0px;"
                    }
                }
            }
            title?.let {
                +it
            }
        }
    }
}

class AjaxDataSource(
    val tableId: String,
    val url: String,
    val columns: List<DataColumn>,
    val type: String = "GET",
    val topHeaderColumns: List<DataColumn>? = null,
    val buttons: List<TableButton>? = null,
    val ignoreLastColumnForButtons: Boolean = false,
    val searching: Boolean = true,
    val pageLength: Int = 50
)

fun FlowContent.ajaxDataTable(
    dataSource: AjaxDataSource,
    dataTableId: String = "datatable",
    exportOnlyVisible: Boolean = false,
    serverSideProcess: Boolean = false,
    rowReorder: Boolean = false,
    apiCallback: String? = null,
    rowCallback: String? = null,
    orderColumnIndex: Int? = null,
    sortBy: String? = "desc"
) {
    table(classes = "table table-striped- table-bordered compact") {
        id = dataSource.tableId
        thead {
            if (dataSource.topHeaderColumns?.isNotEmpty() == true) {
                tr {
                    dataSource.topHeaderColumns.forEach {
                        th {
                            if (it.colSpan > 0) {
                                style = "text-align:center;"
                                colSpan = it.colSpan.toString()
                            }
                            +it.title
                        }
                    }
                }
            }

            tr {
                dataSource.columns.forEach {
                    th { +it.title }
                }
            }
        }
    }
    var columnsVisibleScript = ""
    val notOrderableColumnsScript =
        dataSource.columns.mapIndexedNotNull { index, item -> if (!item.sortAble) index else null }.let {
            if (it.isNotEmpty()) {
                "{orderable: false, targets: [${it.joinToString(",")}]},"
            } else ""
        }
    var columnsDefScript = ""
    var columnsScript = ""


    val notVisibleColumnIds = mutableListOf<Int>()
    val orderColumn = orderColumnIndex ?: dataSource.columns.indexOfFirst { !it.detailColumn }
    dataSource.columns.forEachIndexed { index, triple ->
        if (columnsScript.isNotBlank()) {
            columnsScript += ","
        }
        if (!triple.visible) {
            notVisibleColumnIds.add(index)
        }
        if (triple.detailColumn) {
            columnsScript += """{
                "className":      'details-control',
                "orderable":      false,
                "data":           null,
                "defaultContent": ''
            }"""
        } else {
            columnsScript += "{data: '${triple.key}'}"
            val item = """
                    {
                        targets: $index,
                        render: function(data, type, row, meta){
                            ${if (!triple.template.isNullOrBlank()) triple.template else "return typeof data != 'undefined' ? data : '';"}
                        },
                        ${if (!triple.colClass.isNullOrBlank()) "className: '${triple.colClass}'" else ""}
                    }
                """.trimIndent()
            if (columnsDefScript.isNotBlank()) {
                columnsDefScript += ","
            }
            columnsDefScript += item
        }
    }

    if (notVisibleColumnIds.isNotEmpty()) {
        columnsVisibleScript = "{ visible: false, targets: [ ${notVisibleColumnIds.joinToString(",")} ] },"
    }

    var buttonsScript: String = ""
    val buttonColumns =
        dataSource.columns.mapIndexedNotNull { index, dataColumn -> if (dataSource.ignoreLastColumnForButtons && index == dataSource.columns.size - 1) null else index }
            .joinToString(",")
    dataSource.buttons?.forEach {
        val columnsIndices = if (it.exportColumnIndices == null) {
            buttonColumns
        } else {
            it.exportColumnIndices?.joinToString(",")
        }

        val item = """
                    {
                        extend: "${it.extend}",
                        exportOptions: {
                            columns: ${if (exportOnlyVisible) "':visible(:not(.not-export-col))'" else "[$columnsIndices]"}
                        },
                        ${if (Env.DEBUG) "autoPrint: false," else ""}
                        ${
            if (!it.customize.isNullOrBlank()) {
                "customize: function ( win ) {${it.customize}}"
            } else {
                ""
            }
        }
                    }
                """.trimIndent()
        if (buttonsScript.isNotBlank()) {
            buttonsScript += ","
        }
        buttonsScript += item
    }

    val script = """
            var $dataTableId = $('#${dataSource.tableId}').DataTable({
                ${if (dataSource.buttons != null) "dom: 'iBlfrtip'," else ""}
                ${if (rowReorder) "rowReorder: true," else ""}
                ${if (rowReorder.not()) """"order": [[$orderColumn, "$sortBy" ]],""" else "ordering: false,"}
                processing: true,
                ${if (serverSideProcess) "searchDelay: 700,serverSide: true,\"language\": {processing: '<i class=\"fa fa-spinner fa-spin fa-3x fa-fw\"></i><span class=\"sr-only\">Loading..n.</span> '}," else ""}
             
                lengthMenu: [[10, 25, 50 -1], [10, 25, 50, "All"]],
                responsive: true,
//                "dom": '<"top"iflp<"clear">>rt<"bottom"iflp<"clear">>',
//                "dom": '<"top"Bl<"clear">f><"clear">rt<"bottom"i<"clear">p>',
                searching: ${dataSource.searching},
                pageLength: ${dataSource.pageLength},
                ${if (rowCallback.isNullOrBlank()) "" else "createdRow: typeof $rowCallback != 'undefined' ? $rowCallback : null,"}
                ajax: {
                    url: '${dataSource.url}',
                    type: '${dataSource.type}',
                    ${
        if (apiCallback.isNullOrBlank()
                .not()
        ) "data: function ( d ) {if(typeof $apiCallback == 'function'){$apiCallback(d);}}" else ""
    }
			    },
                columns: [
                    $columnsScript
                ],
                columnDefs: [
                ${if (notOrderableColumnsScript.isNotBlank()) notOrderableColumnsScript else ""}
                ${if (columnsVisibleScript.isNotBlank()) columnsVisibleScript else ""}
                $columnsDefScript
                ],
                buttons:[
                    $buttonsScript
                ]
            });
        """.trimIndent()

    script {
        unsafe {
            +script
        }
    }
}


fun getFileTypeSvg(url: String): String {
    return if (url.lowercase().contains(".png") || url.lowercase().contains(".gif") || url.lowercase()
            .contains(".jpg") || url.lowercase()
            .contains(
                ".jpeg"
            )
    ) {
        url;
    } else if (url.lowercase().contains(".pdf")) {
        "/assets/media/files/pdf.svg";
    } else if (url.lowercase().contains(".doc")) {
        "/assets/media/files/doc.svg";
    } else {
        "/assets/media/icons/svg/Files/File.svg";
    }
}

fun getActionHTMLStr(
    title: String,
    icon: String? = null,
    onClickFunc: String? = null,
    dataTarget: String? = null
): String {
    return createHTML().a(classes = "btn btn-sm btn-clean btn-icon btn-icon-sm") {
        attributes["title"] = title
        if (dataTarget != null) {
            attributes["data-toggle"] = "modal"
            attributes["data-target"] = dataTarget
        }
        if (onClickFunc != null) {
            onClick = onClickFunc
        }
        if (icon != null) {
            i(classes = icon)
        }
    }.toString()
}

data class ActionMenuItem(
    val title: String,
    val icon: String? = null,
    val onClickFunc: String? = null,
    val href: String? = null,
    val dataTarget: String? = null,
    val target: String? = null
)

fun createActionList(items: List<ActionMenuItem>): String {
    return createHTML().span(classes = "dropdown") {
        a(classes = "btn btn-sm btn-clean btn-icon btn-icon-md") {
            href = "#"
            attributes["data-toggle"] = "dropdown"
            attributes["aria-expanded"] = "true"
            i(classes = "la la-ellipsis-h")
        }
        div(classes = "dropdown-menu dropdown-menu-right") {
            items.forEach {
                a(classes = "dropdown-item") {
                    if (it.onClickFunc != null) {
                        onClick = it.onClickFunc
                    }
                    if (it.href != null) {
                        href = it.href
                    }
                    if (it.target != null) {
                        target = it.target
                    }
                    if (it.dataTarget != null) {
                        attributes["data-toggle"] = "modal"
                        attributes["data-target"] = it.dataTarget
                    }
                    if (it.icon != null) {
                        i(classes = it.icon)
                    }
                    +it.title
                }
            }
        }
    }
}