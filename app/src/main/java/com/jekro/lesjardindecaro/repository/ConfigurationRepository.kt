package com.jekro.lesjardindecaro.repository

import io.reactivex.Single

class ConfigurationRepository (val service: ConfigurationService) {
    override fun getDateCoupons(): Single<List<DateCoupon>> {
        return service.getDateCoupons()
    }
}