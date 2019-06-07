package com.android.example.github.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.chethan.abn.AppExecutors
import com.chethan.abn.R
import com.chethan.abn.databinding.VenueItemBinding
import com.chethan.abn.model.Venue

/**
 * A RecyclerView adapter for [Venue] class.
 */
class VenuesListAdapter(
        private val dataBindingComponent: DataBindingComponent,
        appExecutors: AppExecutors,
        private val showFullName: Boolean,
        private val repoClickCallback: ((Venue) -> Unit)?

) : DataBoundListAdapter<Venue, VenueItemBinding>(
        appExecutors = appExecutors,
        diffCallback = object : DiffUtil.ItemCallback<Venue>() {
            override fun areItemsTheSame(oldItem: Venue, newItem: Venue): Boolean {
                return oldItem.name == newItem.name
                        && oldItem.location == newItem.location
            }

            override fun areContentsTheSame(oldItem: Venue, newItem: Venue): Boolean {
                return oldItem.name == newItem.name
                        && oldItem.location == newItem.location
            }
        }
) {

    override fun createBinding(parent: ViewGroup): VenueItemBinding {
        val binding = DataBindingUtil.inflate<VenueItemBinding>(
                LayoutInflater.from(parent.context),
                R.layout.venue_item,
                parent,
                false,
                dataBindingComponent
        )
        binding.showFullName = showFullName
        binding.root.setOnClickListener {
            binding.venue?.let {
                repoClickCallback?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: VenueItemBinding, item: Venue) {
        binding.venue = item
    }
}
