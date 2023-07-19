package io.inodream.wallet.refer.retrofit.data

/**
 * <pre>
 *     author : zhen
 *     time   : 2023/07/19
 *     desc   : BalanceData
 *     version: 1.0
 * </pre>
 */
data class BalanceData(
    // 토큰의 심볼. e.g. ETH
    val symbol: String,
    // 조회한 계정의 주소. '0x'로 시작되는 hex string
    val address: String,
    // 토큰 잔액(소수자리 포함)
    val balance: String
)