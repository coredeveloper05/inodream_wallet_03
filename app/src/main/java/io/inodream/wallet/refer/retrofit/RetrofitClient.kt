package io.inodream.wallet.refer.retrofit

import io.inodream.wallet.refer.retrofit.service.IRemoteSimpleService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitClient {

    // api 주소
    private const val BASE_URL = "https://dev-fonback.eco-esgtest.io/"

    /**
     * auth
     */
    // auth_google
    const val AUTH_GOOGLE = "auth/google"
    const val AUTH_REFRESH = "auth/refresh"

    /**
     * api
     */
    // api 기능 단위 주소
    const val GET_TOKEN_INFOS = "api/tokenInfos"
    const val GET_TOKEN_INFO = "api/tokenInfo"

    /**
     * user
     */
    // 회원 정보를 확인합니다.
    const val GET_USER_INFO = "api/user/info"
    // 회원 지갑정보를 확인합니다.
    const val GET_USER_WALLET = "api/user/wallet"

    // 회원이 출금을 할때 필요한 출금암호를 등록합니다.
    const val SET_USER_WITH_DRAW_PW = "api/user/withdrawPw"

    // 회원 정보 업데이트
    const val GET_USER_PROFILE = "api/user/profile"

    /**
     * token
     */
    const val GET_BALANCE_ALL = "api/token/balanceAll"
    const val GET_BALANCE = "api/token/balance"
    const val SEND_COIN = "api/token/sendCoin"
    const val TX_LIST = "api/token/txList"


    // retrofit 클라이언트 빌드
    private val retrofitCliecnt: Retrofit by lazy {
        val okHttpBuilder = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
        Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpBuilder.build())
            .build()
    }

    // api 인터페이스 서비스 호출
    val remoteSimpleService: IRemoteSimpleService by lazy {
        retrofitCliecnt.create(IRemoteSimpleService::class.java)
    }
}