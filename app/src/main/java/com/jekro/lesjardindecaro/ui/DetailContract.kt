package com.jekro.lesjardindecaro.ui

import com.jekro.lesjardindecaro.mvp.BasePresenter
import com.jekro.lesjardindecaro.mvp.BaseView

interface DetailContract {
    interface Presenter : BasePresenter<View>

    interface View : BaseView<Presenter> {
        fun displayResult()
    }
}