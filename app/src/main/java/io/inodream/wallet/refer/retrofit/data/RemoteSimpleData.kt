package io.inodream.wallet.refer.retrofit.data

import com.google.gson.annotations.SerializedName

/**
 * api 결과 값 바인딩
 */
data class RemoteSimpleData(

    /**
     * 실제 json 네이밍과 동일하게 하면 자동 바인딩
     * 변수명과 동일하게 해도 되지만 직렬화 이슈로 annotation 권장
     */
    @SerializedName("status")
    val status : String,

    @SerializedName("data")
    val data: RemoteSimpleTokenData,

    val message: String
)

data class RemoteSimpleTokenData(

    @SerializedName("currency")
    val currency: String,

    @SerializedName("tokenInfos")
    val tokenInfos: Array<RemoteSimpleTokenInfoData>,

    @SerializedName("scanner")
    val scanner: Array<RemoteSimpleScannerData>

)

data class RemoteSimpleTokenInfoData(

    @SerializedName("idx")
    val idx: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("decimals")
    val decimals: String,

    @SerializedName("priceDecimals")
    val priceDecimals: String,

    @SerializedName("contractAddress")
    val contractAddress: String,

    @SerializedName("price")
    val price: String
)

data class RemoteSimpleScannerData(

    @SerializedName("idx")
    val idx: String,

    @SerializedName("chainId")
    val chainId: String
)