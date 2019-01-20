package com.jekro.lesjardindecaro.ui

import com.auchan.uikit.module.ModuleInteractor
import com.auchan.uikit.mvp.AbsFragment
import com.jekro.lesjardindecaro.R
import com.jekro.lesjardindecaro.model.Configuration
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class HomePageFragment : AbsFragment<HomePageContract.View, HomePageContract.Presenter>(),
HomePageContract.View {

    override fun displayResult(listCoupons: List<Configuration>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setRequesting(requesting: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun displayError(throwable: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLayoutId(): Int = R.layout.fragment_homepage

    override val presenter: HomePageContract.Presenter by inject { parametersOf(this) }
    override val moduleInteractor: ModuleInteractor by inject()
}