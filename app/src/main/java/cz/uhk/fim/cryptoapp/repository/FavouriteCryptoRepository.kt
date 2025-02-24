package cz.uhk.fim.cryptoapp.repository

import cz.uhk.fim.cryptoapp.data.Crypto
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

//todo simulace komunikace s lokální DB
class FavouriteCryptoRepository {
    //princip zapouzdření _favouriteCrypto by neměl být viditelný mimo třídu, proto je private
    //veškeré změny by se měli provádět pomocí dostupných metod pro add či remove
    //StateFlow objekty slouží pro sledování změň (na základě změny reference) a informuje o tom UI
    //POZOR u listů to velmi často svádí k použití MutableListu a volání add a remove funkcí, to však ponechá stejnou referenci, jen se přidávají / odebírají prvky, což nevyvolá změnu UI
    //proto MutableStateFlow nabízí speciální operátory + pro přidání a vrácení nové reference listu či - pro odebrání a vracení nové reference listu
    private val _favouriteCrypto = MutableStateFlow<List<Crypto>>(emptyList())

    //tento StateFlow je již viditelný pro venek a není Mutable
    val favouriteCrypto : StateFlow<List<Crypto>>
        get() = _favouriteCrypto.asStateFlow() //nastavení getru, funkce asStateFlow() se postará o vytvoření IMutableStateFlow objektu (neměnného)

    //funkce volá suspend funkci delay, proto funkce samotná musí být suspend,
    // nebo musí volat tuto suspend funkci uvnitř jiné coroutine scope (mimo hlavní vlákno)
    //takto se o volání v jiné scope musí postarat jiný objekt, který tuto funkci volá
    suspend fun addFavouriteCrypto(crypto: Crypto){
        delay(1000)
        if(!_favouriteCrypto.value.contains(crypto)){
            _favouriteCrypto.value += crypto //speciální operátor definovaný pro MutableStateFlow objekty - umožňuje přidávat prvky do listu, tak že vytvoří nový list (změní referenci)
        }
    }

    suspend fun removeFavouriteCrypto(crypto: Crypto){
        delay(1000)
        _favouriteCrypto.value -= crypto
    }
}