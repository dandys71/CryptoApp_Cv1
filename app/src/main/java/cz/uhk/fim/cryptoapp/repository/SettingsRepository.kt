package cz.uhk.fim.cryptoapp.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import cz.uhk.fim.cryptoapp.consts.SettingsKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(private val dataStore: DataStore<Preferences>) {

    /**
     * Získá ID preferované měny z DataStore.
     *
     * @return Flow s ID preferované měny.
     */
    fun getPreferredRateId(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[SettingsKeys.PREFERRED_RATE_ID]
        }
    }

    /**
     * Uloží ID preferované měny do DataStore.
     *
     * @param rateId ID preferované měny.
     */
    suspend fun savePreferredRateId(rateId: String) {
        dataStore.edit { preferences ->
            preferences[SettingsKeys.PREFERRED_RATE_ID] = rateId
        }
    }
}