package com.frontend.controller

import com.Env
import com.api.KarumApi
import com.controller.Controller
import com.data.Estado
import com.enums.Tc41Alert
import com.helper.*
import com.model.DocumentModel
import com.model.PersonInfoModel
import com.model.UserModel
import com.plugins.ProductSession
import com.plugins.UserSession
import com.plugins.generateOTP
import com.plugins.userSession
import com.tables.pojos.User
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.sessions.*
import java.io.File
import java.util.*

class ApiController(call: ApplicationCall) : Controller(call) {

    /*
        This method receive curp in parameter and validate this received curp
     */
    suspend fun validate() {
        // receiver parameter in side api
        val curp = call.parameters["curp"] ?: error("invalid")
        // call this method to validate curp and send response in api
        val response = CurpHelper.validate(curp)
        success(data = response)
    }

    /*
       This method receive cp code as parameter, then invoke geo data api get zip code information
    */
    suspend fun getGeoDataByCPCode() {
        val param = call.receiveSafeParameters()
        val zipCode = param["cp_code"] ?: error("enter valid cp code!")
        // invoke this method to get geo date api response
        val response = KarumApi.getGeoData(zipCode)
        // send geo data api response to required page
        success(data = response)
    }

    suspend fun tc41FormSubmit() {
        val userSession = call.userSession
        val user = UserModel.getUser(userSession.mobile) ?: error("Invalid session!")
        val params = call.receiveSafeParameters()
        val parentSurname =
            params["parent_surname"]?.takeIf { it.isNotBlank() } ?: error("apellido de los padres requerido")
        val motherSurname = params["mother_surname"]?.takeIf { it.isNotBlank() } ?: error("apellido materno requerido")
        val name = params["name"]?.takeIf { it.isNotBlank() } ?: error("Nombre requerido")
        val stateOfBirth = params["state_birth"] ?: error("Estado de nacimiento requerido")
        val year = params["year"]?.toIntOrNull() ?: error("fecha de nacimiento requerida")
        val month = params["month"]?.toIntOrNull() ?: error("fecha de nacimiento requerida")
        val day = params["day"]?.toIntOrNull() ?: error("fecha de nacimiento requerida")
        val gender = params["gender"] ?: error("género requerido")
        val curp = params["curp"]?.takeIf { it.isNotBlank() } ?: error("curp requerido")
        val ine = params["ine"]?.takeIf { it.isNotBlank() } ?: error("ine requerido")
        val street = params["street"]?.takeIf { it.isNotBlank() } ?: error("Calle requerido")
        val extNo = params["ext_no"]?.takeIf { it.isNotBlank() } ?: error("No Ext requerido")
        val intNo = params["int_no"]
        val zipCode = params["zip"]?.takeIf { it.isNotBlank() } ?: error("C.P requerido")
        val state = params["state"]?.takeIf { it.isNotBlank() } ?: error("Estado requerido")
        val stateCode = params["state_code"]?.takeIf { it.isNotBlank() } ?: error("Estado requerido")
        val municipality = params["municipality"]?.takeIf { it.isNotBlank() } ?: error("Alcaldia o Municipio requerido")
        val municipalityCode =
            params["municipality_code"]?.takeIf { it.isNotBlank() } ?: error("Alcaldia o Municipio requerido")
        val colony = params["colony"]?.takeIf { it.isNotBlank() } ?: error("Colonia requerido")
        val colonyCode = params["colony_code"]?.takeIf { it.isNotBlank() } ?: error("Colonia requerido")
        val city = params["city"]?.takeIf { it.isNotBlank() }
        val cityCode = params["city_code"]?.takeIf { it.isNotBlank() }
        val telephone = params["telephone"]?.takeIf { it.isNotBlank() } ?: error("Telefono Celular requerido")
        val email = params["email"] ?: error("Email requerido")
        val companyName = params["company_name"] ?: error("Nombre da la Empresa requerido")
        val companyPhone = params["company_phone"] ?: error("Telefono da la Empressa: requerido")
        val monthlyIncome = params["monthly_income"] ?: error("Ingreso Mensual Bruto: requerido")

//        val address = "$street $extNo $intNo" //(combination of Calle+No Ext+No Int)

//        val dob = day.let { if (it < 10) "0$it" else "$it" }.plus(month.let { if (it < 10) "0$it" else "$it" }).plus(year)

//        val isRegistered = PersonInfoModel.isTelephoneRegistered(telephone)
//        if (isRegistered) error("Este número fue registrado previamente")

        val tc41Data = PersonInfoModel.getPersonInfoData(userSession.userId)

        var alert = Tc41Alert.N
        if (tc41Data?.name != null && name != tc41Data.name) alert = Tc41Alert.Y
        if (tc41Data?.parentSurname != null && parentSurname != tc41Data.parentSurname) alert = Tc41Alert.Y
        if (tc41Data?.motherSurname != null && motherSurname != tc41Data.motherSurname) alert = Tc41Alert.Y
        if (tc41Data?.stateOfBirth != null && stateOfBirth != tc41Data.stateOfBirth) alert = Tc41Alert.Y
        if (tc41Data?.gender != null && gender != tc41Data.gender) alert = Tc41Alert.Y
        if (tc41Data?.year != null && year != tc41Data.year) alert = Tc41Alert.Y
        if (tc41Data?.month != null && month != tc41Data.month) alert = Tc41Alert.Y
        if (tc41Data?.day != null && day != tc41Data.day) alert = Tc41Alert.Y

        val personInfoAdd = PersonInfoModel.addPersonInfoData(
            name,
            parentSurname,
            motherSurname,
            year,
            month,
            day,
            curp,
            ine,
            zipCode,
            street,
            extNo,
            intNo,
            state,
            stateCode,      //state SEPOMEXCode,
            municipality,
            municipalityCode,       //municipality SEPOMEXCode,
            colony,
            colonyCode,         //colony SEPOMEXCode,
            city,
            cityCode,           //city SEPOMEXCode,
            telephone,
            companyName,
            companyPhone,
            monthlyIncome,
            gender,
            email,
            stateOfBirth,
            alert,
            user.userId
        )

        if (personInfoAdd > 0) {
            if (user.status <= UserModel.SUMMARY_1_COMPLETE) {
                UserModel.updateUserStatus(UserModel.SUMMARY_1_COMPLETE, user.userId)
            }
            UserModel.updateUserCurp(curp, null, userSession.userId)
        }

        success()
    }

