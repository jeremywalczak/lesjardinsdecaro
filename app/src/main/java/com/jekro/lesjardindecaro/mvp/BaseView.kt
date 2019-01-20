package com.auchan.uikit.mvp

interface BaseView<P : BasePresenter<*>> {

    fun setRequesting(requesting: Boolean)

    fun setInitRequesting(requesting: Boolean) = setRequesting(requesting)

    fun displayError(throwable: Throwable)

    fun displayInitError(throwable: Throwable) = displayError(throwable)

}