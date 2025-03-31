package cz.uhk.fim.cryptoapp

import CryptoDetailScreen
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cz.uhk.fim.cryptoapp.consts.BottomNavItem
import cz.uhk.fim.cryptoapp.consts.Routes
import cz.uhk.fim.cryptoapp.screens.CryptoListScreen
import cz.uhk.fim.cryptoapp.screens.FavouriteCryptoScreen
import cz.uhk.fim.cryptoapp.screens.SettingsScreen
import cz.uhk.fim.cryptoapp.ui.theme.CryptoAppTheme
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import cz.uhk.fim.cryptoapp.helpers.NotificationSchedulerHelper
import cz.uhk.fim.cryptoapp.helpers.PermissionHelper
import cz.uhk.fim.cryptoapp.workers.NotificationWorker
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin { //inicializace DI
            androidContext(this@MainActivity)
            modules(
                repositoryModule,
                viewModelModule,
                networkModule,
                objectBoxModule,
                imageModule,
                helperModule
            ) //nastaveni modulu (ze souboru AppModules)
        }
        setContent {
            CryptoAppTheme {
                val navController = rememberNavController()
                MainScreen(navController)
            }
        }

        val permissionHelper = PermissionHelper(this)
        permissionHelper.requestNotificationPermission()

        val notificationSchedulerHelper: NotificationSchedulerHelper by inject()
        notificationSchedulerHelper.scheduleNotificationWorker()
    }


    //zaregistrujeme callback pro navrácení výsledku z požadavku o povolení notifikací
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
        } else { //pozor pokud uživatel poprvé zamítne a my budeme po každém spuštění app volat žádost o povolení,
            // bude stále vypisovat (znovu systém nezobrazí dialog a automaticky oprávnění zamítne)
            Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    //proměnné jejichž hodnotu potřebujeme zachovat i po rekompozici (překreslení) musíme ukládat pomocí remember a mutableStateOf, případně funkcí podobných
    var selectedItem by remember { mutableStateOf(0) }

    //náš list itemů použitých v bottom navigation bar
    val items = listOf(
        BottomNavItem.CryptoList,
        BottomNavItem.FavouriteCrypto
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crypto App") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    //úkol č. 2
                    if (currentRoute == Routes.CryptoDetail || currentRoute == Routes.Settings) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                tint = Color.White,
                                contentDescription = "Go Back"
                            )
                        }
                    }
                },
                actions = {
                    //úkol č. 2
                    if (currentRoute != Routes.Settings) {
                        IconButton(onClick = { navController.navigate(Routes.Settings) }) {
                            Icon(
                                Icons.Filled.Settings,
                                tint = Color.White,
                                contentDescription = "Settings"
                            )
                        }
                    }
                }
            )
        },

        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.primary,
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                item.icon,
                                contentDescription = item.title
                            )
                        },
                        label = { Text(item.title) },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            navController.navigate(item.screenRoute) {
                                navController.graph.startDestinationRoute?.let { screenRoute ->
                                    popUpTo(screenRoute) {  //odebrání ostatních obrazovek z back stacku
                                        saveState =
                                            true//uložení stavu obrazovek odstraněných z back stacku
                                    }
                                }
                                launchSingleTop =
                                    true //zabraňuje vytváření nových instancí stejné obrazovky
                                restoreState =
                                    true //obnovení stavu, pokud byl uložen pomocí saveState
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            unselectedIconColor = Color.LightGray,
                            selectedTextColor = Color.White,
                            unselectedTextColor = Color.LightGray,
                            indicatorColor = MaterialTheme.colorScheme.secondary
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Navigation(navController = navController, innerPadding = innerPadding)
    }
}

@Composable
fun Navigation(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = Routes.CryptoList,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(Routes.CryptoList) { CryptoListScreen(navController) }
        composable(Routes.CryptoDetail) { navBackStackEntry ->
            val cryptoId = navBackStackEntry.arguments?.getString("cryptoId")
            if (cryptoId != null) {
                CryptoDetailScreen(cryptoId)
            }
        }
        composable(Routes.FavouriteCrypto) { FavouriteCryptoScreen(navController) }
        composable(Routes.Settings) { SettingsScreen() }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    CryptoAppTheme {
        MainScreen(rememberNavController())
    }

}