    /*
       This method invoke to send otp token to user on login
    */
    suspend fun tc41OTPSend() {
        // get current user session
        val user = call.userSession
        // get user personal info using current session of user
        val infoData = PersonInfoModel.getPersonInfoData(user.userId) ?: error("Por favor complete los datos primero")
        // modify date of birth to desired format
        val dob = infoData.day.let { if (it < 10) "0$it" else "$it" }
            .plus(infoData.month.let { if (it < 10) "0$it" else "$it" }).plus(infoData.year)

        // (Combination of Calle+NoExt+NoInt)
        val address = if (infoData.intNo != null) {
            infoData.street + " " + infoData.extNo + " " + infoData.intNo
        } else {
            infoData.street + " " + infoData.extNo
        }

        //Api DatosTC41 invoke using this method and get response
        val response = KarumApi.submitTc41Form(
            infoData.name,
            infoData.parentSurname,
            infoData.motherSurname,
            dob,
            infoData.curpNo,
            infoData.zipCode,
            address,
            infoData.state,
            infoData.stateCode,
            infoData.municipality,
            infoData.municipalityCode,
            infoData.colony,
            infoData.colonyCode,
            infoData.city ?: "",              // City is optional
            infoData.cityCode ?: "",          // city is optional
            infoData.telephone
        )

        if (response?.isSuccess() != true || response.message.isNullOrBlank()) {
            error(response?.message ?: "karum api error")
        } else {
            PersonInfoModel.updateApplicationId(response.message, user.userId)
            UserModel.updateUserStatus(UserModel.TC41_API_COMPLETE, user.userId)
            success(response.message)
        }
    }

    /*
       use this function to invoke Resend Otp api
    */
    suspend fun tc41ResendOtp() {
        val user = call.userSession
        val personModel =
            PersonInfoModel.getPersonInfoData(user.userId) ?: error("Por favor complete los datos primero")
        // modify date of birth to desired format
        val dob = personModel.day.let { if (it < 10) "0$it" else "$it" }
            .plus(personModel.month.let { if (it < 10) "0$it" else "$it" }).plus(personModel.year)
//        val address = personModel.street + personModel.extNo + personModel.intNo

        // (Combination of Calle+NoExt+NoInt)
        val address = if (personModel.intNo != null) {
            personModel.street + " " + personModel.extNo + " " + personModel.intNo
        } else {
            personModel.street + " " + personModel.extNo
        }

        val response = KarumApi.submitTc41Form(
            personModel.name,
            personModel.parentSurname,
            personModel.motherSurname,
            dob,
            personModel.curpNo,
            personModel.zipCode,
            address,
            personModel.state,
            personModel.stateCode,
            personModel.municipality,
            personModel.municipalityCode,
            personModel.colony,
            personModel.colonyCode,
            personModel.city ?: "",         // City is optional
            personModel.cityCode ?: "",
            personModel.telephone,
        )

        if (response?.isSuccess() != true || response.message.isNullOrBlank()) {
            error(response?.message ?: "karum api error")
        } else {
            success(response.message)
        }
    }

    /*
        use this function to invoke tc42 api
    */
    suspend fun tc42OtpFormSubmit() {
        // get user current session
        val userSession = call.userSession
        // validate user session with db users
        val user = UserModel.getUser(userSession.mobile) ?: error("Invalid session")
        // receive required parameters like auth_code
        val params = call.receiveSafeParameters()
        val otpCode = params["auth_code"]?.takeIf { it.isNotBlank() } ?: error("NIP requerido")
//        val tc41Session = call.sessions.get<Tc41Session>()
        val tc41Data = PersonInfoModel.getPersonInfoData(userSession.userId) ?: error("invalid session")

        // invoke tc42 api and get response
        val response = KarumApi.submitTC42Form(
            tc41Data.applicationid,
            tc41Data.curpNo,
            tc41Data.ine,
            otpCode,
            tc41Data.companyName,
            tc41Data.companyPhone,
            tc41Data.monthlyIncome
        )
        /*if (response?.error == "Tramite declinado, ofrece al cliente las promociones vigentes.") {
            UserModel.updateUserToDecline(userSession.userId)
        }*/
        if (response?.isSuccess() == true) {
            if (user.status <= UserModel.AUTH_CODE_TC42_ACCEPTED) {
                UserModel.updateUserStatus(UserModel.AUTH_CODE_TC42_ACCEPTED, userSession.userId)
            }
            success("done")
        } else {
            error(response?.error ?: response?.message ?: "karum API error!")
        }
    }

