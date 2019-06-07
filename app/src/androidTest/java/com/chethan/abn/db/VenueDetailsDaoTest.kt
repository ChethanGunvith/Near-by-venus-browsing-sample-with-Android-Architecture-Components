package com.chethan.abn

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.runner.AndroidJUnit4
import com.chethan.abn.model.Venue
import com.chethan.abn.model.VenueDetails
import com.chethan.abn.util.LiveDataTestUtil.getValue
import com.chethan.abn.util.TestUtil
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Chethan on 5/3/2019.
 */
@RunWith(AndroidJUnit4::class)
class VenueDetailsDaoTest : DbTest() {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    val item =
            TestUtil.createVenueDetailItem(
                    "5a2285eddee7701b1d63d2d3",
                    "Café Van Zuylen",
                    "",
                    "31206391055",
                    "+31206391055",
                    7.7,
                    "Torensteeg 4-8",
                    "NL",
                    "Amsterdam",
                    "Noord-Holland",
                    "Nederland",
                    "1012 TH")

    @Test
    fun insertAndRead() {

        // insert data and load data
        val item: VenueDetails = TestUtil.createVenueDetails(item)
        db.venueDetailsDao().insertVenueDetails(item)

        // check data is not null
        val venue = getValue(db.venueDetailsDao().loadVenueDetails("5a2285eddee7701b1d63d2d3"))
        assertThat(venue, notNullValue())

        // check data element
        assertThat(venue.id, `is`("5a2285eddee7701b1d63d2d3"))
        assertThat(venue.name, `is`("Café Van Zuylen"))
        assertThat(venue.location?.city, `is`("Amsterdam"))
        assertThat(venue.location?.address, `is`("Torensteeg 4-8"))
        assertThat(venue.location?.postalCode, `is`("1012 TH"))

    }


    @Test
    fun createIfNotExists_exists() {
        db.venueDetailsDao().insertVenueDetails(item)
        assertThat(db.venueDetailsDao().createVenueDetailsIfNotExists(item), `is`(-1L))
    }

    @Test
    fun createIfNotExists_doesNotExist() {
        assertThat(db.venueDetailsDao().createVenueDetailsIfNotExists(item), `is`(1L))
    }


    @Test
    fun deleteSingleItem() {
        // insert items
        val item: VenueDetails = TestUtil.createVenueDetails(item)
        db.venueDetailsDao().insertVenueDetails(item)
        db.venueDetailsDao().delete(item)
        // check data size after deletion
        val listAfterDelete = getValue(db.venueDetailsDao().loadAllTheVenueDetails())
        assertThat(listAfterDelete.size, `is`(0))

    }


    @Test
    fun insertItem() {
        // insertion operation
        db.venueDetailsDao().insertVenueDetails(item)
        // check data size, it should be 1
        val listAfterInsert = getValue(db.venueDetailsDao().loadAllTheVenueDetails())
        assertThat(listAfterInsert.size, `is`(1))
    }


}
