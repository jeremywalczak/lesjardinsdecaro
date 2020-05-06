package com.jekro.lesjardindecaro.init

import com.jekro.lesjardindecaro.model.Environment
import com.jekro.lesjardindecaro.repository.ConfigurationRepository
import com.jekro.lesjardindecaro.service.ConfigurationService
import com.jekro.lesjardindecaro.service.ConfigurationServiceMock
import com.jekro.lesjardindecaro.ui.DetailContract
import com.jekro.lesjardindecaro.ui.DetailPresenter
import com.jekro.lesjardindecaro.ui.cart.CartContract
import com.jekro.lesjardindecaro.ui.cart.CartPresenter
import com.jekro.lesjardindecaro.ui.cart.ValidateCartContract
import com.jekro.lesjardindecaro.ui.cart.ValidateCartPresenter
import com.jekro.lesjardindecaro.ui.home.HomePageContract
import com.jekro.lesjardindecaro.ui.home.HomePagePresenter
import com.jekro.lesjardindecaro.ui.list.ListProductContract
import com.jekro.lesjardindecaro.ui.list.ListProductPresenter
import io.reactivex.schedulers.Schedulers.single
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.*

val initModule = module {

    single<ConfigurationRepository> { ConfigurationRepository(get()) }

    //single<ConfigurationService> { ConfigurationServiceMock() }

    single {
        get<Retrofit>(named("retrofit")).create(ConfigurationService::class.java)
    }


    single(named ("locale")) { Locale.forLanguageTag("fr-fr") }

    factory(named("backoffice")) { "http://www.lejardindecaro.fr/" }

    single(named("environment")) {
        var environment = Environment(
            "Prod",
            "http://www.lejardindecaro.fr/",
            "",
            "",
            Locale.forLanguageTag("fr-fr")
        )
        environment
    }

    factory<HomePageContract.Presenter> { params ->
        HomePagePresenter(
            params[0],
            get()
        )
    }

    factory<CartContract.Presenter> { params ->
        CartPresenter(
            params[0],
            get()
        )
    }

    factory<ValidateCartContract.Presenter> { params ->
        ValidateCartPresenter(
            params[0],
            get()
        )
    }

    factory<DetailContract.Presenter> { params ->
        DetailPresenter(
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