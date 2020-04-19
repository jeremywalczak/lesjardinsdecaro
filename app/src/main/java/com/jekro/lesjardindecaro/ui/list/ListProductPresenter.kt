package com.jekro.lesjardindecaro.ui.list

import com.auchan.uikit.mvp.AbsPresenter
import com.jekro.lesjardindecaro.model.Product

class ListProductPresenter(
    override var view: ListProductContract.View
) : AbsPresenter<ListProductContract.View, ListProductContract.Presenter>(),
    ListProductContract.Presenter {

    override var filtersType: MutableList<String> = mutableListOf()

    private var currentFilters: List<String>? = null

    override fun start() {
    }

    override fun buildFiltersType(products: ArrayList<Product>) {
        filtersType = mutableListOf()
        products?.forEach {
            it.types?.forEach { type ->
                if (!filtersType.contains(type))
                    filtersType.add(type)
            }
        }
    }

    override fun filter(type: List<String>?, search: String?) {
        currentFilters = type
        //view.displayCouponsFiltered(ArrayList(getCouponsSortedAndFiltered(null, null, search)))
    }

    override fun searchSuggestions(search: String) {

    }

}