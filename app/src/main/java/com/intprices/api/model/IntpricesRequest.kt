package com.intprices.api.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 * Created by Alex Poltavets on 19.10.2016.
 */
interface IntpricesRequest {

    @GET("products.json")
    fun getProducts(@QueryMap query: Map<String,String>): Call<SearchResponce>

    @GET("settings.json")
    fun getSettings(): Call<SettingsResponce>

}