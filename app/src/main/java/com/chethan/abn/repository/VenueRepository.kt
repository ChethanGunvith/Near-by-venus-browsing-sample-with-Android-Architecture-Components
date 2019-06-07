package com.chethan.abn.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.chethan.abn.*
import com.chethan.abn.api.ApiSuccessResponse
import com.chethan.abn.api.NetWorkApi
import com.chethan.abn.api.VenueSearchResponse
import com.chethan.abn.db.VenueDao
import com.chethan.abn.model.Venue
import com.chethan.abn.model.VenuesSearchResult
import com.chethan.abn.testing.OpenForTesting
import com.chethan.abn.utils.AbsentLiveData
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
@OpenForTesting
class VenueRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val venueDao: VenueDao,
        private val netWorkApi: NetWorkApi
) {

    fun searchVenues(query: String): LiveData<Resource<List<Venue>>> {
        return object : NetworkBoundResource<List<Venue>, VenueSearchResponse>(appExecutors) {

            override fun saveCallResult(item: VenueSearchResponse) {
                val repoIds = item.response.venues.map { it.id }
                val repoSearchResult = VenuesSearchResult(
                        query = query,
                        repoIds = repoIds,
                        totalCount = item.response.venues.size
                )

                venueDao.insertVenues(item.response.venues)
                venueDao.insert(repoSearchResult)


            }

            override fun shouldFetch(data: List<Venue>?) = data == null

            override fun loadFromDb(): LiveData<List<Venue>> {
                return Transformations.switchMap(venueDao.search(query)) { searchData ->
                    if (searchData == null) {
                        AbsentLiveData.create()
                    } else {
                        venueDao.loadOrdered(searchData.repoIds)
                    }
                }
            }

            override fun createCall() = netWorkApi.searchVenues(
                    CLIENT_ID,
                    CLIENT_SECRET, query, RADIUS, VERSION
            )

            override fun processResponse(response: ApiSuccessResponse<VenueSearchResponse>)
                    : VenueSearchResponse {
                val body = response.body
                return body
            }
        }.asLiveData()
    }
}
