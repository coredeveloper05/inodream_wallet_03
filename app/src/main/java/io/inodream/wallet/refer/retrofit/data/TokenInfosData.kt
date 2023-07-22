package io.inodream.wallet.refer.retrofit.data

import java.io.Serializable


/**
 * <pre>
 *     author : zhen
 *     time   : 2023/07/19
 *     desc   : TokenInfoData
 *     version: 1.0
 * </pre>
 */
data class TokenInfosData(
    val currency: String?,
    val tokenInfos: ArrayList<TokenInfo>?,
    val scanner: List<Scanner>?
) {
    data class TokenInfo(
        val idx: String,
        val chainId: String,
        val type: String,
        val symbol: String,
        val name: String,
        val decimals: String,
        val priceDecimals: String,
        val icon: String,
        val contractAddress: String,
        val price: String
    ) : Serializable

    data class Scanner(
        val idx: String,
        val chainId: String,
        val url: String
    )
}