package com.jekro.lesjardindecaro.ui.cart

import com.jekro.lesjardindecaro.model.Cart
import com.jekro.lesjardindecaro.model.Product
import com.jekro.lesjardindecaro.mvp.BasePresenter
import com.jekro.lesjardindecaro.mvp.BaseView
import com.jekro.lesjardindecaro.repository.ConfigurationRepository

interface CartContract {
    interface Presenter : BasePresenter<View> {
        fun updateViewButtons()

        var  configurationRepo: ConfigurationRepository

        var cart: Cart?

        fun updateQuantity(id: String, value: Int)

        fun deleteProduct(id: String)

        fun forceUpdateQuantity(id: String, value: Int)
    }

    interface View : BaseView<Presenter> {
        fun displayResult(cart: Cart)

        fun displayPopUpDelete(id: String, quantity: Int)

        fun requestDeleteProduct(product: Product)
    }
}