package com.android.example.github.ui.search


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.chethan.abn.model.Venue
import com.chethan.abn.repository.Resource
import com.chethan.abn.repository.VenueRepository
import com.chethan.abn.util.TestUtil
import com.chethan.abn.util.mock
import com.chethan.abn.view.venuesearch.VenuesSearchListViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class VenuesSearchListViewModelTest {
    @Rule
    @JvmField
    val instantExecutor = InstantTaskExecutorRule()
    private val repository = mock(VenueRepository::class.java)
    private lateinit var viewModel: VenuesSearchListViewModel
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
        // need to init after instant executor rule is established.
        viewModel = VenuesSearchListViewModel(repository)
    }


    @Test
    fun basic() {
        val result = mock<Observer<Resource<List<Venue>>>>()
        viewModel.results.observeForever(result)
        viewModel.setQuery("rotterdam")
        verify(repository).searchVenues("rotterdam")
    }


    @Test
    fun refresh() {
        viewModel.refresh()
        verifyNoMoreInteractions(repository)
        viewModel.setQuery("amsterdam")
        viewModel.refresh()
        verifyNoMoreInteractions(repository)
        viewModel.results.observeForever(mock())
        verify(repository).searchVenues("amsterdam")
        reset(repository)
        viewModel.refresh()
        verify(repository).searchVenues("amsterdam")
    }

    @Test
    fun resetSameQuery() {
        viewModel.results.observeForever(mock())
        viewModel.setQuery("rotterdam")
        verify(repository).searchVenues("rotterdam")
        reset(repository)
        viewModel.setQuery("ROTTERDAM")
        verifyNoMoreInteractions(repository)
        viewModel.setQuery("Amsterdam")
        verify(repository).searchVenues("amsterdam")
    }


    @Test
    fun sendResultToUI() {
        val result1 = MutableLiveData<Resource<List<Venue>>>()
        val result2 = MutableLiveData<Resource<List<Venue>>>()
        `when`(repository.searchVenues("amsterdam")).thenReturn(result1)
        `when`(repository.searchVenues("rotterdam")).thenReturn(result2)
        val observer = mock<Observer<Resource<List<Venue>>>>()
        viewModel.results.observeForever(observer)
        viewModel.setQuery("amsterdam")
        verify(observer, never()).onChanged(Mockito.any())
        val venueList = TestUtil.createVenueArrayList(item)
        val result = Resource.success(venueList)

        result1.value = result
        verify(observer).onChanged(result)
        reset(observer)

        val barValue = Resource.success(venueList)
        result2.value = barValue
        viewModel.setQuery("rotterdam")
        verify(observer).onChanged(barValue)
    }
}
