package io.inodream.wallet.refer.retrofit.service

import com.google.gson.JsonElement
import io.inodream.wallet.refer.retrofit.RetrofitClient
import io.inodream.wallet.refer.retrofit.data.RemoteSimpleData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.HEAD
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * api 기능 단위 인터페이스
 */
interface IRemoteSimpleService {

    @GET(RetrofitClient.GET_TOKEN_INFOS)
    fun getTokenInfos(
        @Query("simpleParam") param1: String
    ): Call<RemoteSimpleData>

}