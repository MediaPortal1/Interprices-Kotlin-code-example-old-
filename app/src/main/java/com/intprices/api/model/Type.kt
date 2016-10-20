package com.intprices.api.model

import com.google.gson.annotations.SerializedName

data class Type(@SerializedName("name") var name:String,@SerializedName("key") var key:String)