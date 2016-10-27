package com.intprices.api.model

import com.google.gson.annotations.SerializedName
import com.intprices.util.SettingMap
import java.util.*

/**
 * Created by Alex Poltavets on 25.10.2016.
 */
open class SettingsItem(protected @SerializedName("name") var _name:String,protected @SerializedName("key") var _key:String): SettingMap{
    override fun getMap(): Map<String, String> {
        val map= HashMap<String,String>()
        map.put("name",_name)
        map.put("key",_key)
        return map
    }
}