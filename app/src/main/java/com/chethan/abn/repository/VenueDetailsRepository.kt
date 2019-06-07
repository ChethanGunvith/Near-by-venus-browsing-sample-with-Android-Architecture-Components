package com.chethan.abn.repository

import androidx.lifecycle.LiveData
import com.chethan.abn.AppExecutors
import com.chethan.abn.CLIENT_ID
import com.chethan.abn.CLIENT_SECRET
import com.chethan.abn.VERSION
import com.chethan.abn.api.NetWorkApi
import com.chethan.abn.api.VenueDetailsResponse
import com.chethan.abn.db.VenueDetailsDao
import com.chethan.abn.model.Photos
import com.chethan.abn.model.VenueDetailsWithPhotos
import com.chethan.abn.model.VenuePhotos
import com.chethan.abn.testing.OpenForTesting
import com.chethan.demoproject.utils.RateLimiter
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
@OpenForTesting
class VenueDetailsRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val venueDetailsDao: VenueDetailsDao,
        private val netWorkApi: NetWorkApi
) {

    private val repoListRateLimit = RateLimiter<String>(10, TimeUnit.MINUTES)

    fun loadDetailVenue(venueId: String): LiveData<Resource<VenueDetailsWithPhotos>> {
        return object : NetworkBoundResource<VenueDetailsWithPhotos, VenueDetailsResponse>(appExecutors) {
            override fun saveCallResult(item: VenueDetailsResponse) {
                item.response.venue.id = venueId
                // insert venue details first
                venueDetailsDao.insertVenueDetails(item.response.venue)
                // then insert venue photos in VenuePhotos
                item.response.venue.photos?.let {
                    val listOfImages = getListOfImages(venueId, it)
                    venueDetailsDao.insertPhotos(listOfImages)
                }


            }

            override fun shouldFetch(data: VenueDetailsWithPhotos?): Boolean {
                return data == null || repoListRateLimit.shouldFetch(venueId)
            }

            override fun loadFromDb() = venueDetailsDao.loadVenueDetailsWithPhotos(
                    venueId
            )

            override fun createCall() = netWorkApi.getVenueDetails(
                    venueId,
                    CLIENT_ID,
                    CLIENT_SECRET,
                    VERSION
            )
        }.asLiveData()
    }


    fun getListOfImages(venueId: String, photos: Photos): List<VenuePhotos> {
        val photoList = ArrayList<VenuePhotos>()
        for (groups in photos.groups) {
            for (items in groups.items!!) {
                val url = items.prefix + items.width + "x" + items.height + items.suffix
                photoList.add(VenuePhotos(venueId, url))
            }

        }
        return photoList
    }
}
