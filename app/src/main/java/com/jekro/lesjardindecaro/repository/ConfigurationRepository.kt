package com.jekro.lesjardindecaro.repository

import com.jekro.lesjardindecaro.model.Products
import com.jekro.lesjardindecaro.service.ConfigurationService
import io.reactivex.Single

class ConfigurationRepository (val service: ConfigurationService) {
    fun getConfiguration(): Single<List<Products>> {
        return service.getConfiguration()
    }
}