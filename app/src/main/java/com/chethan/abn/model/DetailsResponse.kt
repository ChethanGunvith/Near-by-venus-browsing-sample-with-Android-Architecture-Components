package com.chethan.abn.model

import java.io.Serializable

/**
 * Created by Chethan on 5/7/2019.
 */


data class DetailsResponse(
    val venue: VenueDetails
) : Serializable {

}
