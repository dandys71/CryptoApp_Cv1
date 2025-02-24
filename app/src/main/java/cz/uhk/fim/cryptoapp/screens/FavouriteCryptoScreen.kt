package cz.uhk.fim.cryptoapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import cz.uhk.fim.cryptoapp.R
import cz.uhk.fim.cryptoapp.items.CryptoItem
import cz.uhk.fim.cryptoapp.viewmodels.FavouriteCryptoViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavouriteCryptoScreen(
    navController: NavController,
    viewModel: FavouriteCryptoViewModel = koinViewModel() //zde si získáme instanci viewmodelu pomocí DI
) {
    val favouriteList by viewModel.favouriteCryptoList.collectAsState() //zde si získáme list oblíbených kryptoměn pomocí StateFlow objektu
    //v případě změny bude Composable funkce upozrněna a vše, kde je použitý objekt favouriteList bude překresleno

    Column {
        Text(text = "Favourite Crypto Screen")
        if (favouriteList.isEmpty()) {
            Text(text = stringResource(R.string.no_favourite_crypto)) //v rámci aplikace je lepši se odkazovat na stringy z res/values/strings.xml, nabízí nám to např. snažší překlad do jiných jazyků
        } else {
            //LazyColumn slouží pro optimální načítání itemů listu
            //nevykresluje zbytečně celý list, ale pouze ty itemy, co jsou zrovna vidět, nebo je možnost, že budou brzo zobrazené (např. scrolling)
            LazyColumn {
                //pro procházení můžeme buď použít funkci items() nebo itemsIndexed, která nám krom itemů umožňuje získat i aktuální index, což může být někdy užitečné
                itemsIndexed(favouriteList) { index, crypto ->
                    CryptoItem(crypto, navController, isFavourite = true)
                }
            }
        }
    }
}