package com.jekro.lesjardindecaro.ui.list

import com.auchan.uikit.mvp.BasePresenter
import com.auchan.uikit.mvp.BaseView
import com.jekro.lesjardindecaro.model.Product

class ListProductContract {
    interface Presenter : BasePresenter<View>

    interface View : BaseView<Presenter> {
        fun displayResult(products: List<Product>)
    }
}