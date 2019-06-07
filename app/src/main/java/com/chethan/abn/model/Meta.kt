package com.chethan.abn.model

import java.io.Serializable

/**
 * Created by Chethan on 5/4/2019.
 */
data class Meta(
    val code: String,
    val requestId: String
) : Serializable {

}