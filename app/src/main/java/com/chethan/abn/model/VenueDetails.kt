package com.chethan.abn.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Chethan on 5/7/2019.
 */


@Entity(primaryKeys = ["id"])
data class VenueDetails(

        @field:SerializedName("id")
        var id: String,

        @field:SerializedName("name")
        var name: String? = "",

        @field:SerializedName("description")
        var description: String? = "",

        @field:SerializedName("contact") // Nested object
        @field:Embedded(prefix = "contact_")
        var contact: Contact?,

        @field:SerializedName("rating")
        var rating: Double? = 0.0,

        @field:SerializedName("location")
        @field:Embedded(prefix = "location_")
        var location: Location?,

        /*
         Enforce constraints between entities with foreign keys to save photo objects.
         List of objects can be saved either through foreign key relation OR by type converters,
         Photo object json is more lengther and has complex structure, considering Type converter is not
         best approach here, it will slow down performance.
         */
        @field:SerializedName("photos")
        @Ignore
        var photos: Photos?

) : Serializable {
    constructor() : this("", "", "", null, 0.0, null, null)

}