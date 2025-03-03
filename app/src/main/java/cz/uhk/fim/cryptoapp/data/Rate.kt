package cz.uhk.fim.cryptoapp.data

import com.google.gson.annotations.SerializedName

//toto bylo formou samostatné práce
data class Rate(
    @SerializedName("id") val id: String,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("currencySymbol") val currencySymbol: String?,
    @SerializedName("type") val type: String,
    @SerializedName("rateUsd") val rateUsd: String
)
