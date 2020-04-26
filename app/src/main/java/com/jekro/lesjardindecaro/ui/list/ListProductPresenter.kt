package com.jekro.lesjardindecaro.ui.list

import com.auchan.uikit.mvp.AbsPresenter
import com.jekro.lesjardindecaro.model.AutoCompleteEntry
import com.jekro.lesjardindecaro.model.AutoCompleteViewType
import com.jekro.lesjardindecaro.model.Category
import com.jekro.lesjardindecaro.model.Product

class ListProductPresenter(
    override var view: ListProductContract.View
) : AbsPresenter<ListProductContract.View, ListProductContract.Presenter>(),
    ListProductContract.Presenter {

    override var initialEntries = mutableListOf<Product>()

    override var hashProductCategories: HashMap<Product, List<Category>> = hashMapOf()

    override var filtersType: MutableList<String> = mutableListOf()

    private var currentFilters: List<String>? = null

    override fun start() {
    }

    override fun buildFiltersType(products: ArrayList<Product>) {
        filtersType = mutableListOf()
        products?.forEach {
            hashProductCategories[it]?.forEach { category ->
                if (!filtersType.contains(category.title))
                    filtersType.add(category.title)
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
                return initialEntries.filter {it.title != null && formatSearch(it.title).contains(search) }
        } else {
            if (search.isNullOrEmpty()) {
                type.forEach { myType ->
                    initialEntries.filter {hashProductCategories[it]?.firstOrNull { category -> category.title == myType } != null }?.forEach {
                        if (!entriesSortedAndFiltered.contains(it))
                            entriesSortedAndFiltered.add(it)
                    }
                }
                return entriesSortedAndFiltered
            } else {
                type.forEach { myType ->
                    initialEntries.filter {hashProductCategories[it]?.firstOrNull { category -> category.title == myType } != null && it.title != null && formatSearch(it.title).contains(search) }?.forEach {
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
            initialEntries.filter {it.title != null && formatSearch(it.title).contains(search) }?.forEach { productEntry ->
                result.add(
                    AutoCompleteEntry(
                        productEntry.title?.replace(search, "<b>$search</b>"),
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