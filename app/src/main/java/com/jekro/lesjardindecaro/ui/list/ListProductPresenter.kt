package com.jekro.lesjardindecaro.ui.list

import com.auchan.uikit.mvp.AbsPresenter

class ListProductPresenter(
    override var view: ListProductContract.View
) : AbsPresenter<ListProductContract.View, ListProductContract.Presenter>(),
    ListProductContract.Presenter {

    override fun start() {
    }
}