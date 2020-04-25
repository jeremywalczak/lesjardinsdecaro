package com.jekro.lesjardindecaro.ui.list

import com.auchan.uikit.mvp.BasePresenter
import com.auchan.uikit.mvp.BaseView
import com.jekro.lesjardindecaro.model.AutoCompleteEntry
import com.jekro.lesjardindecaro.model.Category
import com.jekro.lesjardindecaro.model.Product

class ListProductContract {

    interface Presenter : BasePresenter<View> {
        var filtersType: MutableList<String>
        var initialEntries : MutableList<Product>
        var hashProductCategories: HashMap<Product, List<Category>>

        fun buildFiltersType(products: ArrayList<Product>)

        fun filter(type: List<String>?, search: String?)

        fun searchSuggestions(search: String)

        fun formatSearch(search: String): String

        fun getProductFiltered(type: List<String>?, search: String?) : List<Product>
    }

    interface View : BaseView<Presenter> {
        fun displayResult(products: List<Product>)

        fun displaySuggestions(suggestions: List<AutoCompleteEntry>, search: String)

        fun displayProductsFiltered(products: List<Product>)
    }
}