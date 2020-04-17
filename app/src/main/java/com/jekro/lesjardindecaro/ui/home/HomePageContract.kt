package com.jekro.lesjardindecaro.ui.home

import com.auchan.uikit.mvp.BasePresenter
import com.auchan.uikit.mvp.BaseView
import com.jekro.lesjardindecaro.model.Product

interface HomePageContract {
    interface Presenter : BasePresenter<View>

    interface View : BaseView<Presenter> {
        fun displayResult(products: List<Product>)
    }
}