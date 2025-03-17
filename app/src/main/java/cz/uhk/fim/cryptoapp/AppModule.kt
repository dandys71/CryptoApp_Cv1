package cz.uhk.fim.cryptoapp

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import cz.uhk.fim.cryptoapp.api.CryptoApi
import cz.uhk.fim.cryptoapp.data.FavouriteCryptoEntity
import cz.uhk.fim.cryptoapp.data.MyObjectBox
import cz.uhk.fim.cryptoapp.helpers.NotificationHelper
import cz.uhk.fim.cryptoapp.repository.FavouriteCryptoRepository
import cz.uhk.fim.cryptoapp.repository.SettingsRepository
import cz.uhk.fim.cryptoapp.viewmodels.CryptoRatesViewModel
import cz.uhk.fim.cryptoapp.viewmodels.CryptoViewModel
import cz.uhk.fim.cryptoapp.viewmodels.FavouriteCryptoViewModel
import cz.uhk.fim.cryptoapp.viewmodels.SettingsViewModel
import io.objectbox.BoxStore
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//soubor, který slouží pro definované modulů uvnitř kterých můžeme vytvářet singletony či viewmodely
val repositoryModule = module {
    single { FavouriteCryptoRepository(get()) }
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            produceFile = { androidContext().preferencesDataStoreFile("settings") }
        )
    }
    single { SettingsRepository(get())}
        //single { AnotherRepository() } //takto bych vytvořil například další repozitář
}

val viewModelModule = module {

    //zde se pomocí get() odkáži na potřebnou instanci repozitáře,
    // mohu mít vytvořených více různých singletonů,
    // koin však vždy dosadí požadovanou instanci (zde FavouriteCryptoRepository)
    viewModel { FavouriteCryptoViewModel(get(), get()) }
    viewModel { CryptoViewModel(get()) } //samostatná práce
    viewModel { CryptoRatesViewModel(get())}
    viewModel { SettingsViewModel(get()) }
    //viewModel { AnotherViewModel(get()) } //takto bych vytvořil další viewmodel, který by mohl požadovat např. AnotherRepository ;)
}

val imageModule = module {
    single { provideImageLoader(androidContext()) }
}

val networkModule = module {
    single { provideOkHttpClient() }
    single { provideRetrofit(get()) }
    single { provideCryptoApi(get()) }
}

val objectBoxModule = module {
    single {
        MyObjectBox.builder()
            .androidContext(androidContext())
            .build()
    }
    single { get<BoxStore>().boxFor(FavouriteCryptoEntity::class.java) }
}

val helperModule = module { //todo
    single { NotificationHelper(androidContext()) }
}

//Pozn.: Není nutné mít vytvořené zvlášť moduly pro singletony (repositáře) a viewmodely,
// můžeme vše definovat v rámci jedné proměnné (module), záleží na našich preferencích

fun provideImageLoader(androidContext: Context): ImageLoader {
    return ImageLoader.Builder(androidContext)
        .memoryCache {
            MemoryCache.Builder(androidContext)
                .maxSizePercent(0.25)
                .build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(androidContext.cacheDir.resolve("image_cache"))
                .maxSizePercent(0.02)
                .build()
        }
        .build()
}

fun provideOkHttpClient(): OkHttpClient {
    val loggingInterceptor = HttpLoggingInterceptor().apply { //tvorba interceptoru pro logování
        level = HttpLoggingInterceptor.Level.BODY
    }
    return OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) //přidání interceptoru
        .build()
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://api.coincap.io/v2/")
        .client(okHttpClient) //clienta je nutné definovat, jen v případě, že si vytváříme vlastního, např. my jsme si vytvořili vlastního, abychom přidali interceptor na logování
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun provideCryptoApi(retrofit: Retrofit): CryptoApi {
    return retrofit.create(CryptoApi::class.java) //zde jse Retrofitu předali naše rozhraní pro API, které Retrofit bude implementovat
}