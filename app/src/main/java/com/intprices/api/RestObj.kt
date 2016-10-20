package com.intprices.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestObj private constructor(){

    private object Holder {
        val instance= RestObj()
    }
    companion object{
        val instance= Holder.instance
    }

    val API_URL="http://intprices.com/en/"

    fun getRestClient(): Retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(getGsonFactory())
            .client(OkHttpClient())
            .build()

    private fun getGsonFactory() = GsonConverterFactory.create(getGson())
    private fun getGson() = GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create()

}