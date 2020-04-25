package com.jekro.lesjardindecaro.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("price")
    val price: Price,
    @SerializedName("status")
    val status: String?,
    @SerializedName("image")
    val image: Image,
    @SerializedName("new")
    val new: Boolean,
    @SerializedName("category_id")
    val categoryId: String?,
    @SerializedName("reduce")
    val reduce: String?,
    @SerializedName("default_quantity")
    val defaultQuantity: Int,
    @SerializedName("unity")
    val unity: String?,
    @SerializedName("origin")
    val origin: String?,
    @SerializedName("cat")
    val cat: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Price::class.java.classLoader),
        parcel.readString(),
        parcel.readParcelable(Image::class.java.classLoader),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeParcelable(price, flags)
        parcel.writeString(status)
        parcel.writeParcelable(image, flags)
        parcel.writeByte(if (new) 1 else 0)
        parcel.writeString(categoryId)
        parcel.writeString(reduce)
        parcel.writeInt(defaultQuantity)
        parcel.writeString(unity)
        parcel.writeString(origin)
        parcel.writeString(cat)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}