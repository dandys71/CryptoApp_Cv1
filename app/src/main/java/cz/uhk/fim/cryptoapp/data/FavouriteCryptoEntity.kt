package cz.uhk.fim.cryptoapp.data

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class FavouriteCryptoEntity(
    @Id var id: Long = 0, // Primární klíč, automaticky generovaný
    var cryptoId: String, // ID kryptoměny z API
    var name: String, // Název kryptoměny
    var symbol: String, // Symbol kryptoměny (např. BTC)
    var amount: Double? = null // Množství kryptoměny, může být null
)