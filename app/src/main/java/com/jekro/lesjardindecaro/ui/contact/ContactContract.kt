package com.jekro.lesjardindecaro.ui.contact

import com.jekro.lesjardindecaro.mvp.BasePresenter
import com.jekro.lesjardindecaro.mvp.BaseView
import com.jekro.lesjardindecaro.repository.ConfigurationRepository

class ContactContract {
    interface Presenter : BasePresenter<View> {
        var  configurationRepo: ConfigurationRepository
    }

    interface View : BaseView<Presenter> {
        fun displayResult()
    }
}