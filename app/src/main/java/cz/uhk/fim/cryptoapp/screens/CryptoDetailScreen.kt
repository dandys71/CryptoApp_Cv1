import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import cz.uhk.fim.cryptoapp.R
import cz.uhk.fim.cryptoapp.api.ApiResult
import cz.uhk.fim.cryptoapp.dialogs.DeleteCryptoConfirmationDialog
import cz.uhk.fim.cryptoapp.viewmodels.CryptoViewModel
import cz.uhk.fim.cryptoapp.viewmodels.FavouriteCryptoViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject


@Composable
fun CryptoDetailScreen(
    cryptoId: String,
    viewModel: CryptoViewModel = koinViewModel(),
    favouriteCryptoViewModel: FavouriteCryptoViewModel = koinViewModel()
) {
    val cryptoDetailResult by viewModel.cryptoDetail.collectAsState()
    val favoriteCryptoList by favouriteCryptoViewModel.favouriteCryptos.collectAsState()
    val scrollState = rememberScrollState()

    val context = LocalContext.current

    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getCryptoDetail(cryptoId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
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
                // Zobraz data
                val crypto = (cryptoDetailResult as ApiResult.Success).data
                if (crypto != null) {
                    val isFavourite =
                        if (favoriteCryptoList is ApiResult.Success) {
                            (favoriteCryptoList as ApiResult.Success).data.any { it.id == crypto.id }
                        } else {
                            false
                        }
                   //úkol č. 5
                    AsyncImage(
                        model = "https://assets.coincap.io/assets/icons/${crypto.symbol.lowercase()}@2x.png",
                        contentDescription = "${crypto.name} icon",
                        modifier = Modifier.size(100.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Nadpis a symbol a button icon
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .wrapContentWidth(Alignment.CenterHorizontally),
                            text = "${crypto.name} (${crypto.symbol})",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
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
                    Spacer(modifier = Modifier.height(16.dp))

                    //úkol č. 7
                    // Cena
                    DetailRow(title = "Price") //nastavím parametr title
                    { //uvnitř content neboli Composable funkce mohu přidávat další composable objekty
                        Text(
                            text = "${crypto.priceUsd} USD",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    // Změna 24h
                    crypto.changePercent24Hr?.let {
                        DetailRow(title = "Change 24h") {
                            Text(
                                text = "${crypto.changePercent24Hr}%",
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (crypto.changePercent24Hr.startsWith("-")) Color.Red else Color.Green
                            )
                        }
                    }

                    //Max supply
                    crypto.maxSupply?.let {
                        DetailRow(title = "Max supply") {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    }


                    //Market cap
                    DetailRow(title = "Market cap") {
                        Text(
                            text = crypto.marketCapUsd,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    //Volume last 24h
                    DetailRow(title = "Volume last 24h") {
                        Text(
                            text = crypto.volumeUsd24Hr,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    //Supply
                    DetailRow(title = "Supply") {
                        Text(
                            text = crypto.supply,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    //Vwap 24h
                    crypto.vwap24Hr?.let {
                        DetailRow(title = "Vwap 24h") {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    if(showDeleteConfirmDialog){
                        DeleteCryptoConfirmationDialog(crypto.name, onConfirmDelete = {
                            favouriteCryptoViewModel.removeFavoriteCrypto(crypto.id)
                            showDeleteConfirmDialog = false
                        }, onDismiss = {
                            showDeleteConfirmDialog = false
                            Toast.makeText(context, "User canceled the deletion", Toast.LENGTH_SHORT).show();
                        })

                    }

                } else {
                    Text(text = "Crypto not found", style = MaterialTheme.typography.bodyLarge)
                }
            }

            is ApiResult.Error -> {
                // Zobraz chybovou hlášku
                val errorMessage = (cryptoDetailResult as ApiResult.Error).message
                Text(text = "Error: $errorMessage", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }

}

@Composable
fun DetailRow(title: String, content: @Composable () -> Unit) {
    //takto vytvořený parametry content jakožto composable funkce, která nepříjímá žádné parametry a nic nevrací,
    // nám umožňuje uvnitř této funkce používat další composable objekty
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
        Text(text = title, fontWeight = FontWeight.Bold)
        content()
        Spacer(modifier = Modifier.height(8.dp))
    }
}