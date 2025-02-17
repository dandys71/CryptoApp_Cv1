package cz.uhk.fim.cryptoapp.data

//id, symbol, name, priceUsd, changePercent24Hr
data class Crypto(
    val id: String,
    val symbol: String,
    val name: String,
    val priceUsd: String,
    val changePercent24Hr: String
)
