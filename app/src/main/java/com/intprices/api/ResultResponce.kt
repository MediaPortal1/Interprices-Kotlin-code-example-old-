package com.intprices.api

import com.intprices.api.model.Product
import com.intprices.api.model.ProductsRequest
import com.intprices.api.model.SearchResponce
import retrofit2.Call


class ResultResponce private constructor(val request: ProductsRequest) {


    private object Holder {
        val instance= ResultResponce(RestObj.instance.getRestClient().create(ProductsRequest::class.java))
    }

    companion object{
        val instance= Holder.instance
    }



    fun getSearchResult(query:String): SearchResponce? {
        val call=this.request?.getProducts(query)
        val responce = call?.execute()
        return responce?.body()
    }

}