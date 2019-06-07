package com.chethan.abn.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.chethan.abn.model.Venue
import com.chethan.abn.model.VenueDetails
import com.chethan.abn.model.VenueDetailsWithPhotos
import com.chethan.abn.model.VenuePhotos
import com.chethan.abn.testing.OpenForTesting

/**
 * Created by Chethan on 5/7/2019.
 */


@Dao
@OpenForTesting
abstract class VenueDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertVenueDetails(vararg venue: VenueDetails)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun createVenueDetailsIfNotExists(venue: VenueDetails): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPhotos(repositories: List<VenuePhotos>)

    @Transaction
    @Query("SELECT * FROM VenueDetails where  id = :venueId")
    abstract fun loadVenueDetailsWithPhotos(venueId: String): LiveData<VenueDetailsWithPhotos>

    @Query("SELECT * FROM VenueDetails where  id = :venueId")
    abstract fun loadVenueDetails(venueId: String): LiveData<VenueDetails>

    @Delete
    abstract fun delete(item: VenueDetails)

    @Query("DELETE FROM VenueDetails")
    abstract fun deleteAll()

    @Query("SELECT * FROM VenueDetails")
    abstract fun loadAllTheVenueDetails(): LiveData<List<VenueDetails>>
}
