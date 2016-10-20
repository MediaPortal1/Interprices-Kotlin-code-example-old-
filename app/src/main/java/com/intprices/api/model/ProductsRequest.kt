package com.intprices.api.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Alex Poltavets on 19.10.2016.
 */
interface ProductsRequest {

    @GET("products.json")
    fun getProducts(@Query("query") query: String): Call<SearchResponce>


}