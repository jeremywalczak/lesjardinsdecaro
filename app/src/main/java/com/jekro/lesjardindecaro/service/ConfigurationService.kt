package com.jekro.lesjardindecaro.service

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jekro.lesjardindecaro.model.Product
import io.reactivex.Single
import retrofit2.http.GET

interface ConfigurationService  {
    @GET("/Products")
    fun getProducts(): Single<List<Product>>
}