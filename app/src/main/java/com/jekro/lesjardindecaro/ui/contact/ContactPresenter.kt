package com.jekro.lesjardindecaro.ui.contact

import com.jekro.lesjardindecaro.mvp.AbsPresenter
import com.jekro.lesjardindecaro.repository.ConfigurationRepository

class ContactPresenter (
    override var view: ContactContract.View,
    override var  configurationRepo: ConfigurationRepository
) : AbsPresenter<ContactContract.View, ContactContract.Presenter>(),
    ContactContract.Presenter {

    override fun start() {
    }
}