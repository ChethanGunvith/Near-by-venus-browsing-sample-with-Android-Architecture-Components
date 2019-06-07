package com.chethan.abn.view.venuedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.chethan.abn.R
import com.chethan.abn.api.binding.FragmentDataBindingComponent
import com.chethan.abn.databinding.VenueDetailFragmentBinding
import com.chethan.abn.di.Injectable
import com.chethan.abn.testing.OpenForTesting
import com.chethan.abn.utils.autoCleared
import javax.inject.Inject

/**
 * Created by Chethan on 5/3/2019.
 */

@OpenForTesting
class VenueDetailFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var venueDetailViewModel: VenueDetailViewModel
    private lateinit var pagerAdapter: VenuePhotosPagerAdapter

    var binding by autoCleared<VenueDetailFragmentBinding>()
    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<VenueDetailFragmentBinding>(
                inflater,
                R.layout.venue_detail_fragment,
                container,
                false,
                dataBindingComponent
        )

        binding = dataBinding
        return dataBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        venueDetailViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(VenueDetailViewModel::class.java)
        val params = VenueDetailFragmentArgs.fromBundle(arguments!!)
        venueDetailViewModel.setVenueId(params.venuId)
        binding.setLifecycleOwner(viewLifecycleOwner)
        venueDetailViewModel.venueDetails.observe(viewLifecycleOwner, Observer { result ->
            binding.venue = result.data
            if (result.data != null)
                context?.let {
                    binding.viewPager.adapter = VenuePhotosPagerAdapter(it, result.data.venuePhotos)
                    binding.tabLayout.setupWithViewPager(binding.viewPager, true)
                }

        })


    }

}