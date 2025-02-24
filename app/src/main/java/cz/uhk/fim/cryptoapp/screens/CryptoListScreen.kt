package cz.uhk.fim.cryptoapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.uhk.fim.cryptoapp.items.CryptoItem
import cz.uhk.fim.cryptoapp.repository.CryptoRepository
import cz.uhk.fim.cryptoapp.viewmodels.FavouriteCryptoViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun CryptoListScreen(navController: NavController, viewModel: FavouriteCryptoViewModel = koinViewModel()) {

    val cryptoList = CryptoRepository.getCryptoList()

    //získáme list s favorites, abychom byli při změně seznamu favorutires ždy upozorněni
    val favouritesList = viewModel.favouriteCryptoList.collectAsState()


    Column(modifier = Modifier.padding(16.dp)){
        Text("Crypto List Screen")
        Spacer(modifier = Modifier.height(16.dp))

        cryptoList.forEach { crypto ->
            //při změně listu se vynutí překreslení, tudíž se projdou všechny itemy znovu a zjistít se stav, zda je daná kryptoměna ve Favourites
            val isFavourite = favouritesList.value.contains(crypto)
            //pokud je ve Favourites, CryptoItem se postará o změnu ikony Favourites (na filled) a při kliknutí místo přidání kryptoměnu z favourites odebere
            CryptoItem(crypto, navController, isFavourite)
        }

    }
}
