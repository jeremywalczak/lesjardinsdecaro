package com.jekro.lesjardindecaro.init

import com.jekro.lesjardindecaro.repository.ConfigurationRepository
import com.jekro.lesjardindecaro.service.ConfigurationService
import com.jekro.lesjardindecaro.ui.HomePageContract
import com.jekro.lesjardindecaro.ui.HomePagePresenter
import org.koin.dsl.module.module
import java.util.*

val initModule = module {

    single<ConfigurationRepository> { ConfigurationRepository(get()) }

    single<ConfigurationService> { ConfigurationService() }


    single(name = "locale") { Locale.forLanguageTag("fr-LU") }
    single(name = "user_token") { "Bearer 60ee1454-55fe-4b78-8286-12b0a93a5350" }

    factory<HomePageContract.Presenter> { params ->
        HomePagePresenter(
            params[0],
            get()
        )
    }
}