import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController


@Composable
fun CryptoDetailScreen(navController: NavController, cryptoId: String) {
    //todo získat kryptoměnu podle ID a zobrazit její podrobnosti, pokud není žádná nalezena zobrazit chybovou hlášku


    // Tlačítko pro návrat zpět
    Button(onClick = { navController.popBackStack() }) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go Back")
        Text("Go Back")
    }
}