package com.jekro.lesjardindecaro.ui.list

import android.os.Bundle
import com.auchan.uikit.module.ModuleInteractor
import com.auchan.uikit.mvp.AbsFragment
import com.jekro.lesjardindecaro.R
import com.jekro.lesjardindecaro.model.Product
import com.jekro.lesjardindecaro.ui.home.HomePageFragment.Companion.PRODUCTS
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class ListProductFragment : AbsFragment<ListProductContract.View, ListProductContract.Presenter>(),
    ListProductContract.View {
    override fun displayResult(products: List<Product>) {
    }

    override fun setRequesting(requesting: Boolean) {
    }

    override fun displayError(throwable: Throwable) {
    }

    override fun getLayoutId(): Int = R.layout.fragment_list

    override val presenter: ListProductContract.Presenter by inject { parametersOf(this) }
    override val moduleInteractor: ModuleInteractor by inject()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val products = activity?.intent?.getParcelableArrayListExtra<Product>(PRODUCTS)
        products
    }

}