package com.intprices.api.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Alex Poltavets on 18.10.2016.
 */
data class Sort(@SerializedName("name") var name:String,@SerializedName("key") var key:String)