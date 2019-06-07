package com.chethan.abn.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chethan.abn.model.Venue
import com.chethan.abn.model.VenueDetails
import com.chethan.abn.model.VenuePhotos
import com.chethan.abn.model.VenuesSearchResult

/**
 * Created by Chethan on 5/3/2019.
 */

@Database(
    entities = [
        VenuesSearchResult::class,
        VenueDetails::class,
        VenuePhotos::class,
        Venue::class],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun venueDao(): VenueDao
    abstract fun venueDetailsDao(): VenueDetailsDao
}
