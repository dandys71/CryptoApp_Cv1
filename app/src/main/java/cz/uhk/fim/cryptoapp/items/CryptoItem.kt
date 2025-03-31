package cz.uhk.fim.cryptoapp.items

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import cz.uhk.fim.cryptoapp.R
import cz.uhk.fim.cryptoapp.consts.Routes
import cz.uhk.fim.cryptoapp.data.Crypto
import cz.uhk.fim.cryptoapp.dialogs.DeleteCryptoConfirmationDialog
import cz.uhk.fim.cryptoapp.viewmodels.FavouriteCryptoViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CryptoItem(crypto: Crypto, navController: NavController, isFavourite: Boolean = false, viewModel: FavouriteCryptoViewModel = koinViewModel()) {
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navController.navigate(Routes.cryptoDetail(crypto.id))
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {

        // Obrázek kryptoměny
        /* Image(
             painter = painterResource(R.drawable.coin),
             contentDescription = "${crypto.name} icon",
             modifier = Modifier.size(48.dp)
         )*/
        AsyncImage(
            model = "https://assets.coincap.io/assets/icons/${crypto.symbol.lowercase()}@2x.png",
            contentDescription = "${crypto.name} icon",
            modifier = Modifier.size(48.dp),
        )
        Spacer(modifier = Modifier.width(16.dp))
        // Informace o kryptoměně
        Column(modifier = Modifier.weight(1f)) {
            // val formattedPrice = String.format("%.5f", crypto.priceUsd.toDoubleOrNull() ?: 0.0)
            val numberFormat = NumberFormat.getNumberInstance(Locale.US)
            numberFormat.maximumFractionDigits = 5
            val formattedPrice = numberFormat.format(crypto.priceUsd.toDoubleOrNull() ?: 0.0)

            //val formattedChange = String.format("%.5f", crypto.changePercent24Hr.toDoubleOrNull() ?: 0.0)
            val formattedChange = numberFormat.format(crypto.changePercent24Hr?.toDoubleOrNull() ?: 0.0)
            Text(text = crypto.name, fontWeight = FontWeight.Bold)
            Text(text = "Symbol: ${crypto.symbol}")
            Text(text = "Price: $formattedPrice USD")
            Text(text = "Change 24h: $formattedChange%")
        }
        // Tlačítka
        IconButton(onClick = {  navController.navigate(Routes.cryptoDetail(crypto.id)) }) {
            Icon(Icons.Filled.Info, contentDescription = "Detail")
        }
        IconButton(onClick = {
            if(!isFavourite){
                viewModel.addFavoriteCrypto(crypto)
            }else{
                showDeleteConfirmDialog = true
            }
        }) {
            if (isFavourite) {
                Icon(Icons.Filled.Favorite, contentDescription = "Remove from Favorites")
            } else {
                Icon(Icons.Filled.FavoriteBorder, contentDescription = "Add to Favorites")
            }
        }
    }

    if(showDeleteConfirmDialog){
        DeleteCryptoConfirmationDialog(crypto.name, onConfirmDelete = {
            viewModel.removeFavoriteCrypto(crypto.id)
            showDeleteConfirmDialog = false
        }, onDismiss = {
            showDeleteConfirmDialog = false
            Toast.makeText(context, "User canceled the deletion", Toast.LENGTH_SHORT).show();
        })

    }
}