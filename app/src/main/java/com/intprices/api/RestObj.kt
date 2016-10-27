package com.intprices.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.intprices.api.model.IntpricesRequest
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RestObj private constructor() {

    private object Holder {
        val instance = RestObj().getRestClient().create(IntpricesRequest::class.java)
    }

    companion object {
        val instance = Holder.instance
    }

    val API_URL = "http://intprices.com/en/"

    fun getRestClient(): Retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(getGsonFactory())
            .client(getOkHttp())
            .build()

    private fun getGsonFactory() = GsonConverterFactory.create(getGson())
    private fun getGson() = GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
    private fun getOkHttp() = OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .build()


}