    suspend fun tc42FormSubmit() {
        val userSession = call.userSession
        val user = UserModel.getUser(userSession.mobile) ?: error("Invalid session")
        val params = call.receiveSafeParameters()
        val profession = params["profession"]?.takeIf { it.isNotBlank() } ?: error("Profesion requerido")
        val homePhone = params["home_phone"]?.takeIf { it.isNotBlank() } ?: error("Telefono de Casa requerido")
        val supYear = params["supYear"]?.toIntOrNull() ?: error("Antiguedad Domicilio Anos requerida")
        val supMonth = params["supMonth"]?.toIntOrNull() ?: error("Antiguedad Domicilio Mesos requerida")
        val street = params["street"]?.takeIf { it.isNotBlank() } ?: error("Calle requerido")
        val extNo = params["ext_no"]?.takeIf { it.isNotBlank() } ?: error("No Ext requerido")
        val intNo = params["int_no"]
        val zipCode = params["zip"]?.takeIf { it.isNotBlank() } ?: error("C.P requerido")
        val state = params["state"]?.takeIf { it.isNotBlank() } ?: error("Estado requerido")
        val stateCode = params["state_code"]?.takeIf { it.isNotBlank() } ?: error("Estado requerido")
        val municipality = params["municipality"]?.takeIf { it.isNotBlank() } ?: error("Alcaldia o Municipio requerido")
        val municipalityCode =
            params["municipality_code"]?.takeIf { it.isNotBlank() } ?: error("Alcaldia o Municipio requerido")
        val colony = params["colony"]?.takeIf { it.isNotBlank() } ?: error("Colonia requerido")
        val colonyCode = params["colony_code"]?.takeIf { it.isNotBlank() } ?: error("Colonia requerido")
        val city = params["city"] ?: ""
        val cityCode = params["city_code"]?.takeIf { it.isNotBlank() }
        val labYear = params["labYear"]?.takeIf { it.isNotBlank() }?.toIntOrNull() ?: error("Antiguedad requerido")
        val labMonth = params["labMonth"]?.takeIf { it.isNotBlank() }?.toIntOrNull() ?: error("Antiguedad requerido")
        val giro = params["giro"]?.takeIf { it.isNotBlank() } ?: error("Giro requerido")
        val occupation = params["occupation"]?.takeIf { it.isNotBlank() } ?: error("Ocupación requerido")
        val familyName1 =
            params["name_reference_one"]?.takeIf { it.isNotBlank() } ?: error("Referencia Familiar requerido")
        val familyPhone1 =
            params["telephone_reference_one"]?.takeIf { it.isNotBlank() } ?: error("Referencia Familiar requerido")
        val familyRelation1 =
            params["familyRelation1"]?.takeIf { it.isNotBlank() } ?: error("Referencia Familiar requerido")
        val familyName2 =
            params["name_reference_two"]?.takeIf { it.isNotBlank() } ?: error("Referencia Familiar requerido")
        val familyPhone2 =
            params["telephone_reference_two"]?.takeIf { it.isNotBlank() } ?: error("Referencia Familiar requerido")
        val familyRelation2 =
            params["familyRelation2"]?.takeIf { it.isNotBlank() } ?: error("Referencia Familiar requerido")
        val personalReferenceName =
            params["personalReferenceName"]?.takeIf { it.isNotBlank() } ?: error("Referencias Personales requerido")
        val personalReferencePhone =
            params["personalReferencePhone"]?.takeIf { it.isNotBlank() } ?: error("Referencias Personales requerido")


        val supplementaryDataAdded = PersonInfoModel.addSupplementaryData(
            profession,
            homePhone,
            supYear,
            supMonth,
            zipCode,
            street,
            extNo,
            intNo,
            state,
            stateCode,   //state SEPOMEXCode
            municipality,
            municipalityCode,   // municipality SEPOMEXCode,
            colony,
            colonyCode,     //colony SEPOMEXCode,
            city,
            cityCode,   // city SEPOMEXCode,
            labYear,
            labMonth,
            giro,
            occupation,
            familyName1,
            familyPhone1,
            familyRelation1,
            familyName2,
            familyPhone2,
            familyRelation2,
            personalReferenceName,
            personalReferencePhone,
            userSession.userId
        )

        if (supplementaryDataAdded > 0) {
            if (user.status <= UserModel.SUMMARY_2_COMPLETE) {
                UserModel.updateUserStatus(UserModel.SUMMARY_2_COMPLETE, userSession.userId)
            }
        }

        success("success")
    }

    suspend fun mobileDataFormSubmit() {
        val userSession = call.userSession
        val user = UserModel.getUser(userSession.mobile) ?: error("Invalid session!")
        val params = call.receiveSafeParameters()
        val street = params["street"]?.takeIf { it.isNotBlank() } ?: error("Calle requerido")
        val extNo = params["ext_no"]?.takeIf { it.isNotBlank() } ?: error("No Ext requerido")
        val intNo = params["int_no"]
        val zipCode = params["zip"]?.takeIf { it.isNotBlank() } ?: error("C.P requerido")
        val state = params["state"]?.takeIf { it.isNotBlank() } ?: error("Estado requerido")
        val stateCode = params["state_code"]?.takeIf { it.isNotBlank() } ?: error("Estado requerido")
        val municipality = params["municipality"]?.takeIf { it.isNotBlank() } ?: error("Alcaldia o Municipio requerido")
        val municipalityCode =
            params["municipality_code"]?.takeIf { it.isNotBlank() } ?: error("Alcaldia o Municipio requerido")
        val colony = params["colony"]?.takeIf { it.isNotBlank() } ?: error("Colonia requerido")
        val colonyCode = params["colony_code"]?.takeIf { it.isNotBlank() } ?: error("Colonia requerido")
        val city = params["city"]?.takeIf { it.isNotBlank() }
        val cityCode = params["city_code"]?.takeIf { it.isNotBlank() }

        val shippingAddress = PersonInfoModel.addShippingAddress(
            street,
            extNo,
            intNo,
            zipCode,
            state,
            stateCode,
            municipality,
            municipalityCode,
            colony,
            colonyCode,
            city ?: "",
            cityCode ?: "",
            userSession.userId
        )

        if (shippingAddress > 0) {
            if (user.status <= UserModel.MOBILE_DATA_COMPLETE) {
                UserModel.updateUserStatus(UserModel.MOBILE_DATA_COMPLETE, userSession.userId)
            }
        }

        success()
    }

