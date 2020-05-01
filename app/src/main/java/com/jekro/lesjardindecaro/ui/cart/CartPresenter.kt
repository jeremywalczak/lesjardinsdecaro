package com.jekro.lesjardindecaro.ui.cart

import com.auchan.uikit.mvp.AbsPresenter
import com.jekro.lesjardindecaro.model.Cart
import com.jekro.lesjardindecaro.repository.ConfigurationRepository

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

    override fun updateQuantity(id: Int, value: String) {
        val products = cart?.productsQuantity?.keys?.firstOrNull { it.id == id.toString() }
    }
}