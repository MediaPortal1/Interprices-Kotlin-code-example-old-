package com.intprices.api.model

import com.google.gson.annotations.SerializedName

data class SearchResponce(@SerializedName("status") var status:Boolean,
                          @SerializedName("products") var products: List<Product>)
