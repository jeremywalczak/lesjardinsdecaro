package com.jekro.lesjardindecaro.ui.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jekro.lesjardindecaro.R
import com.jekro.lesjardindecaro.load
import com.jekro.lesjardindecaro.model.Product
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_product.*


class ListProductAdapter(
    private val context: Context,
    var objects: ArrayList<Product>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onItemClick: ((Product?) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        val holder: RecyclerView.ViewHolder
        holder = CouponViewHolder(inflater.inflate(R.layout.list_item_product, parent, false))
        return holder
    }

    override fun getItemCount(): Int = objects.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val couponsItemHolder = holder as CouponViewHolder
        val product = objects[position]
        couponsItemHolder.product = product
        couponsItemHolder.product_title.text = product.description
        product.image.url?.let {
            couponsItemHolder.produit_image.load("http://lejardindecaro.fr${product.image.url}")
            //CustomPicasso(context).with(context)?.load("http://i.imgur.com/DvpvklR.png")?.into(couponsItemHolder.produit_image)
            //CustomPicasso(context).getNewInstance(context)!!.load("http://lejardindecaro.fr${product.image.url}")?.into(couponsItemHolder.produit_image)
            //ImageLoader.getInstance().displayImage("http://i.imgur.com/DvpvklR.png", couponsItemHolder.produit_image)
        }
        couponsItemHolder.priceTextView.text = (product.price.fractional.toFloat() / 100).toString() + " €"
        couponsItemHolder.categoryTextView.text = product.cat
        couponsItemHolder.originTextView.text = product.origin
    }

    inner class CouponViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        var product: Product? = null

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(product)
            }
        }
    }
}