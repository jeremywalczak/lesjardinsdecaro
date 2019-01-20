package com.jekro.lesjardindecaro.ui

import com.auchan.uikit.mvp.BasePresenter
import com.auchan.uikit.mvp.BaseView

interface HomePageContract {
    interface Presenter : BasePresenter<View>

    interface View : BaseView<Presenter> {
        fun displayResult()
    }
}