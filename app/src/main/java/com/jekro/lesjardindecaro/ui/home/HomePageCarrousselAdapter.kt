package com.jekro.lesjardindecaro.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.jekro.lesjardindecaro.Constants
import com.jekro.lesjardindecaro.Constants.Companion.IMAGE_CROP
import com.jekro.lesjardindecaro.R
import com.jekro.lesjardindecaro.RoundedTransformation
import com.jekro.lesjardindecaro.load
import com.jekro.lesjardindecaro.model.Product
import com.squareup.picasso.Picasso

class HomePageCarrousselAdapter (
private val context: Context,
private val products: List<Product>,
private val placeholder: Int? = null
) : PagerAdapter() {

    override fun getCount(): Int = products.size

    var onItemClick: ((String, String?) -> Unit)? = null

    override fun isViewFromObject(view: View, obj: Any): Boolean = obj as View == view

    @SuppressLint("InflateParams")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layout = R.layout.list_item_banner_home_page

        val rowView = View.inflate(context, layout, null)
        val imageView = rowView.findViewById<ImageView>(R.id.list_item_banner_picture)
        val reduceTextView = rowView.findViewById<TextView>(R.id.list_item_banner_reduce)

        reduceTextView.text = products[position].reduce
        products[position].image.url?.let {
            imageView.load("http://lejardindecaro.fr${it}")
        }


        container.addView(rowView)

        return rowView
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) =
        container.removeView(view as View)
}