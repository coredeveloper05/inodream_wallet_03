package io.inodream.wallet.refer.retrofit.service

import com.google.gson.JsonObject
import io.inodream.wallet.refer.retrofit.RetrofitClient
import io.inodream.wallet.refer.retrofit.data.BalanceData
import io.inodream.wallet.refer.retrofit.data.BaseResponse
import io.inodream.wallet.refer.retrofit.data.GoogleAuthData
import io.inodream.wallet.refer.retrofit.data.RemoteSimpleData
import io.inodream.wallet.refer.retrofit.data.TokenInfoData
import io.inodream.wallet.refer.retrofit.data.TokenInfosData
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 * api 기능 단위 인터페이스
 */
interface IRemoteSimpleService {

    @GET("http://3.26.205.235:8008/api/getToken2")
    fun auth(@QueryMap map: MutableMap<String, String>): Call<JsonObject>

    @POST(RetrofitClient.AUTH_GOOGLE)
    fun authGoogle(@Body map: MutableMap<String, String>): Call<BaseResponse<GoogleAuthData>>

    @POST(RetrofitClient.AUTH_REFRESH)
    fun authRefresh(@HeaderMap map: MutableMap<String, String>): Call<BaseResponse<GoogleAuthData>>

    @GET(RetrofitClient.GET_USER_WALLET)
    fun getUserWallet(@HeaderMap headerMap: MutableMap<String, String>): Call<BaseResponse<GoogleAuthData.WalletData>>

    @GET(RetrofitClient.GET_BALANCE_ALL)
    fun getBalanceAll(@HeaderMap headerMap: MutableMap<String, String>): Call<BaseResponse<BalanceData>>

    @GET(RetrofitClient.GET_BALANCE)
    fun getBalance(@HeaderMap headerMap: MutableMap<String, String>, @QueryMap map: MutableMap<String, String>): Call<BaseResponse<BalanceData>>

    @GET(RetrofitClient.GET_TOKEN_INFOS)
    fun getTokenInfos(@HeaderMap headerMap: MutableMap<String, String>): Call<BaseResponse<TokenInfosData>>

    @GET(RetrofitClient.GET_TOKEN_INFO)
    fun getTokenInfo(@HeaderMap headerMap: MutableMap<String, String>, @QueryMap map: MutableMap<String, String>): Call<BaseResponse<TokenInfoData>>


}