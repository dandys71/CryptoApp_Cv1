package cz.uhk.fim.cryptoapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.uhk.fim.cryptoapp.api.ApiResult
import cz.uhk.fim.cryptoapp.data.Rate
import cz.uhk.fim.cryptoapp.viewmodels.CryptoRatesViewModel
import cz.uhk.fim.cryptoapp.viewmodels.SettingsViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun SettingsScreen(
    cryptoRatesViewModel: CryptoRatesViewModel = koinViewModel(),
    settingsViewModel: SettingsViewModel = koinViewModel()
) {
    val rates by cryptoRatesViewModel.rates.collectAsState()
    val preferredRateId by settingsViewModel.preferredRateId.collectAsState()


    var selectedRateId by remember { mutableStateOf( "") }
    var selectedRateName by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { cryptoRatesViewModel.getRates() }

    LaunchedEffect(preferredRateId) {
        selectedRateId = preferredRateId ?: ""
    }

    Column(modifier = Modifier.padding(16.dp)) {
        when (rates) {
            is ApiResult.Loading -> {
                Text(text = "Loading rates...")
            }

            is ApiResult.Success -> {
                val rateList = (rates as ApiResult.Success<List<Rate>>).data
                if (selectedRateId.isEmpty() && rateList.isNotEmpty()) {
                    selectedRateId = rateList[0].id
                    selectedRateName = rateList[0].symbol
                }
                if (selectedRateId.isNotEmpty()) {
                    selectedRateName = rateList.find { it.id == selectedRateId }?.symbol ?: ""
                }

                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Defaultní měna")
                    Button(
                        onClick = { showDialog = true },
                        modifier = Modifier.background(Color.Transparent),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(text = selectedRateName)
                    }
                }


                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Select Rate") },
                        modifier = Modifier
                            .padding(16.dp)
                            .sizeIn(maxWidth = 200.dp, maxHeight = 400.dp),
                        text = {
                            LazyColumn {
                                items(rateList) { rate ->
                                    Text(
                                        text = rate.symbol,
                                        modifier = Modifier
                                            .clickable {
                                                selectedRateId = rate.id
                                                selectedRateName = rate.symbol
                                                showDialog = false
                                            }
                                            .padding(8.dp)
                                    )
                                }
                            }
                        },
                        confirmButton = {
                            Button(onClick = { showDialog = false }) {
                                Text("Close")
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    settingsViewModel.savePreferredRateId(selectedRateId)
                }) {
                    Text(text = "Save")
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            is ApiResult.Error -> {
                Text(text = "Error loading rates: ${(rates as ApiResult.Error).message}")
            }
        }
    }
}