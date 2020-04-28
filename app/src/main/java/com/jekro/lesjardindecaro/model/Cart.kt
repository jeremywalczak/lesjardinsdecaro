package com.jekro.lesjardindecaro.model

import android.os.Parcel
import android.os.Parcelable

data class Cart(
    val products: List<Product>?,
    val amountTotal: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(Product),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(products)
        parcel.writeString(amountTotal)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Cart> {
        override fun createFromParcel(parcel: Parcel): Cart {
            return Cart(parcel)
        }

        override fun newArray(size: Int): Array<Cart?> {
            return arrayOfNulls(size)
        }
    }
}