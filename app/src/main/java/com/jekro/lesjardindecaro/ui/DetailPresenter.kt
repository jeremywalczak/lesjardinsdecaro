package com.jekro.lesjardindecaro.ui

import com.jekro.lesjardindecaro.mvp.AbsPresenter
import com.jekro.lesjardindecaro.repository.ConfigurationRepository

class DetailPresenter (
    override var view: DetailContract.View,
    override var  configurationRepo: ConfigurationRepository
) : AbsPresenter<DetailContract.View, DetailContract.Presenter>(),
    DetailContract.Presenter {

    override fun start() {
    }
}