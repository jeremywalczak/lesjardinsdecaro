package com.jekro.lesjardindecaro.init

import android.app.Application
import android.graphics.Bitmap
import com.jakewharton.picasso.OkHttp3Downloader
import com.jekro.lesjardindecaro.SVGRequestHandler
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.startKoin
import org.koin.android.logger.AndroidLogger


class InitApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin(androidContext = this.applicationContext, modules = listOf(initModule), logger = AndroidLogger())

        Picasso.setSingletonInstance(
            Picasso.Builder(this.applicationContext)
                .downloader(OkHttp3Downloader(this.applicationContext))
                .addRequestHandler(SVGRequestHandler())
                .defaultBitmapConfig(Bitmap.Config.RGB_565)
                .build()
        )
    }
}