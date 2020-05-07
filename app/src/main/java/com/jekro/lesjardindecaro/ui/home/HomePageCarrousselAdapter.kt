package com.jekro.lesjardindecaro.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.jekro.lesjardindecaro.*
import com.jekro.lesjardindecaro.model.Product

class HomePageCarrousselAdapter (
private val context: Context,
private val products: List<Product>,
private val placeholder: Int? = null
) : PagerAdapter() {

    override fun getCount(): Int = products.size

    var onItemClick: ((Product) -> Unit)? = null

    override fun isViewFromObject(view: View, obj: Any): Boolean = obj as View == view

    @SuppressLint("InflateParams")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layout = R.layout.list_item_banner_home_page

        val rowView = View.inflate(context, layout, null)
        val imageView = rowView.findViewById<ImageView>(R.id.list_item_banner_picture)
        val reduceTextView = rowView.findViewById<TextView>(R.id.list_item_banner_reduce)
        val basePriceTextView = rowView.findViewById<TextView>(R.id.list_item_banner_base_price)
        val promoPriceTextView = rowView.findViewById<TextView>(R.id.list_item_banner_promo_price)
        val titleTextView = rowView.findViewById<TextView>(R.id.list_item_banner_title)
        val promoPrice =  (products[position].price.fractional.toFloat() - (products[position].price.fractional.toFloat() * (products[position].reduce!!.toFloat()/100))) / 100
        reduceTextView.text = "-${products[position].reduce}%"
        basePriceTextView.setHtmlText("<strike>${String.format("%.2f",products[position].price.fractional.toFloat() / 100)}€</strike>")
        promoPriceTextView.text = "${String.format("%.2f",promoPrice)} €"
        titleTextView.text = products[position].title

        products[position].image.url?.let {
            imageView.load("http://lejardindecaro.fr${it}", placeholder = R.drawable.logo_jardin_caro)
        }

        rowView.setOnClickListener {
            onItemClick?.invoke(products[position])
        }


        container.addView(rowView)

        return rowView
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) =
        container.removeView(view as View)
}