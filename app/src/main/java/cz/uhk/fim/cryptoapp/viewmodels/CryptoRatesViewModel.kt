package cz.uhk.fim.cryptoapp.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.uhk.fim.cryptoapp.api.ApiResult
import cz.uhk.fim.cryptoapp.api.CryptoApi
import cz.uhk.fim.cryptoapp.data.Rate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//formou samostatné práce
class CryptoRatesViewModel(private val cryptoApi: CryptoApi) : ViewModel() {

    private val _rates= MutableStateFlow<ApiResult<List<Rate>>>(ApiResult.Loading)
    val rates: StateFlow<ApiResult<List<Rate>>> = _rates.asStateFlow()

    fun getRates() {
        viewModelScope.launch {
            _rates.value = ApiResult.Loading
            try {
                val response = cryptoApi.getRates()
                if (response.isSuccessful) {
                    _rates.value = ApiResult.Success(response.body()!!.data)
                    Log.d("CryptoViewModel", "getRates: ${rates}")
                } else {
                    _rates.value = ApiResult.Error("Error fetching rates: ${response.message()}")
                    Log.e("CryptoViewModel", "Error fetching rates: ${response.message()}")
                }
            } catch (e: Exception) {
                _rates.value = ApiResult.Error("Exception fetching rates: ${e.message}")
                Log.e("CryptoViewModel", "Exception fetching rates: ${e.message}")
            }
        }
    }
}