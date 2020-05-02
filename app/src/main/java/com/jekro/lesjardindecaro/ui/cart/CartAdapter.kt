package com.jekro.lesjardindecaro.ui.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jekro.lesjardindecaro.R
import com.jekro.lesjardindecaro.load
import com.jekro.lesjardindecaro.model.Cart
import com.jekro.lesjardindecaro.model.Product
import com.jekro.lesjardindecaro.ui.QuantityFlatView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_cart_product.*


class CartAdapter(private val context: Context, var items: MutableList<Product>, var cart: Cart) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var loadingIndex: Int? = null
    var cartCallback: CartCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return ViewHolderProductInterface(
            inflater.inflate(
                R.layout.list_item_cart_product,
                parent,
                false
            )
        )
    }


    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolderProductInterface -> items[position]?.let {
                holder.list_item_product_name.text = it.title
                val quantity = if (it.unity.isNullOrEmpty()) cart.productsQuantity[it]!! else cart.productsQuantity[it]!!/100
                holder.list_item_product_price.text = "${String.format("%.2f",(it.price.fractional.toFloat() / 100) * quantity)}€"
                holder.list_item_product_picture.load("http://lejardindecaro.fr${it.image.url}", placeholder = R.drawable.logo_jardin_caro)
                holder.list_item_product_quantity.setValue(cart.productsQuantity[it]!!)
                holder.list_item_product_quantity.setMax(999)
                holder.list_item_product_quantity.setAsyncChangeRequestListener(object :
                    QuantityFlatView.AsyncChangeRequestListener {
                    override fun onQuantityAddRequested(newValue: Int) {
                        cartCallback?.onQuantityAddRequested(newValue, it.id!!)
                    }

                    override fun onQuantityRemoveRequested(newValue: Int) {
                        cartCallback?.onQuantityRemoveRequested(newValue, it.id!!)
                    }
                })
                holder.list_item_product_picture.alpha = 1f
                holder.list_item_product_quantity.visibility = View.VISIBLE
                holder.list_item_product_promo.visibility = View.VISIBLE
                holder.list_item_product_price.visibility = View.VISIBLE

            }
        }
    }

    fun getProductAtPosition(position: Int): Product {
        return items[position]
    }

    fun setPositionLoading(product: Product) {
        val element = items.indexOfFirst { it.id == product.id }
        if (element != -1) {
            loadingIndex = element
            notifyItemChanged(element)
        }
    }

    fun clearLoadingAndNotifyDataSetChanged() {
        loadingIndex = null
        notifyDataSetChanged()
    }

    inner class ViewHolderProductInterface(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer


    interface CartCallback {
        fun onQuantityAddRequested(value: Int, id: String)

        fun onQuantityRemoveRequested(value: Int, id: String)

        fun onCartClearClicked()

        fun onCartValidateClicked()

        fun onEmptyCartContinueClicked()
    }
}
