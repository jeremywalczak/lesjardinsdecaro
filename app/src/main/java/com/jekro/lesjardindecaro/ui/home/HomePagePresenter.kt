package com.jekro.lesjardindecaro.ui.home

import com.auchan.uikit.mvp.AbsPresenter
import com.jekro.lesjardindecaro.repository.ConfigurationRepository

class HomePagePresenter (
    override var view: HomePageContract.View,
    private var  configurationRepo: ConfigurationRepository
) : AbsPresenter<HomePageContract.View, HomePageContract.Presenter>(),
    HomePageContract.Presenter {

    override fun start() {
        addSubscription(
            configurationRepo.getConfiguration(),
            view::displayResult,
            view::displayError
        )
    }
}