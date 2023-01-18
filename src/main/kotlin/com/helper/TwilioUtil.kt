package com.helper

import com.Env
import com.google.gson.annotations.SerializedName
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface TwilioAPi {
    companion object {
        val ACCOUNT_SID = Env.TWILIO_SID
        val AUTH_TOKEN = Env.TWILIO_TOKEN
        val SENDER = Env.TWILIO_SENDER
    }

    @POST("Accounts/{accountSid}/Messages.json")
    @FormUrlEncoded
    suspend fun sendSms(
        @Field("To") toNumber: String,
        @Field("Body") body: String,
        @Field("From") from: String = SENDER,
        @Path("accountSid") accountSid: String = ACCOUNT_SID,
    ): TwilioResponse

}

object TwilioUtil {
    private const val BASE_URL = "https://api.twilio.com/2010-04-01/"
    private val twilioAPI: TwilioAPi

    init {
        val okhttpClient = OkHttpClient().newBuilder()
            .addInterceptor(
                BasicAuthInterceptor(
                    TwilioAPi.ACCOUNT_SID,
                    TwilioAPi.AUTH_TOKEN
                )
            )
            .addInterceptor(HttpLoggingInterceptor { message -> println(message) }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okhttpClient)
            .build()
        twilioAPI = retrofit.create(TwilioAPi::class.java)

    }


    suspend fun sendSms(to: String, body: String): TwilioResponse {
        return twilioAPI.sendSms(to, body)
    }


}

class BasicAuthInterceptor(user: String, password: String) : Interceptor {
    private val credentials: String = Credentials.basic(user, password)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val authenticatedRequest: Request = request.newBuilder()
            .header("Authorization", credentials).build()
        return chain.proceed(authenticatedRequest)
    }

}


data class TwilioResponse(

    @SerializedName("sid") var sid: String? = null,
    @SerializedName("to") var to: String?,
    @SerializedName("from") var from: String? = null,
    @SerializedName("status") var status: String?,
    @SerializedName("body") var body: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("message") var errorMessage: String? = null,
)