package com.jekro.lesjardindecaro.ui.home

import com.auchan.uikit.mvp.BasePresenter
import com.auchan.uikit.mvp.BaseView
import com.jekro.lesjardindecaro.model.Category
import com.jekro.lesjardindecaro.model.Configuration
import com.jekro.lesjardindecaro.model.Product

interface HomePageContract {
    interface Presenter : BasePresenter<View> {
        var configuration: Configuration?
        var hashProductCategories: HashMap<Product, List<Category>>
    }

    interface View : BaseView<Presenter> {
        fun displayResult(configuration: Configuration)
    }
}