package com.helper

import com.api.KarumApi
import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface SinchAPi {


    @POST("/{SERVICE_PLAN_ID}/batches")
    suspend fun sendSms(
        @Header("Authorization") authorization: String,
        @Path("SERVICE_PLAN_ID") SERVICE_PLAN_ID: String,
        @Body sinchBody: SinchBody
    ): SinchResponse
}

object SinchUtil {
//    val SERVICE_PLAN_ID = "CardPay_Group"
//    val TOKEN = "8594b29262324b7582dc3dd4148fc8ac"
//    val SENDER = "+12133771853"

    val SERVICE_PLAN_ID = "0eef3e736c524ebfa87141b260a3cb3b"
    val TOKEN = "037014abef2642ba9734cbd28756fb53"
    val SENDER = "+12133771853"

    val BASE_URL = "https://us.sms.api.sinch.com/xms/v1/"
    val sinchAPi: SinchAPi

    init {
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .client(KarumApi.okhttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        sinchAPi = retrofit.create(SinchAPi::class.java)

    }

    fun getMessageFormat(amount: Float, url: String): String {
        val random = (1..1000).random()
        return StringBuilder()
            .appendLine("From G500")
            .appendLine("Order# $random")
            .appendLine("Amount ${'$'}$amount")
            .appendLine("For Detail open this link: $url").toString()
    }

    suspend fun sendSms(to: String, msg: String): SinchResponse {
        val sinchBody = SinchBody(SENDER, arrayListOf(to), msg)
        return sinchAPi.sendSms("Bearer $TOKEN", SERVICE_PLAN_ID, sinchBody)
    }
}


data class SinchBody(

    @SerializedName("from") var from: String? = null,
    @SerializedName("to") var to: ArrayList<String> = arrayListOf(),
    @SerializedName("body") var body: String? = null

)


data class SinchResponse(

    @SerializedName("id") var id: String? = null,
    @SerializedName("to") var to: ArrayList<String> = arrayListOf(),
    @SerializedName("from") var from: String? = null,
    @SerializedName("canceled") var canceled: Boolean? = null,
    @SerializedName("body") var body: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("modified_at") var modifiedAt: String? = null,
    @SerializedName("delivery_report") var deliveryReport: String? = null,
    @SerializedName("expire_at") var expireAt: String? = null,
    @SerializedName("flash_message") var flashMessage: Boolean? = null

)