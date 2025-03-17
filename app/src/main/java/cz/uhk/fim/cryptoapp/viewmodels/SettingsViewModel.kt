package cz.uhk.fim.cryptoapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.uhk.fim.cryptoapp.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingsRepository: SettingsRepository) :
    ViewModel() {

        //stateIn změní cold flow (pro každého odběratele nová instance), kde se o změny staráme ručně,
        // např. voláním metody loadRates, na hot (jedna instance pro všechny odběratele),
        // který o změnách informuje všechny odběratele
        //state in příjímá couroutine scope ve kterém běží daný kód, tzn. getPreferredRateId(), kdy a jak se má začít / ukončit
        // reagovat na změny - ihned při přihlášení prvního odběratele a 5s po odhlášení posledního, a init value
    val preferredRateId: StateFlow<String?> = settingsRepository.getPreferredRateId()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun savePreferredRateId(rateId: String) {
        viewModelScope.launch {
            settingsRepository.savePreferredRateId(rateId)
        }
    }
}