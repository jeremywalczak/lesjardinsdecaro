package com.jekro.lesjardindecaro.ui.user

import com.jekro.lesjardindecaro.mvp.BasePresenter
import com.jekro.lesjardindecaro.mvp.BaseView
import com.jekro.lesjardindecaro.repository.ConfigurationRepository

interface UserContract {
    interface Presenter : BasePresenter<View> {
        var  configurationRepo: ConfigurationRepository
    }

    interface View : BaseView<Presenter> {
        fun displayResult()
    }
}