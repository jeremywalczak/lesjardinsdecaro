package com.jekro.lesjardindecaro.init

import com.jekro.lesjardindecaro.repository.ConfigurationRepository
import com.jekro.lesjardindecaro.service.ConfigurationService
import com.jekro.lesjardindecaro.service.ConfigurationServiceMock
import com.jekro.lesjardindecaro.ui.home.HomePageContract
import com.jekro.lesjardindecaro.ui.home.HomePagePresenter
import com.jekro.lesjardindecaro.ui.list.ListProductContract
import com.jekro.lesjardindecaro.ui.list.ListProductPresenter
import org.koin.dsl.module.module
import java.util.*

val initModule = module {

    single<ConfigurationRepository> { ConfigurationRepository(get()) }

    single<ConfigurationService> { ConfigurationServiceMock() }

    /*single {
        get<Retrofit>(named("retrofit")).create(ConfigurationService::class.java)
    }*/


    single(name = "locale") { Locale.forLanguageTag("fr-LU") }
    single(name = "user_token") { "Bearer 60ee1454-55fe-4b78-8286-12b0a93a5350" }

    factory<HomePageContract.Presenter> { params ->
        HomePagePresenter(
            params[0],
            get()
        )
    }

    factory<ListProductContract.Presenter> { params ->
        ListProductPresenter(
            params[0]
        )
    }
}