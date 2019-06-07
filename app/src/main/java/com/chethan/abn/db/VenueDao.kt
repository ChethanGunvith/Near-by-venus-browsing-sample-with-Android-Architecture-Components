package com.chethan.abn.db

import android.util.SparseIntArray
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.room.*
import com.chethan.abn.model.Venue
import com.chethan.abn.model.VenuesSearchResult
import com.chethan.abn.testing.OpenForTesting

/**
 * Interface for database access on Venue related operations.
 */
@Dao
@OpenForTesting
abstract class VenueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertVenue(vararg repos: Venue)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertVenues(repositories: List<Venue>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(result: VenuesSearchResult)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun createVenueIfNotExists(venue: Venue): Long

    @Delete
    abstract fun delete(item: Venue)

    @Query("DELETE FROM Venue")
    abstract fun deleteAll()

    @Query("SELECT * FROM Venue")
    abstract fun loadAllTheVenue(): LiveData<List<Venue>>

    @Query("SELECT * FROM VenuesSearchResult WHERE `query` = :query")
    abstract fun search(query: String): LiveData<VenuesSearchResult>

    fun loadOrdered(repoIds: List<String>): LiveData<List<Venue>> {
        val order = SparseIntArray()
        repoIds.withIndex().forEach {
            order.put(it.index, it.index)
        }
        return Transformations.map(loadById(repoIds)) { repositories ->

            repositories
        }
    }

    @Query("SELECT * FROM Venue WHERE id in (:venueIds)")
    abstract fun loadById(venueIds: List<String>): LiveData<List<Venue>>

    @Query("SELECT * FROM VenuesSearchResult WHERE `query` = :query")
    abstract fun findSearchResult(query: String): VenuesSearchResult?


}
