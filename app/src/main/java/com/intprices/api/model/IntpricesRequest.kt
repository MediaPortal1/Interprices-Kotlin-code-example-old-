package com.intprices.api.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface IntpricesRequest {

    @GET("products.json")
    fun getProducts(@QueryMap query: Map<String, String>): Call<SearchResponse>

    @GET("settings.json")
    fun getSettings(): Call<SettingsResponce>

}