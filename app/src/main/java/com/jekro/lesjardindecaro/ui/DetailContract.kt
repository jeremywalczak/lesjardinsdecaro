package com.jekro.lesjardindecaro.ui

import com.jekro.lesjardindecaro.mvp.BasePresenter
import com.jekro.lesjardindecaro.mvp.BaseView
import com.jekro.lesjardindecaro.repository.ConfigurationRepository

interface DetailContract {
    interface Presenter : BasePresenter<View> {
        var  configurationRepo: ConfigurationRepository
    }

    interface View : BaseView<Presenter> {
        fun displayResult()
    }
}