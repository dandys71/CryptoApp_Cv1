package cz.uhk.fim.cryptoapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.uhk.fim.cryptoapp.data.Crypto
import cz.uhk.fim.cryptoapp.repository.FavouriteCryptoRepository
import kotlinx.coroutines.launch

//ViewModel pro oblíbené kryptoměny, viewmodely nabízí funkce a případně další atributy pro UI
//UI si může pomocí DI získat instanci viewmodelu (koinu) a volat patřičné funkce
class FavouriteCryptoViewModel (private val cryptoRepository: FavouriteCryptoRepository) : ViewModel() {
    val favouriteCryptoList = cryptoRepository.favouriteCrypto //vystavení StateFlow objektu pro UI, UI pomocí něho bude dostatávat notifikaci při změně reference

    //funkce pro přidání kryptoměna, pouze volá metodu z repositáře
    //metoda z repozitáře je suspend a proto volá se pomocí coroutine scope
    fun addFavouriteCrypto(crypto: Crypto){
        viewModelScope.launch { //jelikož dědíme z ViewModel() třídy může použít již předvytvořený viewModelScope, který slouží právě pro volání asynchronních funkcí (mimo hlavní UI vlákno)
            cryptoRepository.addFavouriteCrypto(crypto)
        }
    }

    fun removeFavouriteCrypto(crypto: Crypto){
        viewModelScope.launch {
            cryptoRepository.removeFavouriteCrypto(crypto)
        }
    }
}