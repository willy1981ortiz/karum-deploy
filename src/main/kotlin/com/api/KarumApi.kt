package com.api

import com.Env
import com.enums.Tc41Alert
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import io.ktor.server.auth.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface KarumApi {

    companion object {
        private val BASE_URL = Env.KARUM_API

        val okhttpClient =
            OkHttpClient().newBuilder().addInterceptor(HttpLoggingInterceptor { message -> println(message) }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()

        private val instance = Retrofit.Builder().baseUrl(BASE_URL).client(
            okhttpClient
        ).addConverterFactory(GsonConverterFactory.create()).build().create(KarumApi::class.java)

        suspend fun getGeoData(zip: String): List<GeoData> {
            val response = kotlin.runCatching { instance.geoData(zip) }
                .onFailure { it.printStackTrace() }.getOrNull()

            if (response != null && response.message.isNullOrBlank().not() && response.status) {
                return kotlin.runCatching { Gson().fromJson(response.message!!, Array<GeoData>::class.java) }
                    .onFailure { it.printStackTrace() }.getOrNull()?.toList() ?: emptyList()
            }

            return emptyList()
        }

        suspend fun submitTc41Form(
            name: String,
            parentLastName: String,
            motherLastName: String,
            dob: String,
            curp: String,
            postalCode: String,
            address: String, //(combination of Calle+No Ext+No Int)
            stateName: String,
            stateSEPOMEXCode: String,
            municipalityName: String,
            municipalitySEPOMEXCode: String,
            colonyName: String,
            colonySEPOMEXCode: String,
            cityName: String,
            citySEPOMEXCode: String,
            telephone: String,
        ): KarumResponse? {
            /*if (Env.DEBUG) {
                return KarumResponse(200, true, "1709")
            }*/
            val response = kotlin.runCatching {
                instance.data41(
                    name,
                    parentLastName,
                    motherLastName,
                    dob,
                    curp,
                    postalCode,
                    address,
                    stateName,
                    stateSEPOMEXCode,
                    municipalityName,
                    municipalitySEPOMEXCode,
                    colonyName,
                    colonySEPOMEXCode,
                    cityName,
                    citySEPOMEXCode,
                    telephone,
                ).let { Gson().fromJson(it, KarumResponse::class.java) }
            }.onFailure {
                it.printStackTrace()
            }.getOrNull()

            return response
        }

        suspend fun submitTC42Form(
            applicationId: String,
            curp: String,
            ine: String,
            otpCode: String,
            companyName: String,
            companyPhone: String,
            monthlyIncome: String
        ): KarumResponse? {
            if (Env.DEBUG) {
                return KarumResponse(200, true, "success")
//                return KarumResponse(200, false, "Declinado","Tramite declinado, ofrece al cliente las promociones vigentes.")
            }
            val response = kotlin.runCatching {
                instance.data42(
                    TC42RequestBody(applicationId, curp, ine, otpCode, companyName, monthlyIncome, companyPhone)
                ).let { Gson().fromJson(it, KarumResponse::class.java) }
            }.onFailure { it.printStackTrace() }.getOrNull()

            return response
        }

        suspend fun submitTC43Form(
            applicationId: String,
            curp: String,
            email: String,
            clientWorkActivity: String,
            clientStreet: String,
            clientColony: String,
            clientMunicipalityCode: String,
            clientCityCode: String,
            clientStateCode: String,
            clientPostalCode: String,
        ): Boolean {
            val response = kotlin.runCatching {
                instance.data43(
                    TC43RequestBody(
                        applicationId = applicationId,
                        curp = curp,
                        email = email,
                        clientWorkActivity = clientWorkActivity,
                        clientStreet = clientStreet,
                        clientColony = clientColony,
                        clientMunicipalityCode = clientMunicipalityCode,
                        clientCityCode = clientCityCode,
                        clientStateCode = clientStateCode,
                        clientPostalCode = clientPostalCode,
                    )
                ).let { Gson().fromJson(it, KarumResponse::class.java) }
            }.onFailure { it.printStackTrace() }.getOrNull()


            if (response != null && response.message.isNullOrBlank().not() && response.status) {
                return true
            }

//            return false
            return true
        }

        suspend fun submitTC44RR2Form(
            applicationId: String,
            curp: String,
            nameRef: String,
            telephoneRef: String,
            nameRefFamily2: String,
            telephoneRefFamily2: String,
            refRelationshipFamily2: String,
            nameRefFamily: String,
            telephoneRefFamily: String,
            refRelationShipFamily: String,
            homePhone: String,
            stateOfBirth: String,
            profession: String,
            occupationCode: String,
            yearOfResidence: String,
            monthOfResidence: String,
            employmentCity: String,
            twistKey: String,
            workYear: String,
            workMonth: String,
            ineFrontImage: String,
            ineBackImage: String,
            addressImage: String,
            selfieImage: String,
            proofOfIncomeImageOptional: String,
            creditPolicy: String,
        ): String? {
            var responseStr: String? = null
            val response = kotlin.runCatching {
                responseStr = instance.data44RR2(
                    TC44RR2RequestBody(
                        applicationId,
                        curp,
                        nameRef,
                        telephoneRef,
                        nameRefFamily2,
                        telephoneRefFamily2,
                        refRelationshipFamily2,
                        nameRefFamily,
                        telephoneRefFamily,
                        refRelationShipFamily,
                        homePhone,
                        stateOfBirth,
                        profession,
                        occupationCode,
                        yearOfResidence,
                        monthOfResidence,
                        employmentCity,
                        twistKey,
                        workYear,
                        workMonth,
                        ineFrontImage,
                        ineBackImage,
                        addressImage,
                        selfieImage,
                        proofOfIncomeImageOptional,
                        creditPolicy
                    )
                )
                responseStr.let { Gson().fromJson(it, KarumResponse::class.java) }
            }.onFailure {
                it.printStackTrace()
            }.getOrNull()

            if (response != null && response.message.isNullOrBlank().not() && response.status) {
                return response.message
            } else if (responseStr != null) {
                println("tc44 error response: $responseStr")
            }


            return null
        }

        suspend fun submitShippingAddressForm(
            confirmationNumber: String,
            curp: String,
            product: String,
            monto: String,
            streetNumber: String,
            colonySEPOMEXCode: String,
            colonyName: String,
            municipalitySEPOMEXCode: String,
            municipalityName: String,
            citySEPOMEXCode: String,
            cityName: String,
            stateSEPOMEXCode: String,
            stateName: String,
            zipCode: String,
            cellPhone: String
        ): Boolean {
            val response = kotlin.runCatching {
                instance.shippingAddress(
                    ShippingAddressRequestBody(
                        confirmationNumber,
                        curp,
                        product,
                        monto,
                        streetNumber,
                        colonySEPOMEXCode,
                        colonyName,
                        municipalitySEPOMEXCode,
                        municipalityName,
                        citySEPOMEXCode,
                        cityName,
                        stateSEPOMEXCode,
                        stateName,
                        zipCode,
                        cellPhone
                    )
                ).let { Gson().fromJson(it, KarumResponse::class.java) }
            }.onFailure { it.printStackTrace() }.getOrNull()

            if (response != null && response.message.isNullOrBlank().not() && response.status) {
                return true
            }

//            return false
            return true
        }

        suspend fun submitComplementsApi(confirmationNumber: String, tipo: String, arrByDocumento: String): Boolean {
            val response = kotlin.runCatching {
                instance.complements(
                    ComplementsRequestBody(
                        confirmationNumber,
                        tipo,
                        arrByDocumento
                    )
                )
            }.onFailure { it.printStackTrace() }.getOrNull()

            if (response != null && response.message.isNullOrBlank().not() && response.status) {
                return true
            }
            return false
//            return true
        }

        suspend fun submitProductsApi(
            confirmationNumber: String,
            curp: String,
            product: String,
            monto: String,
            cellular: String,
            alert: Tc41Alert
        ): Boolean {
            val response = kotlin.runCatching {
                instance.products(
                    ProductsRequestBody(
                        confirmationNumber,
                        curp,
                        product,
                        monto,
                        cellular,
                        alert.toString()
                    )
                )
            }.onFailure { it.printStackTrace() }.getOrNull()

            if (response != null && response.message.isNullOrBlank().not() && response.status) {
                return true
            }
            return false
        }
    }

    @POST("GeoData")
    @FormUrlEncoded
    suspend fun geoData(
        @Field("cp") zip: String
    ): KarumResponse

    @POST("DatosTC41")
    @FormUrlEncoded
    suspend fun data41(
        @Field("Nombre") name: String,
        @Field("ap_Paterno") parentLastName: String,
        @Field("ap_Materno") motherLastName: String,
        @Field("fechaNacimiento") dob: String,
        @Field("CURP") curp: String,
        @Field("CodigoPostal") postalCode: String,
        @Field("CalleNumero") address: String, //(combination of Calle+No Ext+No Int)
        @Field("NombreEstado") stateName: String,
        @Field("CveEstado") stateSEPOMEXCode: String,
        @Field("NombreMunicipio") municipalityName: String,
        @Field("CveMunicipio") municipalitySEPOMEXCode: String,
        @Field("NombreColonia") colonyName: String,
        @Field("CveColonia") colonySEPOMEXCode: String,
        @Field("NombreCiudad") cityName: String = "",
        @Field("CveCiudad") citySEPOMEXCode: String,
        @Field("celular") telephone: String,
        @Field("identificador") identifier: String = "ac",
        @Field("NoTienda") soteNo: String = "93307",
        @Field("Promotor") promotor: String = "1234", // no value
    ): String


    @POST("DatosTC42")
    suspend fun data42(
        @Body body: TC42RequestBody
    ): String

    @POST("DatosTC43")
    suspend fun data43(
        @Body body: TC43RequestBody
    ): String

    @POST("DatosTC44RR2")
    suspend fun data44RR2(
        @Body body: TC44RR2RequestBody
    ): String

    @POST("shippingAddress")
    suspend fun shippingAddress(
        @Body body: ShippingAddressRequestBody
    ): String

    @POST("complements")
    suspend fun complements(
        @Body body: ComplementsRequestBody
    ): KarumResponse

    @POST("Products")
    suspend fun products(
        @Body body: ProductsRequestBody
    ): KarumResponse
}

data class TC42RequestBody(
    @SerializedName("ID_aplicacion") val applicationId: String,
    @SerializedName("CURP") val curp: String,
    @SerializedName("INE") val ine: String,
    @SerializedName("codeSeguridad") val otpCode: String,
    @SerializedName("NombreEmpresa") val companyName: String,
    @SerializedName("IngresoMens") val monthlyIncome: String,
    @SerializedName("telEmpresa") val companyPhone: String,
    @SerializedName("Identificador") val identifier: String = "ac",
    @SerializedName("NoTienda") val soteNo: String = "93307",
    @SerializedName("latitud") val latitude: String? = "20.7201233", // no value
    @SerializedName("longitud") val longitude: String? = "-103.318844",
)

data class TC43RequestBody(
    @SerializedName("IdAplication") val applicationId: String,
    @SerializedName("CURP") val curp: String,
    @SerializedName("email") val email: String,
    @SerializedName("ActividadLaboral") val clientWorkActivity: String = "9900906",
    @SerializedName("CalleNumeroEmpleo") val clientStreet: String,
    @SerializedName("CveColoniaEmpleo") val clientColony: String,
    /*@SerializedName("NombreColoniaEmpleo") val clientName: String,*/
    @SerializedName("CveMunicipioEmpleo") val clientMunicipalityCode: String,
    /*@SerializedName("NombreMunicipioEmpleo") val clientMunicipality: String,*/
    @SerializedName("CveCiudadEmpleo") val clientCityCode: String,
    /*@SerializedName("NombreCiudadEmpleo") val clientCity: String,*/
    @SerializedName("CveEstadoEmpleo") val clientStateCode: String,
    /*@SerializedName("NombreEstadoEmpleo") val clientState: String,*/
    @SerializedName("CodigoPostalEmpleo") val clientPostalCode: String,
    @SerializedName("Identificador") val identifier: String = "ac",
    @SerializedName("NoTienda") val soteNo: String = "93307",
)

data class TC44RR2RequestBody(
    @SerializedName("IdAplication") val applicationId: String,
    @SerializedName("CURP") val curp: String,
    @SerializedName("NombreRef") val nameRef: String,
    @SerializedName("TelefonoRef") val telephoneRef: String,
    @SerializedName("NombreRefFamiliar2") val nameRefFamily2: String,
    @SerializedName("TelefonoRefFamiliar2") val telephoneRefFamily2: String,
    @SerializedName("TipoReferenciaFamiliar2") val refRelationshipFamily2: String,
    @SerializedName("NombreRefFamiliar") val nameRefFamily: String,
    @SerializedName("TelefonoRefFamiliar") val telephoneRefFamily: String,
    @SerializedName("TipoReferenciaFamiliar") val refRelationShipFamily: String,
    @SerializedName("TelCasa") val homePhone: String,
    @SerializedName("edoNacimiento") val stateOfBirth: String, // from tc41
    @SerializedName("Profesion") val profession: String,
    @SerializedName("Ocupacion") val occupationCode: String,
    @SerializedName("AntiguedadDomicilioanio") val yearOfResidence: String,
    @SerializedName("AntiguedadDomiciliomes") val monthOfResidence: String,
    @SerializedName("Ciudadempleo") val employmentCity: String,
    @SerializedName("Giro") val twistKey: String,
    @SerializedName("AntiguedadTrabajoanio") val workYear: String,
    @SerializedName("AntiguedadTrabajomes") val workMonth: String,
    @SerializedName("arrByINEfrente") val ineFrontImage: String,
    @SerializedName("arrByINEanverso") val ineBackImage: String,
    @SerializedName("arrByComprobanteDomicilio") val addressImage: String,
    @SerializedName("arrBySelfie") val selfieImage: String,
    @SerializedName("arrByComprobanteIngresos") val proofOfIncomeImageOptional: String,
    @SerializedName("Politica") val creditPolicy: String,
    @SerializedName("PPE") val politicalExposedPerson: String = "N",
    @SerializedName("EnvioEdoCuenta") val statementSubmissionMethod: String = "ELECTRONICO",
    @SerializedName("Nacionalidad") val nationality: String = "MEXICANA",
    @SerializedName("IDRequisition") val faadId: String = "c0dbb944-d625-4ea0-ab07-6f32f9f4653e", // always blank
    @SerializedName("identificador") val identifier: String = "ac",
    @SerializedName("NoTienda") val soteNo: String = "93307",
    @SerializedName("Promotor") val promotor: String = "1234", // no value
)

data class ShippingAddressRequestBody(
    @SerializedName("Numero de Confirmacion") val confirmationNumber: String,
    @SerializedName("CURP") val curp: String,
    @SerializedName("Producto") val product: String,
    @SerializedName("Monto") val amount: String,
    @SerializedName("CalleNumero") val street_number: String,
    @SerializedName("CveColonia") val colonySEPOMEXCode: String,
    @SerializedName("NombreColonia") val colonyName: String,
    @SerializedName("CveMunicipio") val municipalitySEPOMEXCode: String,
    @SerializedName("NombreMunicipio") val municipalityName: String,
    @SerializedName("CveCiudad") val citySEPOMEXCode: String,
    @SerializedName("NombreCiudad") val cityName: String,
    @SerializedName("CveEstado") val stateSEPOMEXCode: String,
    @SerializedName("NombreEstado") val stateName: String,
    @SerializedName("CodigoPostal") val zipCode: String,
    @SerializedName("celular") val cellPhone: String,
    @SerializedName("Identificador") val identifier: String = "ac"   // Client Number
)

data class ComplementsRequestBody(
    @SerializedName("numeroConfirmacion") val confirmationNumber: String,
    @SerializedName("Tipo") val tipo: String,
    @SerializedName("arrByDocumento") val arrByDocumento: String,
    @SerializedName("Identificador") val identifier: String = "ac"   // Client Number
)

data class ProductsRequestBody(
    @SerializedName("NumeroConfirmacion") val confirmationNumber: String,
    @SerializedName("CURP") val curp: String,
    @SerializedName("Producto") val product: String,
    @SerializedName("Monto") val monto: String,
    @SerializedName("celular") val celular: String,
    @SerializedName("alerta") val alert: String,
    @SerializedName("Identificador") val identifier: String = "ac"   // Client Number
)


data class KarumResponse(
    @SerializedName("Codigo") val code: Int,
    @SerializedName("Estatus") val status: Boolean,
    @SerializedName("Mensaje") val message: String?,
    @SerializedName("Msjusuario") val error: String? = null
) {
    fun isSuccess(): Boolean {
        return this.message.isNullOrBlank().not() && this.status
    }
}

data class GeoData(
    @SerializedName("codigo") val code: String,
    @SerializedName("asenta") val colony: String,
    @SerializedName("id_asenta_cpcons") val colonySEPOMEXCode: String,
//    @SerializedName("tipo_asenta") val tipoAsenta: String?,
    @SerializedName("municipio") val municipality: String,
    @SerializedName("cmunicipio") val municipalitySEPOMEXCode: String,
    @SerializedName("Cestado") val stateSEPOMEXCode: String,
    @SerializedName("estado") val state: String,
    @SerializedName("ciudad") val city: String,
    @SerializedName("c_cve_ciudad") val citySEPOMEXCode: String,
    @SerializedName("cp") val zip: String,
    @SerializedName("Coficina") val Coficina: String?,
)