package com.chethan.abn.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Chethan on 5/4/2019.
 */



data class Location(

    @field:SerializedName("address")
    var address: String? = "",
    @field:SerializedName("cc")
    var cc: String? = "",
    @field:SerializedName("city")
    var city: String? = "",
    @field:SerializedName("state")
    var state: String? = "",
    @field:SerializedName("country")
    var country: String? = "",
    @field:SerializedName("postalCode")
    var postalCode: String? = ""

) : Serializable {

}