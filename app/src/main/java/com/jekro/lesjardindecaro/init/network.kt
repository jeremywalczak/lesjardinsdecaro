package com.jekro.lesjardindecaro.init

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


val networkModule = module {

    single<Retrofit>(named("retrofit")) {
        val locale = get<Locale>(named("locale"))
        val baseURL = get<String>(named("backoffice"))

        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val headerInterceptor = Interceptor {
            val request = it.request().newBuilder()
                .addHeader("Accept-Language", locale.toLanguageTag())

            Log.d("OkHttp", "Accept-Language :  ${locale.toLanguageTag()}")
            it.proceed(request.build())
        }

        val code401Interceptor = Interceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)
            if (response.code() == 401) {
                //action to check in core
            }
            response
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logInterceptor)
            .addInterceptor(headerInterceptor)
            .addInterceptor(code401Interceptor)
            .build()

        Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .baseUrl(baseURL)
            .build()
    }
}