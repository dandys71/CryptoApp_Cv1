package cz.uhk.fim.cryptoapp

import cz.uhk.fim.cryptoapp.api.CryptoApi
import cz.uhk.fim.cryptoapp.repository.FavouriteCryptoRepository
import cz.uhk.fim.cryptoapp.viewmodels.CryptoViewModel
import cz.uhk.fim.cryptoapp.viewmodels.FavouriteCryptoViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//soubor, který slouží pro definované modulů uvnitř kterých můžeme vytvářet singletony či viewmodely
val repositoryModule = module {
    single { FavouriteCryptoRepository() }
    //single { AnotherRepository() } //takto bych vytvořil například další repozitář
}

val viewModelModule = module {

    //zde se pomocí get() odkáži na potřebnou instanci repozitáře,
    // mohu mít vytvořených více různých singletonů,
    // koin však vždy dosadí požadovanou instanci (zde FavouriteCryptoRepository)
    viewModel { FavouriteCryptoViewModel(get()) }
    viewModel { CryptoViewModel(get()) } //samostatná práce
    //viewModel { AnotherViewModel(get()) } //takto bych vytvořil další viewmodel, který by mohl požadovat např. AnotherRepository ;)
}

val networkModule = module {
    single { provideOkHttpClient() }
    single { provideRetrofit(get()) }
    single { provideCryptoApi(get()) }
}

//Pozn.: Není nutné mít vytvořené zvlášť moduly pro singletony (repositáře) a viewmodely,
// můžeme vše definovat v rámci jedné proměnné (module), záleží na našich preferencích

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