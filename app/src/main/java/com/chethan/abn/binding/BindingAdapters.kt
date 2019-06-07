package com.chethan.abn.api.binding

import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

/**
 * Data Binding adapters specific to the app.
 */
object BindingAdapters {
    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("userRatings")
    fun setUserRatings(view: AppCompatRatingBar, userRating: Double) {
        view.rating = userRating.toFloat() / 2
    }

    @JvmStatic
    @BindingAdapter(value = ["venueImageUrl"])
    fun bindVenueImage(imageView: ImageView, url: String?) {
        Glide.with(imageView.context).load(url).into(imageView)
    }
}
