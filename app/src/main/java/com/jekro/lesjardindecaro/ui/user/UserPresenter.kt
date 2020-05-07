package com.jekro.lesjardindecaro.ui.user

import com.jekro.lesjardindecaro.mvp.AbsPresenter
import com.jekro.lesjardindecaro.repository.ConfigurationRepository

class UserPresenter (
    override var view: UserContract.View,
    override var  configurationRepo: ConfigurationRepository
) : AbsPresenter<UserContract.View, UserContract.Presenter>(),
    UserContract.Presenter {

    override fun start() {
    }
}