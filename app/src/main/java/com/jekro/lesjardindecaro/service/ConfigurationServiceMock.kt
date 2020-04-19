package com.jekro.lesjardindecaro.service

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jekro.lesjardindecaro.model.Configuration
import com.jekro.lesjardindecaro.model.Product
import io.reactivex.Single

class ConfigurationServiceMock : ConfigurationService  {

    override fun getConfiguration(): Single<Configuration> {
        val type = object : TypeToken<Configuration>() {}.type
        return Single.just(Gson().fromJson(getFileContent("configuration.json"), type))
    }

    private fun getFileContent(file: String): String {
        return ConfigurationServiceMock::class.java.classLoader?.getResource(file)!!.readText()
    }
}