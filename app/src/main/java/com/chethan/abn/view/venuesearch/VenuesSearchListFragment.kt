package com.chethan.abn.view.venuesearch

import android.content.Context
import android.os.Bundle
import android.os.IBinder
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.android.example.github.ui.common.VenuesListAdapter
import com.chethan.abn.AppExecutors
import com.chethan.abn.R
import com.chethan.abn.api.binding.FragmentDataBindingComponent
import com.chethan.abn.databinding.VenueSearchFragmentBinding
import com.chethan.abn.di.Injectable
import com.chethan.abn.testing.OpenForTesting
import com.chethan.abn.utils.autoCleared
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

/**
 * Created by Chethan on 5/3/2019.
 */

@OpenForTesting
class VenuesSearchListFragment : Fragment(), Injectable {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<VenueSearchFragmentBinding>()

    private var adapter by autoCleared<VenuesListAdapter>()

    private lateinit var searchViewModel: VenuesSearchListViewModel


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.venue_search_fragment,
                container,
                false,
                dataBindingComponent
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        searchViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(VenuesSearchListViewModel::class.java)
        binding.setLifecycleOwner(viewLifecycleOwner)
        initRecyclerView()
        val rvAdapter = VenuesListAdapter(
                dataBindingComponent = dataBindingComponent,
                appExecutors = appExecutors,
                showFullName = true
        ) { venue ->
            navController().navigate(
                    VenuesSearchListFragmentDirections.showDetailsScreen(venue.id.toString(), venue.name.toString())
            )
        }

        binding.query = searchViewModel.query
        binding.repoList.adapter = rvAdapter
        adapter = rvAdapter
        initSearchInputListener()
    }


    private fun initRecyclerView() {

        binding.searchResult = searchViewModel.results
        searchViewModel.results.observe(viewLifecycleOwner, Observer { result ->

                adapter.submitList(result?.data)

        })

    }

    private fun initSearchInputListener() {
        binding.input.setOnEditorActionListener { view: View, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch(view)
                true
            } else {
                false
            }
        }
        binding.input.setOnKeyListener { view: View, keyCode: Int, event: KeyEvent ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                doSearch(view)
                true
            } else {
                false
            }
        }
    }

    private fun doSearch(v: View) {
        val query = binding.input.text.toString()
        // Dismiss keyboard
        dismissKeyboard(v.windowToken)
        searchViewModel.setQuery(query)
    }


    private fun dismissKeyboard(windowToken: IBinder) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }

    /**
     * Created to be able to override in tests
     */
    fun navController() = findNavController()

}
