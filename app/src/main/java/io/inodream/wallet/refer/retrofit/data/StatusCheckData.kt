package io.inodream.wallet.refer.retrofit.data

/**
 * <pre>
 *     author : zhen
 *     time   : 2023/07/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */
data class StatusCheckData(
    // 트랜젝션 타입, Transfer, Swap
    val type: String,
    // 상태 0 생성, 1 확인, 2 승인, 3 종료
    val status: String,
    // Transfer의 symbol
    val symbol: String,
    // Transfer의 트랜젝션 Id
    val txId: String,
    // Swap의 대상 심볼
    val fromSymbol: String,
    // Swap의 대상 트랜젝션 ID
    val fromTxId: String,
    // Swap의 목적 심볼
    val toSymbol: String,
    // Swap의 목적 트랜젝션 ID
    val toTxId: String
)