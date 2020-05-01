package com.jekro.lesjardindecaro.ui.home

import android.util.Log
import com.auchan.uikit.mvp.AbsPresenter
import com.jekro.lesjardindecaro.model.Category
import com.jekro.lesjardindecaro.model.Configuration
import com.jekro.lesjardindecaro.model.Product
import com.jekro.lesjardindecaro.repository.ConfigurationRepository
import java.util.concurrent.TimeUnit

class HomePagePresenter (
    override var view: HomePageContract.View,
    private var  configurationRepo: ConfigurationRepository
) : AbsPresenter<HomePageContract.View, HomePageContract.Presenter>(),
    HomePageContract.Presenter {

    override var configuration: Configuration? = null
    override var hashProductCategories: HashMap<Product, List<Category>> = hashMapOf()

    override fun start() {
        addSubscription(
            configurationRepo.getConfiguration().map {
                configuration = it
                configuration?.products?.forEach { product ->
                    hashProductCategories[product] = getCategoriesFromProduct(product)
                }
                it
            },
            view::displayResult,
            view::displayError,
            true
        )
    }

    override fun retry() {
        start()
    }

    private fun getCategoriesFromProduct(product: Product): List<Category> {
        val categories = arrayListOf<Category>()
        val category = configuration?.categories?.firstOrNull { it.id == product.categoryId }
        if (category != null) {
            categories.apply {
                add(category)
            }
            val rootCategory = configuration?.categories?.firstOrNull { it.id == category.categoryId}
            if (rootCategory != null)
                categories.apply {
                    add(rootCategory)
                }
            else
                Log.d("GET_CAT_FROM_PRODUCT", "rootCategory non trouvé pour category.id ${category.id} et product.categoryId ${product.categoryId}")
        }
        return categories
    }

}