package com.chethan.abn.ui.venuesearch

import android.view.KeyEvent
import androidx.databinding.DataBindingComponent
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.pressKey
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.chethan.abn.R
import com.chethan.abn.api.binding.FragmentBindingAdapters
import com.chethan.abn.model.Venue
import com.chethan.abn.repository.Resource
import com.chethan.abn.testing.SingleFragmentActivity
import com.chethan.abn.util.*
import com.chethan.abn.view.venuesearch.VenuesSearchListFragment
import com.chethan.abn.view.venuesearch.VenuesSearchListFragmentDirections
import com.chethan.abn.view.venuesearch.VenuesSearchListViewModel
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*

@RunWith(AndroidJUnit4::class)
class VenuesSearchListFragmentTest {
    @Rule
    @JvmField
    val activityRule = ActivityTestRule(SingleFragmentActivity::class.java, true, true)
    @Rule
    @JvmField
    val executorRule = TaskExecutorWithIdlingResourceRule()
    @Rule
    @JvmField
    val countingAppExecutors = CountingAppExecutorsRule()
    @Rule
    @JvmField
    val dataBindingIdlingResourceRule = DataBindingIdlingResourceRule(activityRule)

    private val listVenue = TestUtil.createVenueArrayList(TestUtil.getVenueItem())

    private lateinit var mockBindingAdapter: FragmentBindingAdapters
    private lateinit var viewModel: VenuesSearchListViewModel
    private val results = MutableLiveData<Resource<List<Venue>>>()
    private val searchFragment = TestSearchFragment()

    @Before
    fun init() {
        viewModel = mock(VenuesSearchListViewModel::class.java)

        `when`(viewModel.results).thenReturn(results)

        mockBindingAdapter = mock(FragmentBindingAdapters::class.java)

        searchFragment.appExecutors = countingAppExecutors.appExecutors
        searchFragment.viewModelFactory = ViewModelUtil.createFor(viewModel)
        searchFragment.dataBindingComponent = object : DataBindingComponent {
            override fun getFragmentBindingAdapters(): FragmentBindingAdapters {
                return mockBindingAdapter
            }
        }
        activityRule.activity.setFragment(searchFragment)
        EspressoTestUtil.disableProgressBarAnimations(activityRule)
    }

    @Test
    fun search() {
        onView(withId(R.id.progress_bar)).check(matches(CoreMatchers.not(isDisplayed())))
        onView(withId(R.id.input)).perform(
            typeText("amsterdam"),
            pressKey(KeyEvent.KEYCODE_ENTER)
        )
        verify(viewModel).setQuery("amsterdam")
        results.postValue(Resource.loading(null))
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
    }

    @Test
    fun loadResults() {
        results.postValue(Resource.success(listVenue))
        val view = listMatcher().atPosition(0)
        onView(view).check(matches(hasDescendant(withText("Title : Trainmore"))))
        onView(withId(R.id.progress_bar)).check(matches(CoreMatchers.not(isDisplayed())))
    }


    @Test
    fun navigateToRepo() {
        results.postValue(Resource.success(listVenue))
        val view = listMatcher().atPosition(0)
        onView(view).perform(ViewActions.click())
        verify(searchFragment.navController).navigate(
                VenuesSearchListFragmentDirections.showDetailsScreen("5a2285eddee7701b1d63d2d3", "Trainmore")
        )
    }


    private fun listMatcher(): RecyclerViewMatcher {
        return RecyclerViewMatcher(R.id.repo_list)
    }

    class TestSearchFragment : VenuesSearchListFragment() {
        val navController = mock<NavController>()
        override fun navController() = navController
    }
}