package cz.uhk.fim.cryptoapp.items

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
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.uhk.fim.cryptoapp.R
import cz.uhk.fim.cryptoapp.data.Crypto

@Composable
fun CryptoItem(crypto: Crypto, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navController.navigate("cryptoDetail/${crypto.id}")
            },
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Image(
            painter = painterResource(R.drawable.coin),
            contentDescription = "${crypto.name} icon",
            modifier = Modifier.size(48.dp)
        )

        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = crypto.name, fontWeight = FontWeight.Bold)
            Text(text = "Symbol: ${crypto.symbol}")
            Text(text = "Price: ${crypto.priceUsd} USD")
            Text(text = "Change 24h: ${crypto.changePercent24Hr}%")
        }

        IconButton(onClick = {
            navController.navigate("cryptoDetail/${crypto.id}")
        }) {
            Icon(Icons.Filled.Info, contentDescription = "Detail")
        }

        IconButton(onClick = {
            //todo přidej do favourites
        }) {
            Icon(Icons.Filled.FavoriteBorder, contentDescription = "Add to favourite")
        }
    }
}