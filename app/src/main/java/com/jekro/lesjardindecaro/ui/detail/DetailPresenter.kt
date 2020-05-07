package com.jekro.lesjardindecaro.ui.detail

import com.jekro.lesjardindecaro.mvp.AbsPresenter
import com.jekro.lesjardindecaro.repository.ConfigurationRepository
import com.jekro.lesjardindecaro.ui.detail.DetailContract

class DetailPresenter (
    override var view: DetailContract.View,
    override var  configurationRepo: ConfigurationRepository
) : AbsPresenter<DetailContract.View, DetailContract.Presenter>(),
    DetailContract.Presenter {

    override fun start() {
    }
}