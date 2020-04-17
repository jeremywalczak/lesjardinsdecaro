package com.jekro.lesjardindecaro.ui.list

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.shape.ShapeAppearanceModel
import com.jekro.lesjardindecaro.R
import com.jekro.lesjardindecaro.load
import com.jekro.lesjardindecaro.model.Product
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_product.*


class ListProductAdapter(
    private val context: Context,
    var objects: ArrayList<Product>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onItemClick: ((View, Product) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        val holder: RecyclerView.ViewHolder
        holder = CouponViewHolder(inflater.inflate(R.layout.list_item_product, parent, false))
        return holder
    }

    override fun getItemCount(): Int = objects.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val couponsItemHolder = holder as CouponViewHolder
        val coupon = objects[position]
        couponsItemHolder.product_title.text = coupon.title
        couponsItemHolder.produit_image.load(coupon.image)
    }

    inner class CouponViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        var product: Product? = null

        init {
            /*itemView.setOnClickListener {
                onItemClick?.invoke(coupon_image, coupon!!)
            }*/
        }
    }
}