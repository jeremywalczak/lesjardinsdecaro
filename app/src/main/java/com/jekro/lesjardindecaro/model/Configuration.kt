package com.jekro.lesjardindecaro.model

import com.google.gson.annotations.SerializedName

data class Configuration(
    @SerializedName("carroussel_image_home_page")
    val carrousselImageHomePage : List<String>,
    @SerializedName("products")
    val products : List<Product>
)