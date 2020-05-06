package com.jekro.lesjardindecaro.ui.cart

import com.jekro.lesjardindecaro.mvp.AbsPresenter
import com.jekro.lesjardindecaro.repository.ConfigurationRepository

class ValidateCartPresenter (
    override var view: ValidateCartContract.View,
    override var  configurationRepo: ConfigurationRepository
) : AbsPresenter<ValidateCartContract.View, ValidateCartContract.Presenter>(),
    ValidateCartContract.Presenter {

    override fun start() {
    }
}