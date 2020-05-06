package com.jekro.lesjardindecaro.ui.cart

import com.jekro.lesjardindecaro.model.Cart
import com.jekro.lesjardindecaro.mvp.BasePresenter
import com.jekro.lesjardindecaro.mvp.BaseView
import com.jekro.lesjardindecaro.repository.ConfigurationRepository

interface ValidateCartContract {
    interface Presenter : BasePresenter<View> {
        var  configurationRepo: ConfigurationRepository
    }

    interface View : BaseView<Presenter> {
        fun displayResult(cart: Cart)
    }
}