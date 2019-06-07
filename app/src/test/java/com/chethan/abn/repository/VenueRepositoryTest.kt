package com.chethan.abn.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.chethan.abn.*
import com.chethan.abn.api.ApiResponse
import com.chethan.abn.util.TestUtil
import com.chethan.abn.api.NetWorkApi
import com.chethan.abn.api.VenueSearchResponse
import com.chethan.abn.db.AppDatabase
import com.chethan.abn.db.VenueDao
import com.chethan.abn.model.Venue
import com.chethan.abn.model.VenuesSearchResult
import com.chethan.abn.util.ApiUtil.successCall
import com.chethan.abn.util.InstantAppExecutors
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
class VenueRepositoryTest {
    private lateinit var repository: VenueRepository
    private val dao = mock(VenueDao::class.java)
    private val service = mock(NetWorkApi::class.java)

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()
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

    @Before
    fun init() {
        val db = mock(AppDatabase::class.java)
        `when`(db.venueDao()).thenReturn(dao)
        `when`(db.runInTransaction(ArgumentMatchers.any())).thenCallRealMethod()
        repository = VenueRepository(InstantAppExecutors(), dao, service)
    }


    @Test
    fun search_fromDb() {
        val listOfVenueIds = TestUtil.getListOfVenueIds()

        val observer = mock<Observer<Resource<List<Venue>>>>()
        val dbSearchResult = MutableLiveData<VenuesSearchResult>()
        val repositories = MutableLiveData<List<Venue>>()

        `when`(dao.search("Rotterdam")).thenReturn(dbSearchResult)

        repository.searchVenues("Rotterdam").observeForever(observer)

        verify(observer).onChanged(Resource.loading(null))
        verifyNoMoreInteractions(service)
        reset(observer)

        val dbResult = VenuesSearchResult("Rotterdam", listOfVenueIds, 10)
        `when`(dao.loadOrdered(listOfVenueIds)).thenReturn(repositories)

        dbSearchResult.postValue(dbResult)

        val repoList = arrayListOf<Venue>()
        repositories.postValue(repoList)
        verify(observer).onChanged(Resource.success(repoList))
        verifyNoMoreInteractions(service)
    }

    @Test
    fun search_fromServer() {

        val observer = mock<Observer<Resource<List<Venue>>>>()
        val dbSearchResult = MutableLiveData<VenuesSearchResult>()
        val repositories = MutableLiveData<List<Venue>>()
        val listOfVenueIds = TestUtil.getListOfVenueIds()

        val apiResponse = TestUtil.createVenueSearchResponse(item)

        val callLiveData = MutableLiveData<ApiResponse<VenueSearchResponse>>()
        `when`(service.searchVenues(CLIENT_ID,
                CLIENT_SECRET, "Rotterdam", RADIUS, VERSION)).thenReturn(callLiveData)

        `when`(dao.search("Rotterdam")).thenReturn(dbSearchResult)

        repository.searchVenues("Rotterdam").observeForever(observer)

        verify(observer).onChanged(Resource.loading(null))
        verifyNoMoreInteractions(service)
        reset(observer)

        `when`(dao.loadOrdered(listOfVenueIds)).thenReturn(repositories)
        dbSearchResult.postValue(null)
        verify(dao, never()).loadOrdered(anyList())

        verify(service).searchVenues(CLIENT_ID,
                CLIENT_SECRET, "Rotterdam", RADIUS, VERSION)
        val updatedResult = MutableLiveData<VenuesSearchResult>()
        `when`(dao.search("Rotterdam")).thenReturn(updatedResult)
        updatedResult.postValue(VenuesSearchResult("Rotterdam", listOfVenueIds, 10))

        callLiveData.postValue(ApiResponse.create(Response.success(apiResponse)))
        verify(dao).insertVenues(apiResponse.response.venues)
        repositories.postValue(apiResponse.response.venues)
        verify(observer).onChanged(Resource.success(apiResponse.response.venues))
        verifyNoMoreInteractions(service)
    }


    @Test
    fun search_fromServer_error() {
        `when`(dao.search("Rotterdam")).thenReturn(AbsentLiveData.create())
        val apiResponse = MutableLiveData<ApiResponse<VenueSearchResponse>>()
        `when`(service.searchVenues(CLIENT_ID,
                CLIENT_SECRET, "Rotterdam", RADIUS, VERSION)).thenReturn(apiResponse)

        val observer = mock<Observer<Resource<List<Venue>>>>()
        repository.searchVenues("Rotterdam").observeForever(observer)
        verify(observer).onChanged(Resource.loading(null))

        apiResponse.postValue(ApiResponse.create(Exception("idk")))
        verify(observer).onChanged(Resource.error("idk", null))
    }

}