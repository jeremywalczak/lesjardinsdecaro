package com.jekro.lesjardindecaro.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HistoricOrder(
    var orders: MutableList<Cart>? = null
) : Parcelable