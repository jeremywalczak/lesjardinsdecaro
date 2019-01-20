package com.jekro.lesjardindecaro.init

import android.app.Application
import org.koin.android.ext.android.startKoin
import org.koin.android.logger.AndroidLogger

class InitApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(androidContext = this, modules = listOf(initModule), logger = AndroidLogger())
    }
}