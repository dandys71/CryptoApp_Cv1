package cz.uhk.fim.cryptoapp.data

import com.google.gson.annotations.SerializedName

//toto bylo formou samostatné práce
data class CryptoHistory (
    @SerializedName("priceUsd") val priceUsd: String,
    @SerializedName("time") val time: Long
)