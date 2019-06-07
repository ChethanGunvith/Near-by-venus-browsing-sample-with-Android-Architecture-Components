package com.chethan.abn.model

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Created by Chethan on 5/8/2019.
 *
 * Below structure treats venue details and photos as single data type,
 * Room’s @Relation annotation automatically fetches related entities.
 */

data class VenueDetailsWithPhotos(

        @Embedded
        val venueDetails: VenueDetails,

        @Relation(parentColumn = "id",
        entityColumn = "venueId")
        val venuePhotos: List<VenuePhotos>
) {

}