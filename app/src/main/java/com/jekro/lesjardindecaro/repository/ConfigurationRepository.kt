package com.jekro.lesjardindecaro.repository

import com.jekro.lesjardindecaro.model.Configuration
import com.jekro.lesjardindecaro.model.Product
import com.jekro.lesjardindecaro.service.ConfigurationService
import io.reactivex.Single

class ConfigurationRepository (val service: ConfigurationService) {

    fun getConfiguration(): Single<Configuration> {
        return service.getConfiguration()
    }
}