package com.jekro.lesjardindecaro.mvp

interface BaseView<P : BasePresenter<*>> {

    fun setRequesting(requesting: Boolean)

    fun setInitRequesting(requesting: Boolean) = setRequesting(requesting)

    fun displayError(throwable: Throwable)

    fun displayInitError(throwable: Throwable) = displayError(throwable)

    fun getLoadingContainerId(): Int? = null

    fun getCartView(): Int? = null

    fun setState(state: String) {}

    fun getContentContainerId(): Int? = null

    fun getEmptyStateContainerId(): Int? = null

    fun getInitErrorContainerId(): Int? = null

}