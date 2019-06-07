package com.chethan.abn.view.venuedetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.PagerAdapter
import com.chethan.abn.databinding.PhotoItemBinding
import com.chethan.abn.model.VenuePhotos

/**
 * Created by Chethan on 5/9/2019.
 */


class VenuePhotosPagerAdapter(private val context: Context, private val venuePhotoList: List<VenuePhotos>) : PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int = venuePhotoList.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding = PhotoItemBinding.inflate(LayoutInflater.from(context), container, false)
        binding.venuePhotos = venuePhotoList[position]
        container?.addView(binding.root)
        return binding.root
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container?.removeView(`object` as CardView)
    }
}