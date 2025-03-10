<h2>**1. Cvičení**</h2>
<p>Na prvním cvičení jsme si:</p>
<li>nastavili Android Studio a přidali stažené SDK</li>
<li>vytvořili virtuální zařízení a ukázali si, jak aplikaci nahrávat do svého mobilního telefonu s OS Android</li>
<li>ukázali si základní soubory v rámci nového projektu - AndroidManifest.xml, Gradle soubory, MainActivity</li>

<h2>**2. Cvičení**</h2>
<p>Na druhém cvičení jsme si:</p>
<li>vytvořili datovou třídu pro kryptoměny a přidali funkce do repositáře kryptoměn pro získání listu a kryptoměny podle id.</li>
<li>ukázali základní komponenty pro pozicování Row() a Column() a vyzkoušeji jsme si i komponenty jako Text(), Image(), Icon() či Buttton().</li>
<li>vysvětlili, jak funguje navigace, respektive navigační controller a přidali jsme si vlastní routu pro detail kryptoměny.</li>
<li>podle návrhu samostatně vyzkoušeli vytvořit vlastní komponentu pro detail kryptoměny.</li>

<h2>**3. Cvičení**</h2>
<p>Na třetím cvičení jsme si:</p>
<li>přidali routu pro favoriteCrypto obrazovku</li>
<li>přidali objekt Routes pro definování rout pomocí konstant a aktální kód přepsali na nově vytvořené konstanty</li>
<li>přidali sealed class pro BottomNavItemy, kde jsme si definovali konečnou množinu tříd dědících z této sealed classy</li>
<li>do Scaffoldu přidali kód pro bottomBar, který jsme zkopírovali z Olivy a stěžejní části jsme si vysvětlili</li>
<li>vytvořili FavouriteCryptoRepository, který momentálně slouží pro simulaci komunikace s lokální DB pomocí asynchronních funkcí</li>
<li>vytvořili FavouriteCryptoViewModel, který komunikuje a volá ve viewModelScope asynchornní funkce z repositáře</li>
<li>z Olivy přidali závislost pro koin, která slouží pro použití Dependency Injection, což výrazně ulehčuje práci např. s viewmodely</li>
<li>přidali soubor AppModoules pro definování jednotlivých modulů, které budou složit pro vytváření požadovaných instancí</li>
<li>dovnitř CryptoItem přidali volání funkcí viewmodelu pro přídání/odebrání do/z oblíbených</li>
<li>ukázali použití LazyColumn uvnitř FavouriteCryptoScreen, který slouží pro optimalizované načítání dat, tak aby se zbytečně nevykreslovali itemy, které nemohou být vidět/li>

<h2>**4. Cvičení**</h2>
<p>Na čtvrtém cvičení jsme (si):</p>
<li>přidali permission pro Internet, které je udělováno automaticky při instalaci, a zároveň jsme si přidali závislosti na retrofit a okhttp3</li>
<li>vytvořili package api, do kterého jsme umísti interface CryptoApi pro tvorbu funkcí na obsluhu jednotlívch endpointů</li>
<li>do package api přidali CryptoResponse jakožto pomocnou třídu pro zpracování odpovědí (za využití genericity)</li>
<li>do package api přidali sealed class ApiResult (opět s použitím genericity), do kterého budeme balit výsledky z viewmodelu do UI - Succes, Error, Loading</li>
<li>v AppModule vytvořili nový networkModule uvnitř, kterého jsme vytvořili singletony pro OkHttpClienta, Retrofit a CryptoApi (za použití předvytvořených funkcí)</li>
<li>uvnitř CryptoApi vytvořili 3 suspend funkce pro komunikaci s api, konkrétně s assets, assets s filtrem na limit a assets/{{id}}</li>
<li>upravili datovou třídu Crypto, aby atributy odpovídali klíčům v JSON</li>
<li>vytvořili nový CryptoViewModel, který bude nabízet funkce UI a bude volat patřičné metody z CryptoApi</li>
<li>použili tento viewmodel v CryptoListScreen (i s použitím LaunchedEffects) a o ohledem na všechny možné stavy ApiResult</li>
<li>v rámci samostatné práce přidali podporu i pro endpointy /assets/{{id}}, /assets/{{id}}/history a rates, kdy pro Rates jsme chtěli vytvořit i nový viewmodel (CryptoRatesViewModel)</li>
<li>v CryptoDetailScreen pomocí CryptoViewModel získali detail o kryptoměně a opět jsme upravili UI, tak abychom obstarali všechny možné stavy ApiResult</li>
<li>díky napojení na CryptoViewModel mohli smazat CryptoRepository, který původně sloužil pro simulaci získávání dat (aby bylo možné v UI vůbec něco vykreslit)</li>

<h2>**5. cvičení**</h2>
<p>Na pátem cvičení jsme (si):</p>
<li>řekli o tom, že naše API je označené jako depracated a ukončí ke konci března činnost (pravděpodobně bude nová verze vyžadovat API key, tato verze každé Xté volání místo korektních výsledků vrací zprávu o ukončení činnosti API, což na UI produkuje error)</li>
<li>přidali si závislosti na objectbox (včetně pluginů) pro persistentní ukládání dat (vektorová DB, velmi rychlá) a pro datastore - též pro úkládání dat, ale většinou formou klíč-hodnota (např. nastavení app)</li>
<li>vytvořili nové instance (singletony) v AppModule a nové moduly přidali do inicializace koin v MainActivity</li>
<li>přidali novou datovou třídu FavouriteCryptoEntity, pro ukládání info o oblíbených kryptoměnách</li>
<li>upravili FavouriteCryptoRepository aby pracoval s ObjectBoxem, konkrétně s Box(em) pro FavouriteCryptoEntity</li>
<li>upravili FavouriteCryptoViewModel aby kromě repositáře pracoval i s API - tento viewmodel se bude starat o získání dat z repositáře a následné získání aktuální informací o oblíbených kryptoměnách, ty pak zobrazí UI</li>
<li>v rozhraní CryptoApi vytvořili novou funkci pro získávání všech kryptoměn filtrovaných na základě id</li>
<li>upravili všechny Screeny, které s FavouriteCryptoViewModelem pracovali, tak aby podporovali navrácený List kryptoměn zabalený v ResultApi</li>
<li>ve všech těchto Screenách přidali do LaunchedEffect i volání loadFavouriteCryptos() z FavouriteCryptoViewModelu</li>