package com.intprices.api.model

import com.google.gson.annotations.SerializedName


data class TotalPages(@SerializedName("total") var total: Int,
                      @SerializedName("aliexpress") var aliexpress: Int,
                      @SerializedName("ebay") var ebay: Int,
                      @SerializedName("amazon") var amazon: Int)