    //TC43, TC44 & Product API call in Data Complete
    suspend fun dataComplete() {
        val userSession = call.userSession
        val user = UserModel.getUser(userSession.mobile) ?: error("Invalid session!")
        val tc41Data = PersonInfoModel.getPersonInfoData(userSession.userId) ?: error("información incompleta")
        val tc42Data = PersonInfoModel.getSupplementaryData(user.userId) ?: error("información incompleta")
        val documentData = DocumentModel.getDocumentByUserId(user.userId) ?: error("información incompleta")

        val ineFront = Base64.getEncoder().encodeToString(documentData.ineFront ?: documentData.passport)
        val ineBack = Base64.getEncoder().encodeToString(documentData.ineBack ?: documentData.passport)
//        val proofOfAddress = Base64.getEncoder().encodeToString(documentData.proofOfAddress)
        val proofOfAddress = if (documentData.proofOfAddress != null) {
            Base64.getEncoder().encodeToString(documentData.proofOfAddress)
        } else {
            "R0lGODlhAQABAIAAAAUEBAAAACwAAAAAAQABAAACAkQBADs="
        }

        if (user.status >= UserModel.TC44_API_COMPLETE) {
            error("¡Por favor recarga la página!")
        }
        if (Env.DEBUG) {
            UserModel.updateUserStatus(UserModel.TC44_API_COMPLETE, user.userId)
            return success("345345")
        }

        val tc43Response = KarumApi.submitTC43Form(
            applicationId = tc41Data.applicationid,
            curp = tc41Data.curpNo,
            email = tc41Data.email,
            clientWorkActivity = "9900906",
            clientStreet = tc41Data.street,
            clientColony = tc41Data.colonyCode ?: "",
            clientMunicipalityCode = tc41Data.municipalityCode,
            clientCityCode = tc41Data.cityCode ?: "",
            clientStateCode = tc41Data.stateCode,
            clientPostalCode = tc41Data.zipCode
        )
        if (tc43Response.not()) error("TC43 karum API error!")

        if (user.status <= UserModel.TC43_API_COMPLETE) {
            UserModel.updateUserStatus(UserModel.TC43_API_COMPLETE, userSession.userId)
        }

        val tc44Response = KarumApi.submitTC44RR2Form(
            tc41Data.applicationid,
            tc41Data.curpNo,
            tc42Data.personalReferenceName,
            tc42Data.personalReferencePhone,
            tc42Data.familyReferenceName2,
            tc42Data.familyReferencePhone2,
            tc42Data.familyRelation2,
            tc42Data.familyReferenceName1,
            tc42Data.familyReferencePhone1,
            tc42Data.familyRelation1,
            tc42Data.homePhone,
            tc41Data.stateOfBirth,
            tc42Data.profession,
            tc42Data.occupation,
            tc42Data.supYear.toString(),
            tc42Data.supMonth.toString(),
            tc42Data.city ?: "",
            tc42Data.giro,
            tc42Data.labYear.toString(),
            tc42Data.labMonth.toString(),
            ineFront,
            ineBack,
            proofOfAddress,
            "R0lGODlhAQABAIAAAAUEBAAAACwAAAAAAQABAAACAkQBADs=",
            "R0lGODlhAQABAIAAAAUEBAAAACwAAAAAAQABAAACAkQBADs=",
            ""
        ) ?: error("TC44 karum API error!")

        if (user.status <= UserModel.TC44_API_COMPLETE) {
            UserModel.updateUserStatus(UserModel.TC44_API_COMPLETE, user.userId)
        }

        val confirmationCode = tc44Response.split(":")[1]
        UserModel.updateConfirmationCode(confirmationCode, user.userId)

        //Product API call on Data Completion
        if (user.productId != null && user.price != null) {
            val productApiResponse = KarumApi.submitProductsApi(
                confirmationCode,
                user.curp,
                user.productId,
                user.price,
                tc41Data.telephone,
                tc41Data.alert
            )
            //if (productApiResponse.not()) error("product karum API error!")
        }

        success(confirmationCode)
    }

    /*    companion object {
            var confirmationCode: String? = null
        }*/

