package cz.uhk.fim.cryptoapp.api


import cz.uhk.fim.cryptoapp.data.Crypto
import cz.uhk.fim.cryptoapp.data.CryptoHistory
import cz.uhk.fim.cryptoapp.data.Rate
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//interface nabízející funkce pro volání endpointů (Retrofit na základě tohoto rozhraní vytvoří implementaci)
interface CryptoApi {
    @GET("assets")
    suspend fun getCryptoList(): Response<CryptoResponse<List<Crypto>>>

    @GET("assets")
    suspend fun getCryptoListByLimit(
        @Query("limit") limit: Int
    ): Response<CryptoResponse<List<Crypto>>>

    @GET("assets")
    suspend fun getCryptoListByIds(@Query("ids") cryptoIds: String): Response<CryptoResponse<List<Crypto>>>

    @GET("assets/{id}")
    suspend fun getCryptoDetail(
        @Path("id") id: String
    ): Response<CryptoResponse<Crypto>>

    @GET("assets/{id}/history")
    suspend fun getCryptoHistory(
        @Path("id") id: String,
        @Query("interval") interval: String
    ): Response<CryptoResponse<List<CryptoHistory>>>

    @GET("rates")
    suspend fun getRates(): Response<CryptoResponse<List<Rate>>>
}