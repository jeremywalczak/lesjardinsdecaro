package com.auchan.uikit.mvp

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.atomic.AtomicInteger
import kotlin.reflect.KFunction1

abstract class AbsPresenter<V : BaseView<P>, P : BasePresenter<V>> : BasePresenter<V> {
    open lateinit var view: V

    private var initDisposables: CompositeDisposable = CompositeDisposable()
    private val initLatch: AtomicInteger = AtomicInteger()

    private var disposables: CompositeDisposable = CompositeDisposable()
    private val latch: AtomicInteger = AtomicInteger()

    override fun onCreate() {

    }

    override fun destroy() {
        disposables.clear()
        disposables.dispose()
        initDisposables.clear()
        initDisposables.dispose()
    }

    override fun <T> addSubscription(
        observable: Single<T>,
        successFunc: KFunction1<T, Unit>,
        errorFunc: KFunction1<Throwable, Unit>,
        isInit: Boolean
    ) {
        setRequesting(isInit,true)

        (if(isInit) initLatch else latch).incrementAndGet()

        (if(isInit)initDisposables else disposables).add(
            observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        setRequesting(isInit,(if(isInit) initLatch else latch).decrementAndGet() != 0)
                        successFunc.invoke(result)
                    },
                    { error ->
                        setRequesting(isInit,(if(isInit) initLatch else latch).decrementAndGet() != 0)
                        errorFunc.invoke(error)
                    })
        )
    }

    private fun setRequesting(isInit: Boolean, pending: Boolean) {
        if (isInit)
            view.setInitRequesting(pending)
        else
            view.setRequesting(pending)
    }
}