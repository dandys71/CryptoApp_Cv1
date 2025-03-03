package cz.uhk.fim.cryptoapp.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.uhk.fim.cryptoapp.api.ApiResult
import cz.uhk.fim.cryptoapp.items.CryptoItem
import cz.uhk.fim.cryptoapp.viewmodels.CryptoViewModel
import cz.uhk.fim.cryptoapp.viewmodels.FavouriteCryptoViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun CryptoListScreen(
    navController: NavController,
    viewModel: CryptoViewModel = koinViewModel(),
    favoriteViewModel: FavouriteCryptoViewModel = koinViewModel()
) {
    val cryptoList by viewModel.cryptoList.collectAsState()
    val favourites by favoriteViewModel.favouriteCryptoList.collectAsState()

    LaunchedEffect(Unit) { //LaunchedEffects potřebuje nějaký klíč, aby věděl, zda se již vykonal či nikoliv, tj. zda má dojít k znovuzavolání (např. key1 = true), lze zjednodušit na Unit (key1 je prvním parametrem)
        //v tomto případě, se ať už key1 = true, či Unit, nikdy nezmění, tudíž se vykoná pouze jednou
        //typicky se zde může použít i nějaký state a kod se znovu vykoná při změně state
        //např. uživatel gestem změní hodnotu state a dojde k refresh, tj. zavolání getCryptoList()
        viewModel.getCryptoList() //to co je uvnitř (tzv. side-effect, tj. nepřímo související s UI, mimo jeho scope) se vykoná pouze jednou a ne při každé rekompozici
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(16.dp))
        when (cryptoList) {
            is ApiResult.Loading -> {
                // Zobraz indikátor načítání
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            is ApiResult.Success -> {
                // Zobraz data
                val cryptoList = (cryptoList as ApiResult.Success).data
                LazyColumn() {
                    items(cryptoList) { crypto ->
                        val isFavourite = favourites.any { it.id == crypto.id }
                        CryptoItem(
                            crypto = crypto,
                            navController = navController,
                            isFavourite = isFavourite
                        )
                    }
                }
            }

            is ApiResult.Error -> {
                // Zobraz chybovou hlášku
                val errorMessage = (cryptoList as ApiResult.Error).message
                Text(text = "Error: $errorMessage")
            }
        }
    }
}
