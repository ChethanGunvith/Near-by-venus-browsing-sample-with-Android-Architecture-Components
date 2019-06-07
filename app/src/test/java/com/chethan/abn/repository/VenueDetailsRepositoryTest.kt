package com.chethan.abn.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.chethan.abn.CLIENT_ID
import com.chethan.abn.CLIENT_SECRET
import com.chethan.abn.RADIUS
import com.chethan.abn.VERSION
import com.chethan.abn.api.ApiResponse
import com.chethan.abn.api.NetWorkApi
import com.chethan.abn.api.VenueDetailsResponse
import com.chethan.abn.api.VenueSearchResponse
import com.chethan.abn.db.AppDatabase
import com.chethan.abn.db.VenueDetailsDao
import com.chethan.abn.model.Venue
import com.chethan.abn.model.VenueDetails
import com.chethan.abn.model.VenueDetailsWithPhotos
import com.chethan.abn.model.VenuesSearchResult
import com.chethan.abn.util.ApiUtil
import com.chethan.abn.util.ApiUtil.successCall
import com.chethan.abn.util.InstantAppExecutors
import com.chethan.abn.util.TestUtil
import com.chethan.abn.util.mock
import com.chethan.abn.utils.AbsentLiveData
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import retrofit2.Response

/**
 * Created by Chethan on 5/3/2019.
 */

@RunWith(JUnit4::class)
class VenueDetailsRepositoryTest {
    private lateinit var repository: VenueDetailsRepository
    private val dao = mock(VenueDetailsDao::class.java)
    private val service = mock(NetWorkApi::class.java)

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()
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

    @Before
    fun init() {
        val db = mock(AppDatabase::class.java)
        `when`(db.venueDetailsDao()).thenReturn(dao)
        `when`(db.runInTransaction(ArgumentMatchers.any())).thenCallRealMethod()
        repository = VenueDetailsRepository(InstantAppExecutors(), dao, service)
    }


    @Test
    fun loadVenueDetailsFromNetwork() {
        val dbData = MutableLiveData<VenueDetailsWithPhotos>()
        `when`(dao.loadVenueDetailsWithPhotos("5a2285eddee7701b1d63d2d3")).thenReturn(dbData)

        val venueDetailsResponse = TestUtil.createVenueDetailsResponse(item)
        val call = successCall(venueDetailsResponse)
        `when`(service.getVenueDetails( "5a2285eddee7701b1d63d2d3",
                CLIENT_ID,
                CLIENT_SECRET,
                VERSION)).thenReturn(call)

        val data = repository.loadDetailVenue("5a2285eddee7701b1d63d2d3")
        verify(dao).loadVenueDetailsWithPhotos("5a2285eddee7701b1d63d2d3")
        verifyNoMoreInteractions(service)

        val observer = mock<Observer<Resource<VenueDetailsWithPhotos>>>()
        data.observeForever(observer)
        verifyNoMoreInteractions(service)
        verify(observer).onChanged(Resource.loading(null))

        val updatedDbData = MutableLiveData<VenueDetails>()
        `when`(dao.loadVenueDetails("5a2285eddee7701b1d63d2d3")).thenReturn(updatedDbData)

        dbData.postValue(null)
        verify(service).getVenueDetails("5a2285eddee7701b1d63d2d3",CLIENT_ID,
                CLIENT_SECRET,
                VERSION)

        val venueDetails = TestUtil.createVenueDetails(item)
        verify(dao).insertVenueDetails(venueDetails)

    }



}