package com.jekro.lesjardindecaro.model

import com.google.gson.annotations.SerializedName

data class Configuration(
    @SerializedName("carroussel_image_home_page")
    val carrousselImageHomePage : List<String>? = null,
    @SerializedName("categories")
    val categories : List<Category>,
    @SerializedName("products")
    val products : List<Product>
)