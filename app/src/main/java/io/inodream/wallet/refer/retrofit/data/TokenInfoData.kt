package io.inodream.wallet.refer.retrofit.data

/**
 * <pre>
 *     author : zhen
 *     time   : 2023/07/19
 *     desc   : TokenInfoData
 *     version: 1.0
 * </pre>
 */
data class TokenInfoData(
    val currency: String,
    val tokenInfos: TokenInfosData.TokenInfo,
    val scanner: TokenInfosData.Scanner
)