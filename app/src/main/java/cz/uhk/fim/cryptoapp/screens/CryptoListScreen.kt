package cz.uhk.fim.cryptoapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.uhk.fim.cryptoapp.items.CryptoItem
import cz.uhk.fim.cryptoapp.repository.CryptoRepository


@Composable
fun CryptoListScreen(navController: NavController) {
    val cryptoList = CryptoRepository.getCryptoList()
    Column(modifier = Modifier.padding(16.dp)){
        Text("Crypto List Screen")
        Spacer(modifier = Modifier.height(16.dp))

        cryptoList.forEach { crypto ->
            CryptoItem(crypto, navController)
        }

    }
}
