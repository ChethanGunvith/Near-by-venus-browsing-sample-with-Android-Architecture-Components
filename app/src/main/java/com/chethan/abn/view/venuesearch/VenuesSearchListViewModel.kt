package com.chethan.abn.view.venuesearch


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.chethan.abn.model.Venue
import com.chethan.abn.repository.Resource
import com.chethan.abn.repository.Status
import com.chethan.abn.repository.VenueRepository
import com.chethan.abn.testing.OpenForTesting
import com.chethan.abn.utils.AbsentLiveData
import java.util.*
import javax.inject.Inject

/**
 * Created by Chethan on 5/3/2019.
 */

@OpenForTesting
class VenuesSearchListViewModel @Inject constructor(venueRepository: VenueRepository) : ViewModel() {

    private val _query = MutableLiveData<String>()
    val query: LiveData<String> = _query
    private val nextPageHandler = NextPageHandler(venueRepository)


    val results: LiveData<Resource<List<Venue>>> = Transformations
            .switchMap(_query) { search ->
                if (search.isNullOrBlank()) {
                    AbsentLiveData.create()
                } else {
                    venueRepository.searchVenues(search)
                }
            }

    fun setQuery(originalInput: String) {
        val input = originalInput.toLowerCase(Locale.getDefault()).trim()
        if (input == _query.value) {
            return
        }

        nextPageHandler.reset()
        _query.value = input
    }

    fun refresh() {
        _query.value?.let {
            _query.value = it
        }
    }

    val loadMoreStatus: LiveData<LoadMoreState>
        get() = nextPageHandler.loadMoreState


    class LoadMoreState(val isRunning: Boolean, val errorMessage: String?) {
        private var handledError = false

        val errorMessageIfNotHandled: String?
            get() {
                if (handledError) {
                    return null
                }
                handledError = true
                return errorMessage
            }
    }


    class NextPageHandler(private val repository: VenueRepository) : Observer<Resource<Boolean>> {
        private var nextPageLiveData: LiveData<Resource<Boolean>>? = null
        val loadMoreState = MutableLiveData<LoadMoreState>()
        private var query: String? = null
        private var _hasMore: Boolean = false
        val hasMore
            get() = _hasMore

        init {
            reset()
        }


        override fun onChanged(result: Resource<Boolean>?) {
            if (result == null) {
                reset()
            } else {
                when (result.status) {
                    Status.SUCCESS -> {
                        _hasMore = result.data == true
                        unregister()
                        loadMoreState.setValue(
                                LoadMoreState(
                                        isRunning = false,
                                        errorMessage = null
                                )
                        )
                    }
                    Status.ERROR -> {
                        _hasMore = true
                        unregister()
                        loadMoreState.setValue(
                                LoadMoreState(
                                        isRunning = false,
                                        errorMessage = result.message
                                )
                        )
                    }
                    Status.LOADING -> {
                        // ignore
                    }
                }
            }
        }

        private fun unregister() {
            nextPageLiveData?.removeObserver(this)
            nextPageLiveData = null
            if (_hasMore) {
                query = null
            }
        }

        fun reset() {
            unregister()
            _hasMore = true
            loadMoreState.value = LoadMoreState(
                    isRunning = false,
                    errorMessage = null
            )
        }
    }
}