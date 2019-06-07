package com.chethan.abn.db

import androidx.room.TypeConverter

object VenueTypeConverters {
    @TypeConverter
    @JvmStatic
    fun stringToIntList(data: String?): List<String>? {
        return data?.let {
            it.split(",").map {
                it
            }
        }?.filterNotNull()
    }

    @TypeConverter
    @JvmStatic
    fun intListToString(ints: List<String>?): String? {
        return ints?.joinToString(",")
    }
}
