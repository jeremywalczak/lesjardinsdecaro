package com.auchan.uikit.mvp

import io.reactivex.Single
import kotlin.reflect.KFunction1
import kotlin.reflect.KFunction2

interface BasePresenter<V> {
    fun destroyView()

    fun onDestroy()

    fun start()

    fun <OT> addSubscription(
        observable: Single<OT>,
        successFunc: KFunction1<OT, Unit>,
        errorFunc: KFunction1<Throwable, Unit>,
        isInit: Boolean = false
    )

    fun <OT> addQuietSubscription(
        observable: Single<OT>,
        successFunc: KFunction1<OT, Unit>,
        errorFunc: KFunction1<Throwable, Unit>,
        isInit: Boolean
    )

    fun <OT, PT> addSubscription(
        observable: Single<OT>,
        successFunc: KFunction2<OT, PT, Unit>,
        errorFunc: KFunction1<Throwable, Unit>,
        isInit: Boolean,
        params: PT
    )

    fun retry()
}