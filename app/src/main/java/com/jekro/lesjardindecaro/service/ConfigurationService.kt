package com.jekro.lesjardindecaro.service

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jekro.lesjardindecaro.model.Configuration
import io.reactivex.Single

class ConfigurationService  {
    fun getConfiguration(): Single<List<Configuration>> {
        val type = object : TypeToken<List<Configuration>>() {}.type
        return Single.just(Gson().fromJson(getFileContent("produits.json"), type))
    }

    private fun getFileContent(file: String): String {
        return ConfigurationService::class.java.classLoader?.getResource(file)!!.readText()
    }
}