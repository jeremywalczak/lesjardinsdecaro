package com.jekro.lesjardindecaro.ui.list

import com.auchan.uikit.mvp.BasePresenter
import com.auchan.uikit.mvp.BaseView
import com.jekro.lesjardindecaro.model.Product

class ListProductContract {

    interface Presenter : BasePresenter<View> {
        var filtersType: MutableList<String>

        fun buildFiltersType(products: ArrayList<Product>)

        fun filter(type: List<String>?, search: String?)

        fun searchSuggestions(search: String)
    }

    interface View : BaseView<Presenter> {
        fun displayResult(products: List<Product>)
    }
}