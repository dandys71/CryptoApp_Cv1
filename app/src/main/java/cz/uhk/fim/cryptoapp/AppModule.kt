package cz.uhk.fim.cryptoapp

import cz.uhk.fim.cryptoapp.repository.FavouriteCryptoRepository
import cz.uhk.fim.cryptoapp.viewmodels.FavouriteCryptoViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

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
    //viewModel { AnotherViewModel(get()) } //takto bych vytvořil další viewmodel, který by mohl požadovat např. AnotherRepository ;)
}

//Pozn.: Není nutné mít vytvořené zvlášť moduly pro singletony (repositáře) a viewmodely,
// můžeme vše definovat v rámci jedné proměnné (module), záleží na našich preferencích