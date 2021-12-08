package me.alfredobejarano.movieslist.utils

import android.net.Uri
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.request.ImageRequestBuilder
import me.alfredobejarano.movieslist.R

/**
 * Created by alfredo on 2019-08-02.
 */
abstract class BindingAdapters {
    companion object {
        @JvmStatic
        @BindingAdapter("posterURL")
        fun setPosterURL(simpleDraweeView: SimpleDraweeView, url: String?) {
            simpleDraweeView.hierarchy.setPlaceholderImage(
                ContextCompat.getDrawable(simpleDraweeView.context, R.drawable.shape_movie_placeholder)
            )

            val imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url ?: "")).build()
            simpleDraweeView.setImageRequest(imageRequest)
        }

        @JvmStatic
        @BindingAdapter("setVisible")
        fun setViewVisibleByBoolean(layout: FrameLayout, visible: Boolean) {
            layout.visibility = if (visible) View.VISIBLE else View.GONE
        }
    }
}
