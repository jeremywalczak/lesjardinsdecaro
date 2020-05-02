package com.jekro.lesjardindecaro.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User (
    var name: String? = null,
    var firstname: String? = null,
    var mail: String? = null,
    var phone: String? = null
) : Parcelable