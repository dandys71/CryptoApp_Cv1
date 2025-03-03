package cz.uhk.fim.cryptoapp.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.uhk.fim.cryptoapp.api.ApiResult
import cz.uhk.fim.cryptoapp.api.CryptoApi
import cz.uhk.fim.cryptoapp.consts.CryptoHistoryInterval
import cz.uhk.fim.cryptoapp.data.Crypto
import cz.uhk.fim.cryptoapp.data.CryptoHistory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CryptoViewModel(private val cryptoApi: CryptoApi):ViewModel() {
    private val _cryptoList = MutableStateFlow<ApiResult<List<Crypto>>>(ApiResult.Loading)
    val cryptoList: StateFlow<ApiResult<List<Crypto>>> = _cryptoList.asStateFlow()

    private val _cryptoDetail = MutableStateFlow<ApiResult<Crypto>>(ApiResult.Loading)
    val cryptoDetail: StateFlow<ApiResult<Crypto>> = _cryptoDetail.asStateFlow()

    private val _cryptoHistory = MutableStateFlow<ApiResult<List<CryptoHistory>>>(ApiResult.Loading)
    val cryptoHistory: StateFlow<ApiResult<List<CryptoHistory>>> = _cryptoHistory.asStateFlow()


    fun getCryptoList() {
        viewModelScope.launch {
            _cryptoList.value = ApiResult.Loading
            try {
                val response = cryptoApi.getCryptoList()
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    if(data != null){
                        _cryptoList.value = ApiResult.Success(data)
                        Log.d("CryptoViewModel", "getCryptoList: ${response.body()?.data}")
                    }else{
                        _cryptoList.value = ApiResult.Error("Data is null")
                        Log.e("CryptoViewModel", "Data is null")
                    }
                } else {
                    _cryptoList.value = ApiResult.Error("Error fetching crypto list: ${response.message()}")
                    Log.e("CryptoViewModel", "Error fetching crypto list: ${response.message()}")
                }
            } catch (e: Exception) {
                _cryptoList.value = ApiResult.Error("Exception fetching crypto list: ${e.message}")
                Log.e("CryptoViewModel", "Exception fetching crypto list: ${e.message}")
            }
        }
    }

    //pouze ukázka, není nutné mít implementované
    fun getCryptoListByLimit(limit: Int) {
        viewModelScope.launch {
            _cryptoList.value = ApiResult.Loading
            try {
                val response = cryptoApi.getCryptoListByLimit(limit)
                if (response.isSuccessful) {
                    _cryptoList.value = ApiResult.Success(response.body()!!.data)
                    Log.d("CryptoViewModel", "getCryptoListBySymbolAndLimit: ${response.body()?.data}")
                } else {
                    _cryptoList.value = ApiResult.Error("Error fetching crypto list by symbol and limit: ${response.message()}")
                    Log.e("CryptoViewModel", "Error fetching crypto list by symbol and limit: ${response.message()}")
                }
            } catch (e: Exception) {
                _cryptoList.value = ApiResult.Error("Exception fetching crypto list by symbol and limit: ${e.message}")
                Log.e("CryptoViewModel", "Exception fetching crypto list by symbol and limit: ${e.message}")
            }
        }
    }

    //formou samostatné práce
    fun getCryptoDetail(id: String) {
        viewModelScope.launch {
            _cryptoDetail.value = ApiResult.Loading
            try {
                val response = cryptoApi.getCryptoDetail(id)
                if (response.isSuccessful) {
                    _cryptoDetail.value = ApiResult.Success(response.body()!!.data)
                } else {
                    _cryptoDetail.value = ApiResult.Error("Error fetching crypto detail: ${response.message()}")
                    Log.e("CryptoViewModel", "Error fetching crypto detail: ${response.message()}")
                }
            } catch (e: Exception) {
                _cryptoDetail.value = ApiResult.Error("Exception fetching crypto detail: ${e.message}")
                Log.e("CryptoViewModel", "Exception fetching crypto detail: ${e.message}")
            }
        }
    }

    //formou samostatné práce, zde jsme si pro možné hodnoty mohli vytvořit enum
    fun getCryptoHistory(id: String, interval: CryptoHistoryInterval) {
        viewModelScope.launch {
            _cryptoHistory.value = ApiResult.Loading
            try {
                val response = cryptoApi.getCryptoHistory(id, interval.toString())
                if (response.isSuccessful) {
                    _cryptoHistory.value = ApiResult.Success(response.body()!!.data)
                    Log.d("CryptoViewModel", "getCryptoHistory: ${cryptoHistory}")
                } else {
                    _cryptoHistory.value = ApiResult.Error("Error fetching crypto history: ${response.message()}")
                    Log.e("CryptoViewModel", "Error fetching crypto history: ${response.message()}")
                }
            } catch (e: Exception) {
                _cryptoHistory.value = ApiResult.Error("Exception fetching crypto history: ${e.message}")
                Log.e("CryptoViewModel", "Exception fetching crypto history: ${e.message}")
            }
        }
    }



}