    /*suspend fun tc44Submit() {
        val userSession = call.userSession
        val user = UserModel.getUser(userSession.mobile) ?: error("Invalid session")
        val tc41Data = PersonInfoModel.getPersonInfoData(user.userId) ?: error("invalid session")
        val tc42Data = PersonInfoModel.getSupplementaryData(user.userId) ?: error("invalid session")
        val documentData = DocumentModel.getDocumentByUserId(user.userId) ?: error("invalid session")

        val ineFront = Base64.getEncoder().encodeToString(documentData.ineFront ?: documentData.passport)
        val ineBack = Base64.getEncoder().encodeToString(documentData.ineBack ?: documentData.passport)
        val proofOfAddress = Base64.getEncoder().encodeToString(documentData.proofOfAddress)

        val response = KarumApi.submitTC44RR2Form(
            tc41Data.applicationid,
            tc41Data.curpNo,
            tc42Data.personalReferenceName,
            tc42Data.personalReferencePhone,
            tc42Data.familyReferenceName2,
            tc42Data.familyReferencePhone2,
            tc42Data.familyRelation2,
            tc42Data.familyReferenceName1,
            tc42Data.familyReferencePhone1,
            tc42Data.familyRelation1,
            tc42Data.homePhone,
            tc41Data.stateOfBirth,
            tc42Data.profession,
            tc42Data.occupation,
            tc42Data.supYear.toString(),
            tc42Data.supMonth.toString(),
            tc42Data.city ?: "",
            tc42Data.giro,
            tc42Data.labYear.toString(),
            tc42Data.labMonth.toString(),
            ineFront,
            ineBack,
            proofOfAddress,
            "R0lGODlhAQABAIAAAAUEBAAAACwAAAAAAQABAAACAkQBADs=",
            "R0lGODlhAQABAIAAAAUEBAAAACwAAAAAAQABAAACAkQBADs=",
            ""
        )

        if (response != null) {
          *//*  if (user.status <= 8) {
                UserModel.updateUserStatus(8, user.userId)
            }*//*

            val splitCode = response.split(":")[1]
            UserModel.updateConfirmationCode(splitCode, user.userId)
            confirmationCode = splitCode

            success(response)
        } else {
            error("karum API error!")
        }
    }*/

    suspend fun shippingAddressSubmit() {
        val userSession = call.userSession
        val user = UserModel.getUser(userSession.mobile) ?: error("Invalid session!")
        val tc41Data = PersonInfoModel.getPersonInfoData(userSession.userId) ?: error("invalid session")
        val shippingAddressData = PersonInfoModel.getShippingAddress(userSession.userId) ?: error("invalid session")

        val streetNo = shippingAddressData.street + shippingAddressData.extNo + shippingAddressData.intNo

        val response = KarumApi.submitShippingAddressForm(
            user.confirmationCode,
            tc41Data.curpNo,
            "",
            "",
            streetNo,
            shippingAddressData.colonyCode,
            shippingAddressData.colony,
            shippingAddressData.municipalityCode,
            shippingAddressData.municipality,
            shippingAddressData.cityCode ?: "",
            shippingAddressData.city ?: "",
            shippingAddressData.stateCode,
            shippingAddressData.state,
            shippingAddressData.zipCode,
            tc41Data.telephone
        )

        if (response) {
            if (user.status < UserModel.SHIPPING_API_COMPLETE) {
                UserModel.updateUserStatus(UserModel.SHIPPING_API_COMPLETE, userSession.userId)
            }

            success("done")
        } else {
            error("karum API error!")
        }
    }

    suspend fun complementsSubmit(document: String, type: String) {
        val userSession = call.userSession
        val user = UserModel.getUser(userSession.mobile) ?: error("Invalid Session")

        val response = KarumApi.submitComplementsApi(user.confirmationCode, type, document)

        if (response) {
            success("done")
        } else {
            error("karum API error!")
        }
    }

    /*
       This method use to read ine image information using oct!
    */
    suspend fun ocr() {
        val multipartData: List<PartData> = call.receiveMultipart().readAllParts()
        val filePart = multipartData.getFileOrNull("file") ?: error("Invalid file")
        val fileName = filePart.originalFileName as String
        val fileBytes = filePart.streamProvider().readBytes()
        val file = File("${Env.tempFolder}$fileName")
        file.writeBytes(fileBytes)
        val result = OCRHelper.extract(file)

        success(data = result.map { it.description })
    }

    data class OtpSession(val mobile: String, val otp: String)

    /*
       use this function to get otp
    */
    suspend fun otp() {
//        success()
//        error()

        val params = call.receiveSafeParameters()
        val phone = params["phone"] ?: error("Enter valid phone!")
        val country = params["country"] ?: error("Invalid country!")

        val to = "+$country$phone"

        if (Env.TWILIO_ENABLED.not()) {
            call.sessions.set(OtpSession(to, "123456"))
            success()
            return
        }

        val otp = generateOTP()
//            val to = "+52$phone"
//        val to = "+923157499335"
//        val response = SinchResponse(null)
        val response =
            kotlin.runCatching { TwilioUtil.sendSms(to, "Tu código de seguridad Instant C+ es: $otp") }.onFailure {
                it.printStackTrace()
            }.getOrNull()

        //val user = UserModel.getAuthenticatedUser(uname = username, pass = password, userType = UserUserType.ADMIN)

        call.sessions.set(OtpSession(to, otp))
        if (response?.status != null) {
            success(msg = "Otp enviado con éxito!", data = mapOf("otp" to otp, "sms" to response))
        } else {
            error("Error al enviar otp!")
        }
    }

    /*
         use this function to verify otp
     */

