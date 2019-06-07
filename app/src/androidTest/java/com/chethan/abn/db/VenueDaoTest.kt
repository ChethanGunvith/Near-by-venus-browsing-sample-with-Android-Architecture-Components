package com.chethan.abn

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.runner.AndroidJUnit4
import com.chethan.abn.model.Venue
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
class VenueDaoTest : DbTest() {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    val item =
            TestUtil.createVenueItem(
                    "5a2285eddee7701b1d63d2d3",
                    "Trainmore",
                    "Coolsingel 63",
                    "NL",
                    "Rotterdam",
                    "South Holland",
                    "Netherlands",
                    "3012 AS"
            )


    @Test
    fun insertAndRead() {

        // insert data and load data
        val itemArrayList: List<Venue> = TestUtil.createVenueArrayList(item)
        db.venueDao().insertVenues(itemArrayList)

        // check data is not null
        val loaded = getValue(db.venueDao().loadById(TestUtil.getListOfVenueIds()))
        assertThat(loaded, notNullValue())

        // check data element
        val venue = loaded.get(0)
        assertThat(venue.id, `is`("4b83cb72f964a520d71031e3"))
        assertThat(venue.name, `is`("Trainmore"))
        assertThat(venue.location.city, `is`("Rotterdam"))
        assertThat(venue.location.address, `is`("Coolsingel 63"))
        assertThat(venue.location.postalCode, `is`("3012 AS"))

        // check data size
        assertThat(loaded.size, `is`(10))

    }


    @Test
    fun createIfNotExists_exists() {
        db.venueDao().insertVenue(item)
        assertThat(db.venueDao().createVenueIfNotExists(item), `is`(-1L))
    }

    @Test
    fun createIfNotExists_doesNotExist() {
        assertThat(db.venueDao().createVenueIfNotExists(item), `is`(1L))
    }


    @Test
    fun deleteSingleItem() {
        // insert 10 items
        val itemArrayList: List<Venue> = TestUtil.createVenueArrayList(item)
        db.venueDao().insertVenues(itemArrayList)
        // Delete single item, Number of item present this time is 10
        db.venueDao().delete(item)
        // check data size after deletion
        val listAfterDelete = getValue(db.venueDao().loadAllTheVenue())
        assertThat(listAfterDelete.size, `is`(9))

    }


    @Test
    fun deleteAll() {
        // Delete all the items
        db.venueDao().deleteAll()
        // check data size after deletion
        val listOfItemsAfterDeletingAll = getValue(db.venueDao().loadAllTheVenue())
        assertThat(listOfItemsAfterDeletingAll.size, `is`(0))
    }

    @Test
    fun insertItem() {
        // insertion operation
        db.venueDao().insertVenue(item)
        // check data size, it should be 1
        val listAfterInsert = getValue(db.venueDao().loadAllTheVenue())
        assertThat(listAfterInsert.size, `is`(1))
    }


}
