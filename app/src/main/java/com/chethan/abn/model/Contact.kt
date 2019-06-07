package com.chethan.abn.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Chethan on 5/7/2019.
 */

data class Contact(
    @field:SerializedName("phone")
    var phone: String? = "",
    @field:SerializedName("formattedPhone")
    var formattedPhone: String? = ""
) : Serializable {

}