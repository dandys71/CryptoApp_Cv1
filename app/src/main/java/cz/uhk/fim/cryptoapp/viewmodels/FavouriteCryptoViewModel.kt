package cz.uhk.fim.cryptoapp.viewmodels

import androidx.lifecycle.ViewModel
import cz.uhk.fim.cryptoapp.api.ApiResult
import cz.uhk.fim.cryptoapp.api.CryptoApi
import cz.uhk.fim.cryptoapp.data.FavouriteCryptoEntity
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import cz.uhk.fim.cryptoapp.data.Crypto
import cz.uhk.fim.cryptoapp.repository.FavouriteCryptoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavouriteCryptoViewModel(
    private val favouriteCryptoRepository: FavouriteCryptoRepository,
    private val cryptoApi: CryptoApi
) : ViewModel() {

    private val _favouriteCryptos = MutableStateFlow<ApiResult<List<Crypto>>>(ApiResult.Loading)
    val favouriteCryptos: StateFlow<ApiResult<List<Crypto>>> = _favouriteCryptos.asStateFlow()

    init {
        loadFavouriteCryptos()
    }

    fun addFavoriteCrypto(crypto: Crypto) {
        viewModelScope.launch {
            favouriteCryptoRepository.addFavouriteCrypto(crypto)
            loadFavouriteCryptos()
        }
    }

    fun removeFavoriteCrypto(cryptoId: String) {
        viewModelScope.launch {
            favouriteCryptoRepository.removeFavouriteCrypto(cryptoId)
            loadFavouriteCryptos()
        }
    }

    fun loadFavouriteCryptos() {
        viewModelScope.launch {
            try{
            val favouriteEntities: List<FavouriteCryptoEntity> = favouriteCryptoRepository.getAllFavouriteCryptos()
            if (favouriteEntities.isEmpty()) {
                _favouriteCryptos.value = ApiResult.Success(emptyList())
            } else {
                val cryptoIds = favouriteEntities.map { it.cryptoId }.joinToString(",")

                val result = cryptoApi.getCryptoListByIds(cryptoIds)

                if(result.isSuccessful){
                    _favouriteCryptos.value = ApiResult.Success(result.body()?.data ?: emptyList())
                }else{
                    _favouriteCryptos.value = ApiResult.Error(result.message())
                }
            }
            }catch (e: Exception){
                _favouriteCryptos.value = ApiResult.Error("Exception fetching favourite cryptos: ${e.message}")
            }
        }
    }
}