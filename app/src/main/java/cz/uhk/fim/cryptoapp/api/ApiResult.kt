package cz.uhk.fim.cryptoapp.api

/*
Do této třídy budeme "balit" odpovědi z API pro UI.
Při volání fce nastavíme stav odpovědi na Loading, UI tedy bude vědět, že se data stále načítají a má zobrazit nějakou animaci pro načítání. Jelikož tato třída nemá co vracet, ale ApiResult vyžaduje předání nějaké výstupní třídy použijeme pro tyto účely Nothing. Stejně tak nepotřebujeme vytvářet instance, proto místo "class" použijeme "objekt", což je v podstatě třída, která automaticky vytvoří singleton.
Při úspěšném získání dat vrátíme tyto data uvnitř třídy Success, UI bude vědět, že má při úspěšném získání dat, tyto data zobrazit
Při neúspěchu použijeme třídu Error, do které můžeme přidat přizpůsobenou chybovou hlášku. Stejně tak při vyvolání výjimky. Opět nemáme žádná data k vracení, proto opět použijeme Nothing.
 */
sealed class ApiResult<out T> {
    object Loading : ApiResult<Nothing>()
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val message: String) : ApiResult<Nothing>()
}