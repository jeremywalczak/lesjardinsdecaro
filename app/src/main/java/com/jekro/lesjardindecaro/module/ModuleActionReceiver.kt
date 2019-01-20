package com.auchan.uikit.module

interface ModuleActionReceiver {
    fun <T> onModuleActionReceived(action: String, data: T? = null)
}