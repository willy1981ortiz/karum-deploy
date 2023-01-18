package com.helper

import com.api.KarumApi
import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface CurpApi {


    @POST("api/Business/Curp/bydata")
    @FormUrlEncoded
    @Headers("ApiKey: FTG2vZbb")
    suspend fun byData(
        @Field("nombres") name: String,
        @Field("apellidoPaterno") firstSurname: String,
        @Field("apellidoMaterno") secondSurname: String?,
        @Field("sexo") gender: String,
        @Field("fechaNacimiento") dob: String, // dd/mm/yyyy.
        @Field("entidadNacimiento") birthEntity: String = "MC", // http://docs.tuidentidad.com/curp-service/#table-1-federal-entities
    ): CurpResponse


    @POST("api/Business/Curp/bycurp")
    @FormUrlEncoded
    @Headers("ApiKey:FTG2vZbb")
    suspend fun byCurp(
        @Field("curp") curp: String
    ): CurpResponse
}

object CurpUtil {
    val BASE_URL = "https://web-prod01.tuidentidad.com"
    val curpAPi: CurpApi

    init {
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .client(KarumApi.okhttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        curpAPi = retrofit.create(CurpApi::class.java)

    }

    suspend fun getByData(
        name: String,
        firstSurname: String,
        secondSurname: String?,
        gender: String,
        dob: String,
        birthEntity: String
    ): CurpResponse {
        return curpAPi.byData(name, firstSurname, secondSurname, gender, dob)
    }


}


data class CurpResponse(
    @SerializedName("codigoRespuesta") var responseCode: String? = null,
    @SerializedName("respuestaRENAPO") var respuestaRENAPO: RespuestaRENAPO? = null,
)


class RespuestaRENAPO {
    @SerializedName("curpStatus") var curpStatus: CurpStatus? = null
}

class CurpStatus {
    @SerializedName("resultCURPS") var resultCURPS: ResultCURPS? = null
}

class ResultCURPS {
    @SerializedName("curp") var curp: String? = null
    @SerializedName("apellidoPaterno") var parentSurname: String? = null
    @SerializedName("apellidoMaterno") var motherSurname: String? = null
    @SerializedName("nombres") var name: String? = null
    @SerializedName("fechNac") var dob: String? = null
    @SerializedName("sexo") var gender: String? = null
    @SerializedName("cveEntidadNac") var stateOfBirth: String? = null
    @SerializedName("anioReg") var regYear: String? = null


    fun formatInputDob(): String? {
        return dob?.split("/")?.reversed()?.joinToString("-")
    }
}