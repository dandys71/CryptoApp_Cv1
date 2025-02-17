import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.uhk.fim.cryptoapp.R
import cz.uhk.fim.cryptoapp.repository.CryptoRepository


@Composable
fun CryptoDetailScreen(navController: NavController, cryptoId: String) {
    val crypto = CryptoRepository.getCryptoById(cryptoId)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (crypto != null) {
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

            Text(
                text = "Change 24h: ${crypto.changePercent24Hr}%",
                style = MaterialTheme.typography.bodyLarge,
                color = if (crypto.changePercent24Hr.toDouble() >= 0) Color.Green else Color.Red
            )
            Spacer(modifier = Modifier.height(16.dp))

            IconButton(onClick = {
                //todo
            }) {
                Icon(Icons.Filled.FavoriteBorder, contentDescription = "Add to favourite")
            }
        } else {
            Text(text = "Crypto not found")
        }

        // Tlačítko pro návrat zpět
        Button(onClick = { navController.popBackStack() }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go Back")
            Text("Go Back")
        }

    }
}