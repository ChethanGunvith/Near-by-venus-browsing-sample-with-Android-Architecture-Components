package com.chethan.abn.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Created by Chethan on 5/8/2019.
 */


@Entity(
        indices = [Index("venueId")],
        foreignKeys = [ForeignKey(
                entity = VenueDetails::class,
                parentColumns = ["id"],
                childColumns = ["venueId"],
                onDelete = ForeignKey.CASCADE,
                deferred = true
        )])
data class VenuePhotos(
        @PrimaryKey(autoGenerate = true)
        val id : Int,
        val venueId: String, // this ID points to a VenueDetails
        val url: String? = ""
) {
        constructor(venueId : String, url:String) : this(0,venueId, url)
}