package com.jekro.lesjardindecaro.ui.list

import com.auchan.uikit.mvp.AbsPresenter
import com.jekro.lesjardindecaro.model.AutoCompleteEntry
import com.jekro.lesjardindecaro.model.AutoCompleteViewType
import com.jekro.lesjardindecaro.model.Product

class ListProductPresenter(
    override var view: ListProductContract.View
) : AbsPresenter<ListProductContract.View, ListProductContract.Presenter>(),
    ListProductContract.Presenter {

    override var initialEntries = mutableListOf<Product>()

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
        view.displayProductsFiltered(ArrayList(getProductFiltered(type, search)))
    }

    override fun getProductFiltered(
        type: List<String>?,
        search: String?
    ): List<Product> {
        val entriesSortedAndFiltered = arrayListOf<Product>()
        if (type.isNullOrEmpty()) {
            if (search.isNullOrEmpty())
                return initialEntries
            else
                return initialEntries.filter {formatSearch(it.title).contains(search) }
        } else {
            if (search.isNullOrEmpty()) {
                type.forEach { myType ->
                    initialEntries.filter {it.types!!.contains(myType) }?.forEach {
                        if (!entriesSortedAndFiltered.contains(it))
                            entriesSortedAndFiltered.add(it)
                    }
                }
                return entriesSortedAndFiltered
            } else {
                type.forEach { myType ->
                    initialEntries.filter {it.types!!.contains(myType) && formatSearch(it.title).contains(search) }?.forEach {
                        if (!entriesSortedAndFiltered.contains(it))
                            entriesSortedAndFiltered.add(it)
                    }
                }
                return entriesSortedAndFiltered
            }
        }

    }

    override fun searchSuggestions(search: String) {
        val result = mutableListOf<AutoCompleteEntry>()
        if (!search.isNullOrEmpty()) {
            initialEntries.filter {formatSearch(it.title).contains(search) }?.forEach { productEntry ->
                result.add(
                    AutoCompleteEntry(
                        productEntry.title.replace(search, "<b>$search</b>"),
                        productEntry.title,
                        productEntry,
                        AutoCompleteViewType.ITEM
                    )
                )
            }
        }
        view.displaySuggestions(result, search)
    }

    override fun formatSearch(search: String): String {
        return search.replace("é", "e").replace("è", "e").replace("à", "a").replace("â", "a")
            .replace("ç", "c").toLowerCase()
    }
}