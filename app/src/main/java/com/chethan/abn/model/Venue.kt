package com.chethan.abn.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Chethan on 5/4/2019.
 *
 * Keeping only what we needed
 *
 *  Since we have two independent call for searching venues and fetching its details.
 *  it is really not need to have all of the information of venue object which comes venue search call,
 *  Creating a UserMinimal object that holds only the data needed will improve the amount of memory used
 *  by the app. load only the subset of fields what is needed for UI will improve the speed of the queries
 *  by reducing the IO cost.
 *
 *  Hence I have consider below fields in the venue table.
 *
 */


@Entity(
    indices = [
        Index("location_city")],
    primaryKeys = ["id"]
)
data class Venue(
    @field:SerializedName("id")
    var id: String,

    @field:SerializedName("name")
    var name: String? = "",

    @field:SerializedName("location")
    @field:Embedded(prefix = "location_")
    var location: Location


) : Serializable {

}