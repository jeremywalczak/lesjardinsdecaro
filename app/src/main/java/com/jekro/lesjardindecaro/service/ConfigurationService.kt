package com.jekro.lesjardindecaro.service

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jekro.lesjardindecaro.model.Configuration
import com.jekro.lesjardindecaro.model.Product
import io.reactivex.Single
import retrofit2.http.GET

interface ConfigurationService  {
    @GET("/products.json")
    fun getConfiguration(): Single<Configuration>
}