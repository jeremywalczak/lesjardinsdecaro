package com.jekro.lesjardindecaro.service

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jekro.lesjardindecaro.model.Products
import io.reactivex.Single

class ConfigurationService  {
    fun getConfiguration(): Single<List<Products>> {
        val type = object : TypeToken<List<Products>>() {}.type
        return Single.just(Gson().fromJson(getFileContent("produits.json"), type))
    }

    private fun getFileContent(file: String): String {
        return ConfigurationService::class.java.classLoader?.getResource(file)!!.readText()
    }
}