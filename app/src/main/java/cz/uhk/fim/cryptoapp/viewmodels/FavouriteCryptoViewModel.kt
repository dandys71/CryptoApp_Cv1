package cz.uhk.fim.cryptoapp.viewmodels

import androidx.lifecycle.ViewModel
import cz.uhk.fim.cryptoapp.api.ApiResult
import cz.uhk.fim.cryptoapp.api.CryptoApi
import cz.uhk.fim.cryptoapp.data.FavouriteCryptoEntity
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import cz.uhk.fim.cryptoapp.data.Crypto
import cz.uhk.fim.cryptoapp.repository.FavouriteCryptoRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.text.find
import kotlin.text.firstOrNull
import kotlin.text.toDoubleOrNull

class FavouriteCryptoViewModel(
    private val favouriteCryptoRepository: FavouriteCryptoRepository,
    private val cryptoApi: CryptoApi
) : ViewModel() {

    val favouriteCryptosUpdated = flow { //flow se postará o update UI
        while (true) {
            emit(Unit) //emitnutí nové hodnoty do Flow
            delay(TimeUnit.SECONDS.toMillis(15)) //počkat 15 sekund
        }
    }.map { //zde se postaráme o získání hodnot (logika z loadFavouriteCryptos())
        try{
            val favouriteEntities: List<FavouriteCryptoEntity> = favouriteCryptoRepository.getAllFavouriteCryptos()
            if (favouriteEntities.isEmpty()) {
                ApiResult.Success<List<Crypto>>(emptyList())
            } else {
                val cryptoIds = favouriteEntities.map { it.cryptoId }.joinToString(",")

                val result = cryptoApi.getCryptoListByIds(cryptoIds)

                if(result.isSuccessful){
                    ApiResult.Success(result.body()?.data ?: emptyList())
                }else{
                    ApiResult.Error(result.message())
                }
            }
        }catch (e: Exception){
            ApiResult.Error("Exception fetching favourite cryptos: ${e.message}")
        }
    }.stateIn(
        scope = viewModelScope, //nastavení couroutine scope ve které se tento kod spouští
        started = SharingStarted.WhileSubscribed(5_000), //nastavení parametrů flowu, začne okamžitě po přihlášení a prvního subscriberu a přestene 5 vteřin po odhlášení posledního, tzn. nebude se vykonávat pokud nikdo nekolektuje stav
        initialValue = ApiResult.Loading
    )

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