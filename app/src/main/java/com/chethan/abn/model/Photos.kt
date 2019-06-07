package com.chethan.abn.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Chethan on 5/7/2019.
 */


data class Photos(
        @field:SerializedName("count")
        var count: Int,
        @field:SerializedName("groups")
        var groups: List<Groups>
) : Serializable {


    data class Groups(
            @field:SerializedName("items")
            var items: List<Items>? = emptyList()
    ) {

    }

    data class Items(
            @field:SerializedName("prefix")
            var prefix: String? = "",

            @field:SerializedName("suffix")
            var suffix: String? = "",

            @field:SerializedName("width")
            var width: String? = "",

            @field:SerializedName("height")
            var height: String? = ""
    ) {


    }


}


