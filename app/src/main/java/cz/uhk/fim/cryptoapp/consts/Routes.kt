package cz.uhk.fim.cryptoapp.consts

//objekt pro konstanty všech routes
//objekt umožňuje statický přístup (v podstatě singleton)
object Routes {
    const val CryptoList = "cryptoList"
    const val CryptoDetail = "cryptoDetail/{cryptoId}"
    const val FavouriteCrypto = "favouriteCrypto"
    const val Settings = "settings"

    //Funkce pro vytvoření routy s id
    fun cryptoDetail(cryptoId: String): String {
        return "cryptoDetail/$cryptoId"
    }
}