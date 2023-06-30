package io.inodream.wallet.refer.retrofit

import io.inodream.wallet.refer.retrofit.service.IRemoteSimpleService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // api 주소
    private const val BASE_URL = "https://dev-zqbackend.eco-esgtest.io/"

    // api 기능 단위 주소
    const val GET_TOKEN_INFOS = "api/tokenInfos"

    // retrofit 클라이언트 빌드
    private val retrofitCliecnt: Retrofit by lazy {
        Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    // api 인터페이스 서비스 호출
    val remoteSimpleService: IRemoteSimpleService by lazy {
        retrofitCliecnt.create(IRemoteSimpleService::class.java)
    }
}