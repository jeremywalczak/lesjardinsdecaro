package com.jekro.lesjardindecaro.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Cart(
    var productsQuantity: MutableMap<Product, Int> = mutableMapOf(),
    var amountTotal: Float = 0F,
    var isCompleted: Boolean = false,
    val dateOrder: String? = null
) : Parcelable