package com.jekro.lesjardindecaro.init

import android.app.Application
import android.graphics.Bitmap
import com.jakewharton.picasso.OkHttp3Downloader
import com.jekro.lesjardindecaro.SVGRequestHandler
import com.squareup.picasso.Picasso
import io.paperdb.Paper
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin


class InitApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Paper.init(this)

        startKoin{
            androidLogger()
            androidContext(this@InitApplication)
            modules(initModule)
            modules(networkModule)
        }

        /*Picasso.setSingletonInstance(
            Picasso.Builder(this.applicationContext)
                .downloader(OkHttp3Downloader(this.applicationContext))
                .addRequestHandler(SVGRequestHandler())
                .defaultBitmapConfig(Bitmap.Config.RGB_565)
                .build()
        )*/
    }
}