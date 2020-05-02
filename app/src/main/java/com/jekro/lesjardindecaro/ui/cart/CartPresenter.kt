package com.jekro.lesjardindecaro.ui.cart

import com.jekro.lesjardindecaro.mvp.AbsPresenter
import com.jekro.lesjardindecaro.model.Cart
import com.jekro.lesjardindecaro.repository.ConfigurationRepository
import java.util.concurrent.TimeUnit

class CartPresenter (
    override var view: CartContract.View,
    override var  configurationRepo: ConfigurationRepository
) : AbsPresenter<CartContract.View, CartContract.Presenter>(),
    CartContract.Presenter {

    override var cart: Cart? = null

    override fun updateViewButtons() {

    }

    override fun start() {
    }

    override fun updateQuantity(id: String, quantity: Int) {
        if (quantity == 0) {
            view.displayPopUpDelete(id, quantity)
        } else {
            forceUpdateQuantity(id, quantity)
        }
    }

    override fun deleteProduct(id: String) {
        updateQuantity(id, 0)
    }

    override fun forceUpdateQuantity(id: String, quantity: Int) {
        val product = cart?.productsQuantity?.keys?.firstOrNull { it.id == id }
        product?.let {
            if (quantity == 0) {
                cart!!.productsQuantity.remove(it)
            } else {
                cart!!.productsQuantity[it] = quantity
            }
            cart!!.amountTotal = 0F
            cart!!.productsQuantity.keys.forEach { product ->
                val quantity = if (product.unity.isNullOrEmpty()) cart!!.productsQuantity[product]!! else cart!!.productsQuantity[product]!!/100
                cart!!.amountTotal += ((product.price.fractional.toFloat() / 100) * quantity)
            }
            configurationRepo.saveCart(cart!!)
            view.displayResult(cart!!)
        }
    }

}