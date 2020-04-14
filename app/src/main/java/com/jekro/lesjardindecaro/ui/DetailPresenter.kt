package com.jekro.lesjardindecaro.ui

import com.auchan.uikit.mvp.AbsPresenter
import com.jekro.lesjardindecaro.repository.ConfigurationRepository
import com.jekro.lesjardindecaro.ui.home.HomePageContract

class DetailPresenter (
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