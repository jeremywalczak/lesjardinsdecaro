package com.jekro.lesjardindecaro.ui

import com.auchan.uikit.mvp.AbsPresenter

class HomePagePresenter (
    override var view: HomePageContract.View,
    private var couponRepo: CouponsRepository
) : AbsPresenter<HomePageContract.View, HomePageContract.Presenter>(),
    HomePageContract.Presenter {

    override fun start() {
        addSubscription(
            couponRepo.getDateCoupons(),
            view::displayResult,
            view::displayError
        )
    }
}