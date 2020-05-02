package com.jekro.lesjardindecaro.mvp

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.atomic.AtomicInteger
import kotlin.reflect.KFunction1
import kotlin.reflect.KFunction2

abstract class AbsPresenter<V : BaseView<P>, P : BasePresenter<V>> : BasePresenter<V> {
    open lateinit var view: V

    private var initDisposables: CompositeDisposable = CompositeDisposable()
    private val initLatch: AtomicInteger = AtomicInteger()

    private var disposables: CompositeDisposable = CompositeDisposable()
    private val latch: AtomicInteger = AtomicInteger()

    private var lastObservable: Single<*>? = null
    private var lastSuccessFunc: KFunction1<*, *>? = null
    private var lastSuccessFunc2: KFunction2<*, *, *>? = null
    private var lastErrorFunc: KFunction1<Throwable, Unit>? = null
    private var lastIsInit: Boolean = false
    private var lastParam: Any? = null

    override fun destroyView() {
        disposables.clear()
        initDisposables.clear()
    }

    override fun onDestroy(){
        disposables.dispose()
        initDisposables.dispose()
        lastObservable = null
        lastSuccessFunc = null
        lastSuccessFunc2 = null
        lastErrorFunc = null
        lastIsInit = false
    }

    override fun <OT, PT> addSubscription(
        observable: Single<OT>,
        successFunc: KFunction2<OT, PT, Unit>,
        errorFunc: KFunction1<Throwable, Unit>,
        isInit: Boolean,
        params: PT
    ) {
        lastObservable = observable
        lastSuccessFunc = null
        lastSuccessFunc2 = successFunc
        lastErrorFunc = null
        lastIsInit = false


        setRequesting(isInit, true)

        (if (isInit) initLatch else latch).incrementAndGet()

        (if (isInit) initDisposables else disposables).add(
            observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        setRequesting(
                            isInit,
                            (if (isInit) initLatch else latch).decrementAndGet() != 0
                        )
                        successFunc.invoke(it, params)
                    },
                    {
                        setRequesting(
                            isInit,
                            (if (isInit) initLatch else latch).decrementAndGet() != 0
                        )
                        errorFunc.invoke(it)
                    })
        )
    }

    override fun <PT> addSubscription(
        observable: Single<PT>,
        successFunc: KFunction1<PT, Unit>,
        errorFunc: KFunction1<Throwable, Unit>,
        isInit: Boolean
    ) {
        lastObservable = observable
        lastSuccessFunc = successFunc
        lastSuccessFunc2 = null
        lastErrorFunc = errorFunc
        lastIsInit = isInit

        setRequesting(isInit, true)

        (if (isInit) initLatch else latch).incrementAndGet()

        (if (isInit) initDisposables else disposables).add(
            observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        setRequesting(isInit, (if (isInit) initLatch else latch).decrementAndGet() != 0)
                        successFunc.invoke(it)
                    },
                    {
                        setRequesting(isInit, (if (isInit) initLatch else latch).decrementAndGet() != 0)
                        errorFunc.invoke(it)
                    })
        )
    }

    override fun <PT> addQuietSubscription(
        observable: Single<PT>,
        successFunc: KFunction1<PT, Unit>,
        errorFunc: KFunction1<Throwable, Unit>,
        isInit: Boolean
    ) {
        lastObservable = observable
        lastSuccessFunc = successFunc
        lastSuccessFunc2 = null
        lastErrorFunc = errorFunc
        lastIsInit = isInit

        (if (isInit) initLatch else latch).incrementAndGet()

        (if (isInit) initDisposables else disposables).add(
            observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        successFunc.invoke(it)
                    },
                    {
                        errorFunc.invoke(it)
                    })
        )
    }

    override fun retry() {
        lastSuccessFunc?.let {
            addSubscription(
                lastObservable!!,
                it as KFunction1<Any, Unit>,
                lastErrorFunc!!,
                lastIsInit
            )
        }
        lastSuccessFunc2?.let {
            addSubscription(
                lastObservable!!,
                it as KFunction2<Any, Any, Unit>,
                lastErrorFunc!!,
                lastIsInit,
                lastParam!!
            )
        }
    }

    private fun setRequesting(isInit: Boolean, pending: Boolean) {
        if (isInit)
            view.setInitRequesting(pending)
        else
            view.setRequesting(pending)
    }
}