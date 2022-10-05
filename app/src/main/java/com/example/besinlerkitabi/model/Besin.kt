package com.example.besinlerkitabi.model

import com.google.gson.annotations.SerializedName

data class Besin(

    @SerializedName("isim")
    val isim: String,
    @SerializedName("kalori")
    val kalori: String,
    @SerializedName("karbonhidrat")
    val karbonhidrat: String,
    @SerializedName("protein")
    val protein: String,
    @SerializedName("yag")
    val yag: String,
    @SerializedName("gorsel")
    val gorsel: String
)
