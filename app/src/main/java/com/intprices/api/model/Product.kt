package com.intprices.api.model

import android.databinding.BindingAdapter
import android.os.Parcel
import android.os.Parcelable
import android.widget.ImageView
import com.google.gson.annotations.SerializedName
import java.util.*


public data class Product(@SerializedName("title") var title: String, @SerializedName("price") var price: Float, @SerializedName("image") var image: String, @SerializedName("url") var url: String, @SerializedName("shop") var shop: String): Parcelable {

    // aliexpress
    @SerializedName("isFreeShipping") var isFreeShipping: Boolean = false //
    @SerializedName("oldPrice") var oldPrice: Float = 0.toFloat() //
    @SerializedName("packageType") var packageType: String? = null

    // ebay
    @SerializedName("subtitle") var subtitle: String? = null//
    @SerializedName("bestOfferEnabled") var bestOfferEnabled: Boolean = false//
    @SerializedName("buyItNowAvailable") var buyItNowAvailable: Boolean = false//
    @SerializedName("buyItNowPrice") var buyItNowPrice: Float = 0.toFloat()//
    @SerializedName("listingType") var listingType: String? = null//
    @SerializedName("bidCount") var bidCount: Int = 0//
    @SerializedName("state") var state: String? = null //TODO
    @SerializedName("timeLeft") var timeLeft: String? = null //
    @SerializedName("condition") var condition: String? = null//
    @SerializedName("shippingType") var shippingType: String? = null//
    @SerializedName("shipToLocations") var shipToLocations: List<String>? = null
    @SerializedName("shippingServiceCost") var shippingServiceCost: Float = 0.toFloat()//
    @SerializedName("endDate") var endDate: Date? = null//
    @SerializedName("currentDate") var currentDate: Date? = null//

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
    @SerializedName("availabilityType") var availabilityType: String? = null//
    @SerializedName("isEligibleForSuperSaverShipping") var isEligibleForSuperSaverShipping: Boolean = false
    // oldPrice as for aliexpress

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeValue(title)
        dest?.writeValue(price)
        dest?.writeValue(image)
        dest?.writeValue(url)
        dest?.writeValue(shop)
        dest?.writeValue(isFreeShipping)
        dest?.writeValue(oldPrice)
        dest?.writeValue(packageType)
        dest?.writeValue(subtitle)
        dest?.writeValue(bestOfferEnabled)
        dest?.writeValue(buyItNowAvailable)
        dest?.writeValue(buyItNowPrice)
        dest?.writeValue(listingType)
        dest?.writeValue(bidCount)
        dest?.writeValue(state)
        dest?.writeValue(timeLeft)
        dest?.writeValue(condition)
        dest?.writeValue(shippingType)
        dest?.writeValue(shipToLocations)
        dest?.writeValue(shippingServiceCost)
        dest?.writeValue(endDate)
        dest?.writeValue(currentDate)
        dest?.writeValue(totalNew)
        dest?.writeValue(lowestNewPrice)
        dest?.writeValue(totalUsed)
        dest?.writeValue(lowestUsedPrice)
        dest?.writeValue(totalCollectible)
        dest?.writeValue(lowestCollectiblePrice)
        dest?.writeValue(amountSaved)
        dest?.writeValue(percentageSaved)
        dest?.writeValue(availabilityType)
        dest?.writeValue(isEligibleForSuperSaverShipping)
    }

    override fun describeContents(): Int {
        return 0
    }

    constructor(`in`: Parcel) : this(`in`.readValue(ClassLoader.getSystemClassLoader()) as String,`in`.readValue(ClassLoader.getSystemClassLoader()) as Float, `in`.readValue(ClassLoader.getSystemClassLoader()) as String,`in`.readValue(ClassLoader.getSystemClassLoader()) as String,`in`.readValue(ClassLoader.getSystemClassLoader()) as String){
        isFreeShipping= `in`.readValue(ClassLoader.getSystemClassLoader()) as Boolean
        oldPrice= `in`.readValue(ClassLoader.getSystemClassLoader()) as Float
        packageType= `in`.readValue(ClassLoader.getSystemClassLoader()) as String?
        subtitle= `in`.readValue(ClassLoader.getSystemClassLoader()) as String?
        bestOfferEnabled= `in`.readValue(ClassLoader.getSystemClassLoader()) as Boolean
        buyItNowAvailable= `in`.readValue(ClassLoader.getSystemClassLoader()) as Boolean
        buyItNowPrice= `in`.readValue(ClassLoader.getSystemClassLoader()) as Float
        listingType= `in`.readValue(ClassLoader.getSystemClassLoader()) as String?
        bidCount= `in`.readValue(ClassLoader.getSystemClassLoader()) as Int
        state= `in`.readValue(ClassLoader.getSystemClassLoader()) as String?
        timeLeft= `in`.readValue(ClassLoader.getSystemClassLoader()) as String?
        condition= `in`.readValue(ClassLoader.getSystemClassLoader()) as String?
        shippingType= `in`.readValue(ClassLoader.getSystemClassLoader()) as String?
        shipToLocations= `in`.readValue(ClassLoader.getSystemClassLoader()) as List<String>?
        shippingServiceCost= `in`.readValue(ClassLoader.getSystemClassLoader()) as Float
        endDate= `in`.readValue(ClassLoader.getSystemClassLoader()) as Date?
        currentDate= `in`.readValue(ClassLoader.getSystemClassLoader()) as Date?
        totalNew= `in`.readValue(ClassLoader.getSystemClassLoader()) as Int
        lowestNewPrice= `in`.readValue(ClassLoader.getSystemClassLoader()) as Float
        totalUsed= `in`.readValue(ClassLoader.getSystemClassLoader()) as Int
        lowestUsedPrice= `in`.readValue(ClassLoader.getSystemClassLoader()) as Float
        totalCollectible= `in`.readValue(ClassLoader.getSystemClassLoader()) as Int
        lowestCollectiblePrice= `in`.readValue(ClassLoader.getSystemClassLoader()) as Float
        amountSaved= `in`.readValue(ClassLoader.getSystemClassLoader()) as Float
        percentageSaved= `in`.readValue(ClassLoader.getSystemClassLoader()) as Float
        availabilityType= `in`.readValue(ClassLoader.getSystemClassLoader()) as String?
        isEligibleForSuperSaverShipping= `in`.readValue(ClassLoader.getSystemClassLoader()) as Boolean
    }
    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Product> = object : Parcelable.Creator<Product> {
            override fun createFromParcel(`in`: Parcel): Product {
                return Product(`in`)
            }

            override fun newArray(size: Int): Array<Product?> {
                return kotlin.arrayOfNulls<Product>(size)
            }
        }
    }
}