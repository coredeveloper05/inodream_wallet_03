package io.inodream.wallet.refer.retrofit.service

import com.google.gson.JsonObject
import io.inodream.wallet.refer.retrofit.RetrofitClient
import io.inodream.wallet.refer.retrofit.data.BalanceData
import io.inodream.wallet.refer.retrofit.data.BalancesData
import io.inodream.wallet.refer.retrofit.data.BaseResponse
import io.inodream.wallet.refer.retrofit.data.GoogleAuthData
import io.inodream.wallet.refer.retrofit.data.NFTListData
import io.inodream.wallet.refer.retrofit.data.TokenInfoData
import io.inodream.wallet.refer.retrofit.data.TokenInfosData
import io.inodream.wallet.refer.retrofit.data.TokenQuoteData
import io.inodream.wallet.refer.retrofit.data.TxData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.QueryMap
import retrofit2.http.Url

/**
 * api 기능 단위 인터페이스
 */
interface IRemoteSimpleService {

    @GET("http://3.26.205.235:8008/api/getToken")
    fun auth(@QueryMap map: MutableMap<String, String>): Call<JsonObject>

    @POST("http://3.26.205.235:8008/api/decodeSeed")
    fun decodeSeed(@Body map: MutableMap<String, String>): Call<JsonObject>

    @GET("http://3.26.205.235:8008/api/quote")
    fun quoteToken(@QueryMap map: MutableMap<String, String>): Call<TokenQuoteData>

    @POST("http://3.26.205.235:8008/api/encodeText")
    fun encodeText(@Body map: MutableMap<String, String>): Call<JsonObject>

    @POST("http://3.26.205.235:8008/api/swap")
    fun swapChange(@Body map: MutableMap<String, Any>): Call<JsonObject>

    @POST("http://3.26.205.235:8008/api/getNftList2")
    fun getNftList(@Body map: MutableMap<String, Any>): Call<NFTListData>

    @POST("http://3.26.205.235:8008/api/transferNft")
    fun transferNft(@Body map: MutableMap<String, Any>): Call<JsonObject>

    @POST("http://3.26.205.235:8008/api/estimateTransferGasFee")
    fun estimateTransferGasFee(@Body map: MutableMap<String, String>): Call<JsonObject>

    @POST(RetrofitClient.AUTH_GOOGLE)
    fun authGoogle(@Body map: MutableMap<String, String>): Call<BaseResponse<GoogleAuthData>>

    @POST(RetrofitClient.AUTH_REFRESH)
    fun authRefresh(@HeaderMap map: MutableMap<String, String>): Call<BaseResponse<GoogleAuthData>>

    @POST(RetrofitClient.AUTH_LOGOUT)
    fun logout(@HeaderMap map: MutableMap<String, String>): Call<BaseResponse<GoogleAuthData>>

    @GET(RetrofitClient.GET_USER_WALLET)
    fun getUserWallet(@HeaderMap headerMap: MutableMap<String, String>): Call<BaseResponse<GoogleAuthData.WalletData>>

    @GET(RetrofitClient.GET_BALANCE_ALL)
    fun getBalanceAll(@HeaderMap headerMap: MutableMap<String, String>): Call<BaseResponse<BalancesData>>

    @GET(RetrofitClient.GET_BALANCE)
    fun getBalance(
        @HeaderMap headerMap: MutableMap<String, String>,
        @QueryMap map: MutableMap<String, String>
    ): Call<BaseResponse<BalanceData>>

    @GET(RetrofitClient.GET_TOKEN_INFOS)
    fun getTokenInfos(@HeaderMap headerMap: MutableMap<String, String>): Call<BaseResponse<TokenInfosData>>

    @GET(RetrofitClient.GET_TOKEN_INFO)
    fun getTokenInfo(
        @HeaderMap headerMap: MutableMap<String, String>,
        @QueryMap map: MutableMap<String, String>
    ): Call<BaseResponse<TokenInfoData>>

    @POST(RetrofitClient.SEND_COIN)
    fun sendCoin(
        @HeaderMap map: MutableMap<String, String>,
        @Body body: MutableMap<String, String>
    ): Call<BaseResponse<Any>>

    @POST(RetrofitClient.SET_USER_WITH_DRAW_PW)
    fun withDrawPwd(
        @HeaderMap map: MutableMap<String, String>,
        @Body body: MutableMap<String, String>
    ): Call<BaseResponse<JsonObject>>

    @GET(RetrofitClient.TX_LIST)
    fun listTX(
        @HeaderMap map: MutableMap<String, String>,
        @QueryMap body: MutableMap<String, String>
    ): Call<BaseResponse<List<TxData.Data>>>

    @POST(RetrofitClient.AUTH_SIGN_START)
    fun signStart(
        @HeaderMap map: MutableMap<String, String>,
        @Body body: MutableMap<String, String>
    ): Call<BaseResponse<String>>

    @POST(RetrofitClient.AUTH_SIGN_VERIFY)
    fun signVerify(
        @HeaderMap map: MutableMap<String, String>,
        @Body body: MutableMap<String, String>
    ): Call<BaseResponse<Any>>

    @POST(RetrofitClient.AUTH_RESET_WITH_DRAW_PW)
    fun resetWithDrawPw(
        @HeaderMap map: MutableMap<String, String>,
        @Body body: MutableMap<String, String>
    ): Call<BaseResponse<Boolean>>

    @GET
    fun getData(@Url url: String): Call<JsonObject>
}