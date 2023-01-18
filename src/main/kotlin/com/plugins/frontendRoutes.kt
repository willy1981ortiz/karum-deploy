package com.plugins

import com.ServerSession
import com.frontend.controller.AuthPageController
import com.frontend.controller.DocumentsPageController
import com.frontend.controller.InfoPageController
import com.frontend.controller.PageController
import com.helper.withBaseUrl
import com.mActiveRoute
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Routing.frontendRoutes() {
    get("/logout") {
        call.sessions.clear<ServerSession>()
        call.respondRedirect("/login".withBaseUrl())
    }

    get("/splash") {
        PageController(call).landingPage()
    }
    get("/") {
        PageController(call).landingPage()
    }

    get("/landing") {
        PageController(call).landingPage()
    }

    get("/login") {
        AuthPageController(call).loginPage()
    }

    get("/login/otp") {
        AuthPageController(call).loginOtpPage()
    }

    get("/handoff") {
        AuthPageController(call).handoff()
    }

    authenticate("customerAuth") {
        get("/welcome") {
            PageController(call).welcomePage()
        }

        get("/document") {
            PageController(call).documentCheckPage()
        }

        get("/documentPreview") {
            PageController(call).documentPreviewPage()
        }

        get("/identification") {
            DocumentsPageController(call).identificationPage()
        }

        get("/capture") {
            DocumentsPageController(call).captureImageWithCameraPage()
        }

        get ("/uploadDocument"){
            DocumentsPageController(call).uploadDocumentPage()
        }

        get ("/upload/document/instructions"){
            DocumentsPageController(call).documentUploadInstructionsPage()
        }

        get("/upload/document") {
            DocumentsPageController(call).dragAndDropUploadPage()
        }

        get("/person_info") {
            InfoPageController(call).personalInfoPage()
        }

        get("/info_view") {
            InfoPageController(call).infoDataViewPage()
        }

        get("/pre_clarification") {
            PageController(call).calificationPage()
        }

        get("/supplementaryData") {
            InfoPageController(call).referenceSummaryPage()
        }

        get("/mobileData") {
            InfoPageController(call).mobileDataPage()
        }

        get("/declaration") {
            PageController(call).declarationPage()
        }

        get("/goodBye") {
            PageController(call).goodByePage()
        }

        get("/goodByeMobile") {
            DocumentsPageController(call).goodByeMobilePage()
        }
    }


    get("/infoSummary") {
        InfoPageController(call).personalInfoPage()
    }

    /*get("/economicData") {
        PageController(call).economicDataPage()
    }

    get("/familyReference") {
        InfoPageController(call).familyReferencePage()
    }

    get("/personalReference") {
        PageController(call).personalReferencePage()
    }

    get("/referenceSummary") {
        InfoPageController(call).referenceSummaryPage()
    }*/
}