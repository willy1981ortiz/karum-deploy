package com.plugins

import com.frontend.controller.ApiController
import com.frontend.controller.PageController
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Routing.backendPageApis() {
    route("api") {
        //   api/otp
        post("otp") {
            ApiController(call).otp()
        }

        // api/verify/otp
        post("/verify/otp") {
            ApiController(call).verifyOtp()
        }

        post("ocr") {
            ApiController(call).ocr()
        }

        get("curp/validate") {
            ApiController(call).validate()
        }

        post("geoData/get") {
            ApiController(call).getGeoDataByCPCode()
        }

        authenticate("customerAuth") {
            post("password/update") {
                //WebApiController(call).updatePassword()
            }

            post("ine/upload") {
                ApiController(call).ineUpload()
            }

            post("passport/upload") {
                ApiController(call).passportNumberUpload()
            }

            post("tc41/submit") {
                ApiController(call).tc41FormSubmit()
            }

            post("tc41/otpSend") {
                ApiController(call).tc41OTPSend()
            }

            post("tc41/resubmit") {
                ApiController(call).tc41ResendOtp()
            }

            post("tc42/otp") {
                ApiController(call).tc42OtpFormSubmit()
            }

            post("tc42/submit") {
                ApiController(call).tc42FormSubmit()
            }

            post("mobileData/submit") {
                ApiController(call).mobileDataFormSubmit()
            }

            /*post("tc43/submit") {
                ApiController(call).tc43Submit()
            }

            post("tc44/submit") {
                ApiController(call).tc44Submit()
            }*/

            post("data/complete") {
                ApiController(call).dataComplete()
            }

            post("shippingAddress/submit") {
                ApiController(call).shippingAddressSubmit()
            }

            post("complements/submit") {
                ApiController(call).complementsSubmit("", "")
            }

            post("product/submit") {
                ApiController(call).productSubmit()
            }

            get("/user/session") {
                ApiController(call).getUserSession()
            }

            get("mobile/flag/handoff") {
                ApiController(call).isMobileSessionCreated()
            }

            post("summary/set") {
                ApiController(call).storeSummaryData()
            }

            post("user/statusUpdate") {
                ApiController(call).updateUserStatusToPoa()
            }
        }
    }
}

fun generateOTP(): String {
    val randomPin = (Math.random() * 900000).toInt() + 100000
    return randomPin.toString()
}