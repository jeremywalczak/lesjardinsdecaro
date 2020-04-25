package com.jekro.lesjardindecaro.model

import android.os.Parcel
import android.os.Parcelable
import java.util.*


class Environment(
    var label: String = "",
    var url: String = "",
    var apiKey: String = "",
    var apiKeyTranslation: String = "",
    var locale: Locale,
    var useRefreshToken: Boolean = false,
    var loginAction: String = "",
    var logoutAction: String = "",
    var properties: MutableMap<String, Any?> = mutableMapOf()
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readSerializable() as Locale,
        1 == source.readInt(),
        source.readString()!!,
        source.readString()!!
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(label)
        writeString(url)
        writeString(apiKey)
        writeString(apiKeyTranslation)
        writeSerializable(locale)
        writeInt((if (useRefreshToken) 1 else 0))
        writeString(loginAction)
        writeString(logoutAction)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Environment> = object : Parcelable.Creator<Environment> {
            override fun createFromParcel(source: Parcel): Environment = Environment(source)
            override fun newArray(size: Int): Array<Environment?> = arrayOfNulls(size)
        }
    }
}