    suspend fun verifyOtp() {
        val params = call.receiveSafeParameters()
        val first = params["first"] ?: error("Codice non valido inserito!")
        val second = params["second"] ?: error("Codice non valido inserito!")
        val third = params["third"] ?: error("Codice non valido inserito!")
        val fourth = params["fourth"] ?: error("Codice non valido inserito!")
        val fifth = params["fifth"] ?: error("Codice non valido inserito!")
        val sixth = params["sixth"] ?: error("Codice non valido inserito!")

        val otp = "$first$second$third$fourth$fifth$sixth"

        val otpSession = call.sessions.get<OtpSession>()
        if (otp == otpSession?.otp) {

            val randomToken = UUID.randomUUID().toString()

            UserModel.login(mobileNumber = otpSession.mobile, authToken = randomToken)

            val user = UserModel.getUser(otpSession.mobile) ?: error("Invalid user")

            call.sessions.set(
                UserSession(
                    user.userId,
                    user.mobileNumber,
                    randomToken,
                    false,
                    user.status > UserModel.NEW_USER
                )
            )

//            if (user.isDeclined == 1) error(msg = "isDeclined")

            val prodSession = call.sessions.get<ProductSession>()

            if (prodSession != null) {
                val update = UserModel.updateProduct(prodSession.productId, prodSession.price, user.userId)
                if (update) call.sessions.clear<ProductSession>()
            }

            success(msg = "")
        } else {
            error("")
        }
    }

    private suspend fun checkAndUpdateExistingDocuments(user: User, type: String, front: ByteArray, back: ByteArray?) {
        if (user.status >= UserModel.TC44_API_COMPLETE && user.confirmationCode.isNullOrBlank().not()) {
            val ineFront = Base64.getEncoder().encodeToString(front)
            val ineBack = back?.let { Base64.getEncoder().encodeToString(back) }
            var tipo = if (type == "a") "DOM" else if (type == "i") "Solicitud" else "IDE"
            KarumApi.submitComplementsApi(user.confirmationCode, tipo, ineFront)
            if (tipo == "IDE" && type != "p" && ineBack != null) {
                tipo = "OT1"
                KarumApi.submitComplementsApi(user.confirmationCode, tipo, ineBack)
            }
        }
    }

