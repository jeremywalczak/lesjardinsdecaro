package com.jekro.lesjardindecaro

import android.content.Context
import android.util.Log
import com.squareup.picasso.OkHttpDownloader
import com.squareup.picasso.Picasso


class CustomPicasso constructor(context: Context) {
    private val LOG_TAG: String = CustomPicasso::class.java.getSimpleName()
    private var hasCustomPicassoSingletonInstanceSet = false

    fun with(context: Context?): Picasso? {
        if (hasCustomPicassoSingletonInstanceSet) return Picasso.with(context)
        try {
            Picasso.setSingletonInstance(null)
        } catch (e: IllegalStateException) {
            Log.w(
                LOG_TAG, "-> Default singleton instance already present" +
                        " so CustomPicasso singleton cannot be set. Use CustomPicasso.getNewInstance() now."
            )
            return Picasso.with(context)
        }
        val picasso = Picasso.Builder(context).downloader(OkHttpDownloader(context))
            .build()
        Picasso.setSingletonInstance(picasso)
        Log.w(
            LOG_TAG, "-> CustomPicasso singleton set to Picasso singleton." +
                    " In case if you need Picasso singleton in future then use Picasso.Builder()"
        )
        hasCustomPicassoSingletonInstanceSet = true
        return picasso
    }

    fun getNewInstance(context: Context?): Picasso? {
        Log.w(
            LOG_TAG, "-> Do not forget to call customPicasso.shutdown()" +
                    " to avoid memory leak"
        )
        return Picasso.Builder(context).downloader(OkHttpDownloader(context))
            .build()
    }
}