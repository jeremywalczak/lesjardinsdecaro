package com.jekro.lesjardindecaro.ui.home

import com.auchan.uikit.mvp.AbsPresenter
import com.jekro.lesjardindecaro.model.Configuration
import com.jekro.lesjardindecaro.repository.ConfigurationRepository

class HomePagePresenter (
    override var view: HomePageContract.View,
    private var  configurationRepo: ConfigurationRepository
) : AbsPresenter<HomePageContract.View, HomePageContract.Presenter>(),
    HomePageContract.Presenter {

    override var configuration: Configuration? = null

    override fun start() {
        addSubscription(
            configurationRepo.getConfiguration().map {
                configuration = it
                it
            },
            view::displayResult,
            view::displayError
        )
    }
}