    /*
         use this function to upload user ine record
      */
    suspend fun ineUpload() {
        //get users current session
        val userSession = call.userSession
        // verify user session with db users
        val user = UserModel.getUser(userSession.mobile) ?: error("Invalid session")
        // receive required parameters
        val type = call.parameters["type"] ?: ""

        /*val params = call.receiveSafeParameters()
        val front = params["frontFile"] ?: error("Invalid file")
        val fileBytes: ByteArray = Base64.getDecoder().decode(front.split("base64,").last())*/


        val multipartData: List<PartData> = call.receiveMultipart().readAllParts()
        val frontFilePart: PartData.FileItem = multipartData.getFileOrNull("frontFile") ?: error("Invalid file")
        val backFilePart = if (type.isBlank()) {
            multipartData.getFileOrNull("backFile") ?: error("Invalid file")
        } else null

        val frontFileName: String = frontFilePart.originalFileName as String
        val backFileName: String? = backFilePart?.originalFileName
//        val frontFileBytes = frontFilePart.streamProvider().readBytes()
//        val backFileBytes = backFilePart?.streamProvider?.let { it() }?.readBytes()
        val frontFilePath = "${Env.tempFolder}${UUID.randomUUID()}$frontFileName"
        val backFilePath = "${Env.tempFolder}${UUID.randomUUID()}$backFileName"

        val frontFile = File(frontFilePath)
        frontFile.writeBytes(frontFilePart.streamProvider().readBytes())
        val backFile = backFilePart?.streamProvider?.let { it() }?.let {
            val file = File(backFilePath)
            file.writeBytes(it.readBytes())
            file
        }
        // Encode INE Card Image file to Base 64
//        val frontFileBase64 = Base64.getEncoder().encode(frontFileBytes).size
//        if (frontFileBase64 > 1000000) error("Tamaño de archivo incorrecto")

        /*if (backFileBytes != null) {
            val backFileBase64 = Base64.getEncoder().encode(backFileBytes).size
//            if (backFileBase64 > 1000000) error("Tamaño de archivo incorrecto")
        }

        if (frontFileBase64 > 1000000) {
            Thumbnails.of(frontFileInputStream).outputQuality(0.8).keepAspectRatio(true).size(1000, 800)
                .toFiles(Rename.PREFIX_DOT_THUMBNAIL)
        }*/


        val passportNumber = multipartData.getValueOrNull("passport")
        if (type == "p" && passportNumber.isNullOrBlank()) {
            error("Pasaporte faltante o incompleto no.")
        }
        println("imagePath: $frontFilePath")

        /*val documentSession = call.sessions.get<UploadDocumentsSession>() ?: UploadDocumentsSession(
            ine = false,
            passport = false,
            address = false
        )*/
        var userNewStatus = user.status

        // if type is address then this condition is true
        if (type == "a") {
//            documentSession.address = true
//            call.sessions.set(documentSession)
            DocumentModel.uploadAddressDocument(frontFile.readBytes(), userSession.userId)
            val documentData = DocumentModel.getDocumentByUserId(userSession.userId)
            if (documentData != null) {
                if (documentData.ineFront == null && documentData.passport == null) {
                    if (user.status <= UserModel.INE_OR_PASS_UPLOADED) {
                        userNewStatus = UserModel.INE_OR_PASS_UPLOADED
                        UserModel.updateUserStatus(UserModel.INE_OR_PASS_UPLOADED, userSession.userId)
                    }
                } else {
                    if (user.status <= UserModel.DOCUMENT_COMPLETE) {
                        userNewStatus = UserModel.DOCUMENT_COMPLETE
                        UserModel.updateUserStatus(UserModel.DOCUMENT_COMPLETE, userSession.userId)
                    }
                }
            }
            checkAndUpdateExistingDocuments(user, type, frontFile.readBytes(), backFile?.readBytes())
            success(extra = userNewStatus)
            return
        } else if (type == "p") {       // if type is passport then this condition is true
//            documentSession.address = true
//            call.sessions.set(documentSession)
            DocumentModel.uploadPassportDocument(frontFile.readBytes(), userSession.userId)
            UserModel.updateUserCurp(ine = passportNumber, userId = userSession.userId)
            val documentData = DocumentModel.getDocumentByUserId(userSession.userId)
            if (documentData != null) {
                if (documentData.proofOfAddress == null) {
                    if (user.status <= UserModel.INE_OR_PASS_UPLOADED) {
                        userNewStatus = UserModel.INE_OR_PASS_UPLOADED
                        UserModel.updateUserStatus(UserModel.INE_OR_PASS_UPLOADED, userSession.userId)
                    }
                } else {
                    if (user.status <= UserModel.DOCUMENT_COMPLETE) {
                        userNewStatus = UserModel.DOCUMENT_COMPLETE
                        UserModel.updateUserStatus(UserModel.DOCUMENT_COMPLETE, userSession.userId)
                    }
                }
            }
            checkAndUpdateExistingDocuments(user, type, frontFile.readBytes(), backFile?.readBytes())
            success(extra = userNewStatus)
            return
        } else if (type == "i") {   // if type is income then this condition is true
            DocumentModel.uploadIncomeDocument(frontFile.readBytes(), userSession.userId)
            checkAndUpdateExistingDocuments(user, type, frontFile.readBytes(), backFile?.readBytes())
            success()
            /*val documentData = DocumentModel.getDocumentByUserId(userSession.userId)
            if (documentData != null) {
                if (documentData.proofOfAddress == null) {
                    UserModel.updateUserStatus(1, userSession.userId)
                    call.sessions.set(UserSession(userSession.userId, userSession.mobile, 1))
                } else {
                    UserModel.updateUserStatus(2, userSession.userId)
                    call.sessions.set(UserSession(userSession.userId, userSession.mobile, 2))
                }
            }*/
        } else {
//            ine uploading


            val frontResults = OCRHelper.extract(frontFile)

            var curp: String? = null
            var ine: String? = null
            val fatherSurname: String?
            val motherSurname: String?
            val name: String?
            val dob: String?

            val ocrText = frontResults.firstOrNull()?.description
            // Get Personal info with ocr 'Name', 'Father Surname', 'Mother Surname'
            val personInfo = ocrText?.split("NOMBRE")?.lastOrNull()?.split("\n")
            dob = ocrText?.split("FECHA DE NACIMIENTO")?.last()?.split("\n")?.getOrNull(1)
            // Get Gender using OCR
//            val gender = ocrText?.split("SEXO")?.lastOrNull()?.split("\n")?.getOrNull(0)


            fatherSurname = personInfo?.getOrNull(1)
            motherSurname = personInfo?.getOrNull(2)
            name = personInfo?.getOrNull(3)

            frontResults.forEach {
                val str = if (it.description.length == 18 && it.description.last() == 'O') {

                    var fixValue = it.description.dropLast(1)
                    if (fixValue.last() == 'O') {
                        fixValue = fixValue.dropLast(1)
                    }
                    fixValue.padEnd(18, '0')
                } else it.description
                if (CurpHelper.validate(str)) {
                    curp = str
                } else if (it.description.length == 18 && it.description.contains(" ").not()) {
                    ine = str
                }
            }
//            var curpData: ResultCURPS? = null
            if (curp.isNullOrBlank().not() && ine.isNullOrBlank().not()) {
                val backResults = OCRHelper.extract(backFile ?: error("Invalid back file"))
                var cicNo: String? = null
                var ocrNo: String? = null
                backResults.firstOrNull()?.description?.split("\n")?.forEach {
                    val value = it.trim()
                    if (value.contains("<<") && value.startsWith("IDMEX")) {
                        val parts = value.split("<<")
                        cicNo = parts[0].split("IDMEX").last()
                        ocrNo = parts[1]
                    }
                }

                /*curpData =
                    kotlin.runCatching { CurpUtil.curpAPi.byCurp(curp.toString())?.respuestaRENAPO?.curpStatus?.resultCURPS }
                        .onFailure { it.printStackTrace() }.getOrNull() ?: error("error al obtener datos")*/

                val first = String(ine!!.toCharArray(0, 8))
                val last = String(ine!!.toCharArray(14, 18))
                val randNumber = String.format("%06d", Random().nextInt(999999))
                ine = "$first$randNumber$last"

                DocumentModel.uploadINEDocument(frontFile.readBytes(), backFile.readBytes(), userSession.userId)
                // Save Curp and Ine to Database
                UserModel.updateUserCurp(curp!!, ine!!, userSession.userId)

                val documentData = DocumentModel.getDocumentByUserId(userSession.userId)
                if (documentData != null) {
                    if (documentData.proofOfAddress == null) {
                        if (user.status <= UserModel.INE_OR_PASS_UPLOADED) {
                            userNewStatus = UserModel.INE_OR_PASS_UPLOADED
                            UserModel.updateUserStatus(UserModel.INE_OR_PASS_UPLOADED, userSession.userId)
                        }
                    } else {
                        if (user.status <= UserModel.DOCUMENT_COMPLETE) {
                            userNewStatus = UserModel.DOCUMENT_COMPLETE
                            UserModel.updateUserStatus(UserModel.DOCUMENT_COMPLETE, userSession.userId)
                        }
                    }
                }
//            UserModel.updateUserStatus(1, userSession.userId)
                /// Get User Data by Curp id

                val addressList = mutableListOf<String>()
                var expiryYear = ""
                // var registerYear = ""
                var addressCounter = 1
                var isAddressFlag = false
                frontResults[0].description.split("\n").forEach {
                    if (isAddressFlag && addressCounter <= 3) {
                        addressList.add(it)
                        addressCounter++
                    }
                    if (it.contains("DOMICILIO")) {
                        isAddressFlag = true
                    }
                    if (it.contains("-20")) {
                        expiryYear = it.split("-").last()
                    }
                }
                checkAndUpdateExistingDocuments(user, type, frontFile.readBytes(), backFile.readBytes())
                val stateOfBirth: String? =
                    curp?.substring(11, 13)?.let { st -> Estado().getStateList().find { it.curpCode == st } }?.codeTC44
                val gender: String? = curp?.substring(10, 11)

                // Send Final Response of api
                success(
                    data = IneSummaryData(
                        ine ?: "",
                        curp ?: "",
                        name ?: "",             // curpData.name
                        motherSurname ?: "",      //curpData.motherSurname
                        fatherSurname ?: "",            // curpData.parentSurname
                        stateOfBirth ?: "",
                        dob ?: "",                              //  curpData.dob
                        gender?.trim() ?: "",                          //curpData.gender
                        /*curpData.regYear ?:*/
                        "",
                        addressList.getOrNull(0) ?: "",
                        addressList.getOrNull(1) ?: "",
                        addressList.getOrNull(2) ?: "",
                        expiryYear,
                        cicNo ?: "",
                        ocrNo ?: "",
                    ), extra = userNewStatus
                )
            }
        }
        error()
    }

