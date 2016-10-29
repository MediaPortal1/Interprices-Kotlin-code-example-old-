package com.intprices.api

import com.intprices.api.model.Product
import com.intprices.api.model.IntpricesRequest
import com.intprices.api.model.SearchResponce
import com.intprices.api.model.SettingsResponce
import retrofit2.Call
import java.util.*


class ResultResponce private constructor(val request: IntpricesRequest) {

    val map=HashMap<String,String>()

    var freeshipping=false
    var pricefrom=0
    var priceto=0
    var category=""


    private object Holder {
        val singleton= ResultResponce(RestObj.instance)
    }

    companion object{
        val instance= Holder.singleton
    }

    fun getSearchResult(query:String,page: Int): SearchResponce? {
        map["query"]=query
        map["page"]=page.toString()
        val call=this.request?.getProducts(map)
        val responce = call?.execute()
        return responce?.body()
    }
    fun getSearchResult(querymap:Map<String,String>): SearchResponce? {
        val call=this.request?.getProducts(querymap)
        val responce = call?.execute()
        return responce?.body()
    }

    fun getSettingsResult(): SettingsResponce? {
        val call=this.request?.getSettings()
        val responce=call.execute()
        return responce?.body()
    }

}