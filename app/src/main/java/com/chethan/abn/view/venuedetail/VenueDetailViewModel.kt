package com.chethan.abn.view.venuedetail


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.chethan.abn.model.VenueDetailsWithPhotos
import com.chethan.abn.repository.Resource
import com.chethan.abn.repository.VenueDetailsRepository
import com.chethan.abn.testing.OpenForTesting
import javax.inject.Inject

/**
 * Created by Chethan on 5/3/2019.
 */
@OpenForTesting
class VenueDetailViewModel @Inject constructor(venueDetailsRepository: VenueDetailsRepository) : ViewModel() {

    private val _venueId = MutableLiveData<String>()

    val venueDetails: LiveData<Resource<VenueDetailsWithPhotos>> = Transformations
            .switchMap(_venueId) { input ->
                venueDetailsRepository.loadDetailVenue(input)
            }

    fun setVenueId(venueId: String) {
        if (_venueId.value == venueId) {
            return
        }
        _venueId.value = venueId
    }
}


