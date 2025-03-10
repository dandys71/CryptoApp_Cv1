package cz.uhk.fim.cryptoapp.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.uhk.fim.cryptoapp.api.ApiResult
import cz.uhk.fim.cryptoapp.data.Crypto
import cz.uhk.fim.cryptoapp.items.CryptoItem
import cz.uhk.fim.cryptoapp.viewmodels.FavouriteCryptoViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavouriteCryptoScreen(navController: NavController, viewModel: FavouriteCryptoViewModel = koinViewModel()) {

    val favouriteListResult by viewModel.favouriteCryptos.collectAsState()

    LaunchedEffect (Unit) {
        viewModel.loadFavouriteCryptos()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(16.dp))

        when (favouriteListResult) {
            is ApiResult.Loading -> {
                CircularProgressIndicator()
            }
            is ApiResult.Success -> {
                val favouriteList = (favouriteListResult as ApiResult.Success<List<Crypto>>).data
                if (favouriteList.isEmpty()) {
                    Text(text = "No favorite crypto yet.")
                } else {
                    LazyColumn {
                        items(favouriteList) { crypto ->
                            CryptoItem(crypto = crypto, navController = navController, isFavourite = true)
                        }
                    }
                }
            }
            is ApiResult.Error -> {
                Text(text = "Error: ${(favouriteListResult as ApiResult.Error).message}")
            }
        }
    }
}