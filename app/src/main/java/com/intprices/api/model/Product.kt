package com.intprices.api.model

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.google.gson.annotations.SerializedName
import java.util.*


public data class Product(@SerializedName("title") var title: String, @SerializedName("price") var price: Float, @SerializedName("image") var image: String, @SerializedName("url") var url: String, @SerializedName("shop") var shop: String) {

    // aliexpress
    @SerializedName("isFreeShipping") var isFreeShipping: Boolean = false
    @SerializedName("oldPrice") var oldPrice: Float = 0.toFloat()
    @SerializedName("packageType") var packageType: String? = null

    // ebay
    @SerializedName("subtitle") var subtitle: String? = null
    @SerializedName("bestOfferEnabled") var bestOfferEnabled: Boolean = false
    @SerializedName("buyItNowAvailable") var buyItNowAvailable: Boolean = false
    @SerializedName("buyItNowPrice") var buyItNowPrice: Float = 0.toFloat()
    @SerializedName("listingType") var listingType: String? = null
    @SerializedName("bidCount") var bidCount: Int = 0
    @SerializedName("state") var state: String? = null
    @SerializedName("timeLeft") var timeLeft: String? = null // DateInterval
    @SerializedName("condition") var condition: String? = null
    @SerializedName("shippingType") var shippingType: String? = null
    @SerializedName("shipToLocations") var shipToLocations: List<String>? = null
    @SerializedName("shippingServiceCost") var shippingServiceCost: Float = 0.toFloat()
    @SerializedName("endDate") var endDate: Date? = null
    @SerializedName("currentDate") var currentDate: Date? = null

    // amazon
    @SerializedName("totalNew") var totalNew: Int = 0
    @SerializedName("lowestNewPrice") var lowestNewPrice: Float = 0.toFloat()
    @SerializedName("totalUsed") var totalUsed: Int = 0
    @SerializedName("lowestUsedPrice") var lowestUsedPrice: Float = 0.toFloat()
    @SerializedName("totalCollectible") var totalCollectible: Int = 0
    @SerializedName("lowestCollectiblePrice") var lowestCollectiblePrice: Float = 0.toFloat()
    @SerializedName("totalRefurbished") var totalRefurbished: Int = 0
    @SerializedName("lowestRefurbishedPrice") var lowestRefurbishedPrice: Float = 0.toFloat()
    @SerializedName("amountSaved") var amountSaved: Float = 0.toFloat()
    @SerializedName("percentageSaved") var percentageSaved: Float = 0.toFloat()
    @SerializedName("availabilityType") var availabilityType: String? = null
    @SerializedName("isEligibleForSuperSaverShipping") var isEligibleForSuperSaverShipping: Boolean = false
    // oldPrice as for aliexpress

    companion object{
        public fun loadImages(view:ImageView,url:String): Unit{

        }
    }

}