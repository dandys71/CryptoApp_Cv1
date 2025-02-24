**1. Cvičení**
<p>Na prvním cvičení jsme si:</p>
<li>nastavili Android Studio a přidali stažené SDK</li>
<li>vytvořili virtuální zařízení a ukázali si, jak aplikaci nahrávat do svého mobilního telefonu s OS Android</li>
<li>ukázali si základní soubory v rámci nového projektu - AndroidManifest.xml, Gradle soubory, MainActivity</li>

**2. Cvičení**
<p>Na druhém cvičení jsme si:</p>
<li>vytvořili datovou třídu pro kryptoměny a přidali funkce do repositáře kryptoměn pro získání listu a kryptoměny podle id.</li>
<li>ukázali základní komponenty pro pozicování Row() a Column() a vyzkoušeji jsme si i komponenty jako Text(), Image(), Icon() či Buttton().</li>
<li>vysvětlili, jak funguje navigace, respektive navigační controller a přidali jsme si vlastní routu pro detail kryptoměny.</li>
<li>podle návrhu samostatně vyzkoušeli vytvořit vlastní komponentu pro detail kryptoměny.</li>

**3. Cvičení**
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
