package com.intprices.api.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(@SerializedName("status") var status: Boolean,
                          @SerializedName("products") var products: List<Product>,
                          @SerializedName("totalPages") var totalPages: TotalPages)
