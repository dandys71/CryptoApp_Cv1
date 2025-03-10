import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.uhk.fim.cryptoapp.R
import cz.uhk.fim.cryptoapp.api.ApiResult
import cz.uhk.fim.cryptoapp.viewmodels.CryptoViewModel
import cz.uhk.fim.cryptoapp.viewmodels.FavouriteCryptoViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun CryptoDetailScreen(
    navController: NavController,
    cryptoId: String,
    viewModel: CryptoViewModel = koinViewModel(),
    favouriteCryptoViewModel: FavouriteCryptoViewModel = koinViewModel()
) {

    val cryptoDetailResult by viewModel.cryptoDetail.collectAsState()
    val favourites by favouriteCryptoViewModel.favouriteCryptos.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCryptoDetail(cryptoId)
        favouriteCryptoViewModel.loadFavouriteCryptos()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        when (cryptoDetailResult) {
            is ApiResult.Loading -> {
                // Zobraz indikátor načítání
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            is ApiResult.Success -> {
                val crypto = (cryptoDetailResult as ApiResult.Success).data
                val isFavourite = if (favourites is ApiResult.Success) {
                    (favourites as ApiResult.Success).data.any { it.id == crypto.id }
                } else {
                    false
                }

                Image(
                    painter = painterResource(id = R.drawable.coin),
                    contentDescription = "Coin image",
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = crypto.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Symbol: ${crypto.symbol}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Price: ${crypto.priceUsd}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(4.dp))

                crypto.changePercent24Hr?.let { changePercent ->
                    Text(
                        text = "Change 24h: ${crypto.changePercent24Hr}%",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (changePercent.toDouble() >= 0) Color.Green else Color.Red
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                IconButton(onClick = {
                    if (isFavourite) {
                        favouriteCryptoViewModel.removeFavoriteCrypto(crypto.id)
                    } else {
                        favouriteCryptoViewModel.addFavoriteCrypto(crypto)
                    }
                }) {
                    if (isFavourite) {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = "Remove from Favorites"
                        )
                    } else {
                        Icon(
                            Icons.Filled.FavoriteBorder,
                            contentDescription = "Add to Favorites"
                        )
                    }
                }
            }

            is ApiResult.Error -> {
                val errorMessage = (cryptoDetailResult as ApiResult.Error).message
                Text(text = "Error: $errorMessage", style = MaterialTheme.typography.bodyLarge)
            }
        }

        // Tlačítko pro návrat zpět
        Button(onClick = { navController.popBackStack() }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go Back")
            Text("Go Back")
        }

    }
}