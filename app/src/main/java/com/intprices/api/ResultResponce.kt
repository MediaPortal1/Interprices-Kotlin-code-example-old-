package com.intprices.api

import com.intprices.api.model.IntpricesRequest
import com.intprices.api.model.SearchResponse
import com.intprices.api.model.SettingsResponce


class ResultResponce private constructor(val request: IntpricesRequest) {

    private object Holder {
        val singleton = ResultResponce(RestObj.instance)
    }

    companion object {
        val instance = Holder.singleton
    }

    fun getSearchResult(querymap: Map<String, String>): SearchResponse? {
        val call = this.request.getProducts(querymap)
        val responce = call.execute()
        return responce?.body()
    }

    fun getSettingsResult(): SettingsResponce? {
        val call = this.request.getSettings()
        val responce = call.execute()
        return responce?.body()
    }

}