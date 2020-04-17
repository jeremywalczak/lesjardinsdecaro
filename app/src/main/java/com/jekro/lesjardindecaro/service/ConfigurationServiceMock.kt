package com.jekro.lesjardindecaro.service

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jekro.lesjardindecaro.model.Product
import io.reactivex.Single

class ConfigurationServiceMock : ConfigurationService  {
    override fun getProducts(): Single<List<Product>> {
        val type = object : TypeToken<List<Product>>() {}.type
        return Single.just(Gson().fromJson(getFileContent("produits.json"), type))
    }

    private fun getFileContent(file: String): String {
        return ConfigurationServiceMock::class.java.classLoader?.getResource(file)!!.readText()
    }
}