    suspend fun passportNumberUpload() {
        val userSession = call.userSession
        val params = call.receiveSafeParameters()
        val passportNumber = params["passport_number"]

        val response = UserModel.updateUserCurp(ine = passportNumber, userId = userSession.userId)
        if (response) {
            success()
        }
    }

    suspend fun getUserSession() {
        val userSession = call.userSession
        val user = UserModel.getUser(userSession.mobile) ?: error("")
        success(data = user.status)
    }

    /*
         use this function to check that mobile session is created!
      */
    suspend fun isMobileSessionCreated() {
        val user = UserModel.getUserByAuthToken(call.userSession.authToken)
        success(data = user!!.isHanddOff)
    }

    suspend fun productSubmit() {
        val userSession = call.userSession
        val user = UserModel.getUser(userSession.mobile) ?: error("Sesión inválida")
        val tc41 = PersonInfoModel.getPersonInfoData(user.userId) ?: error("información incompleta")

        if (user.productId != null && user.price != null) {
            val response = KarumApi.submitProductsApi(
                user.confirmationCode,
                user.curp,
                user.productId,
                user.price,
                tc41.telephone,
                tc41.alert
            )

            if (response) {
                success("done")
            } else {
                error("karum API error!")
            }
        }
    }

    suspend fun storeSummaryData() {
        val userSession = call.userSession
        val param = call.receiveSafeParameters()
        val parentSurname = param["parent_surname"] ?: ""
        val motherSurname = param["mother_surname"] ?: ""
        val name = param["name"] ?: ""
        val gender = param["gender"]?.trim() ?: ""
        val dateOfBirth = param["date_of_birth"] ?: ""
        val stateOfBirth = param["stateOfBirth"] ?: ""
        // val addressOwo = param["address_one"] ?: ""
        //val addressTwo = param["address_two"] ?: ""
        //val addressThree = param["address_three"] ?: ""
        val curpId = param["curp_id"] ?: ""
        //val registerYear = param["register_year"] ?: ""
        //val issuNumber = param["issu_number"] ?: ""
        val summaryIne = param["summary_ine"] ?: ""
        //val yearOfIssue = param["year_of_issue"] ?: ""
        //val yearOfExpiry = param["year_of_expiry"] ?: ""
        //val ocr = param["ocr"] ?: ""
        //val cic = param["cic"] ?: ""

        val dob = dateOfBirth.split("-")
        val year = dob[0].toIntOrNull() ?: 0
        val month = if (dob.size > 1) dob[1].toIntOrNull() else 0
        val day = if (dob.size > 2) dob[2].toIntOrNull() else 0

        UserModel.updateUserCurp(curpId, summaryIne, userSession.userId)
        PersonInfoModel.addINEData(
            parentSurname,
            motherSurname,
            name,
            gender,
            stateOfBirth,
            year,
            month ?: 0,
            day ?: 0,
            curpId,
            summaryIne,
            userSession.userId
        )

        success()
    }

    suspend fun updateUserStatusToPoa() {
        val userSession = call.userSession
        UserModel.updateUserStatus(UserModel.DOCUMENT_COMPLETE, userSession.userId)
        success()
    }

}

data class IneSummaryData(
    val ine: String,
    val curp: String,
    val name: String,
    val motherSurname: String,
    val parentSurname: String,
    val birthState: String,
    val dob: String,
    val gender: String,
    val yearRegister: String,
    val address1: String,
    val address2: String,
    val address3: String,
    val yearOfExpire: String,
    val cic: String,
    val ocr: String,
)