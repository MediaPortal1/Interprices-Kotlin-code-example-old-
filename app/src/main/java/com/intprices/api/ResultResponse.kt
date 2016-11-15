package com.intprices.api

import com.intprices.api.model.IntpricesRequest
import com.intprices.api.model.SearchResponse
import com.intprices.api.model.SettingsResponce


class ResultResponse private constructor(val request: IntpricesRequest) {

    private object Holder {
        val singleton = ResultResponse(RestObj.instance)
    }

    companion object {
        val instance = Holder.singleton
    }

    fun getSearchResult(queryMap: Map<String, String>): SearchResponse? {
        val call = this.request.getProducts(queryMap)
        val response = call.execute()
        return response?.body()
    }

    fun getSettingsResult(): SettingsResponce? {
        val call = this.request.getSettings()
        val response = call.execute()
        return response?.body()
    }

}