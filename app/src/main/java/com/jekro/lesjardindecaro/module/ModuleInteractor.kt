package com.jekro.lesjardindecaro.module

class ModuleInteractor {
    var containerId: Int = 0
        private set
    var actionReceiver: ModuleActionReceiver? = null
        private set

    fun init(containerId: Int, actionReceiver: ModuleActionReceiver? = null) {
        this.containerId = containerId
        this.actionReceiver = actionReceiver
    }
}