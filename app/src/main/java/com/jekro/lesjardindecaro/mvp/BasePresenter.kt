package com.auchan.uikit.mvp

import io.reactivex.Single
import kotlin.reflect.KFunction1

interface BasePresenter<V> {
    fun destroy()

    fun onCreate()

    fun start()

    fun <T> addSubscription(
        observable: Single<T>,
        successFunc: KFunction1<T, Unit>,
        errorFunc: KFunction1<Throwable, Unit>,
        isInit : Boolean = false
    )
}