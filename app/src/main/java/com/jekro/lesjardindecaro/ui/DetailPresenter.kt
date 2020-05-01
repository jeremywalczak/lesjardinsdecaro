package com.jekro.lesjardindecaro.ui

import com.auchan.uikit.mvp.AbsPresenter
import com.jekro.lesjardindecaro.repository.ConfigurationRepository
import com.jekro.lesjardindecaro.ui.home.HomePageContract

class DetailPresenter (
    override var view: DetailContract.View,
    override var  configurationRepo: ConfigurationRepository
) : AbsPresenter<DetailContract.View, DetailContract.Presenter>(),
    DetailContract.Presenter {

    override fun start() {
    }
}