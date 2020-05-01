package com.jekro.lesjardindecaro.ui.cart

import com.auchan.uikit.mvp.AbsPresenter
import com.jekro.lesjardindecaro.repository.ConfigurationRepository

class CartPresenter (
    override var view: CartContract.View,
    override var  configurationRepo: ConfigurationRepository
) : AbsPresenter<CartContract.View, CartContract.Presenter>(),
    CartContract.Presenter {

    override fun updateViewButtons() {

    }

    override fun start() {
    }
}