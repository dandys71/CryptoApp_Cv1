package cz.uhk.fim.cryptoapp.repository

import cz.uhk.fim.cryptoapp.data.Crypto
import cz.uhk.fim.cryptoapp.data.FavouriteCryptoEntity
import cz.uhk.fim.cryptoapp.data.FavouriteCryptoEntity_
import io.objectbox.Box
import io.objectbox.query.QueryBuilder

class FavouriteCryptoRepository(private val favouriteCryptoBox: Box<FavouriteCryptoEntity>) {
    /**
     * Přidá kryptoměnu do oblíbených.
     *
     * @param cryptoId ID kryptoměny k přidání.
     * @param symbol Symbol kryptoměny k přidání.
     */
    fun addFavouriteCrypto(crypto: Crypto) {
        val existingEntity = favouriteCryptoBox.query()
            .equal(FavouriteCryptoEntity_.cryptoId, crypto.id, QueryBuilder.StringOrder.CASE_INSENSITIVE)
            .build().findFirst()

        if (existingEntity != null) {
            // Entita již existuje, aktualizujeme ji
            existingEntity.name = crypto.name
            existingEntity.symbol = crypto.symbol
            favouriteCryptoBox.put(existingEntity)
        } else {
            // Entita neexistuje, vytvoříme novou
            val favouriteCryptoEntity = FavouriteCryptoEntity(cryptoId = crypto.id, name = crypto.name, symbol = crypto.symbol)
            favouriteCryptoBox.put(favouriteCryptoEntity)
        }

    }

    /**
     * Odebere kryptoměnu z oblíbených.
     *
     * @param cryptoId ID kryptoměny k odebrání.
     */
    fun removeFavouriteCrypto(cryptoId: String) {
        val query = favouriteCryptoBox.query()
            .equal(FavouriteCryptoEntity_.cryptoId, cryptoId, QueryBuilder.StringOrder.CASE_INSENSITIVE)
            .build()
        val result = query.findFirst()
        if (result != null) {
            favouriteCryptoBox.remove(result)
        }
        query.close()
    }

    /**
     * Získá všechny oblíbené kryptoměny.
     *
     * @return List oblíbených kryptoměn.
     */
    fun getAllFavouriteCryptos(): List<FavouriteCryptoEntity> {
        return favouriteCryptoBox.all
    }
}