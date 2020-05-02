package com.jekro.lesjardindecaro.module

interface ModuleActionReceiver {
    fun <T> onModuleActionReceived(action: String, data: T? = null)
}