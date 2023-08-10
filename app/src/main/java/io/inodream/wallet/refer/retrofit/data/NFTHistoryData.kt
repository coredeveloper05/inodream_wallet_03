package io.inodream.wallet.refer.retrofit.data

/**
 * <pre>
 *     author : zhen
 *     time   : 2023/08/09
 *     desc   :
 *     version: 1.0
 * </pre>
 */
data class NFTHistoryData(
    val address: String,
    val topics: List<String>,
    val data: String,
    val blockNumber: String,
    val blockHash: String,
    val timeStamp: String,
    val gasPrice: String,
    val gasUsed: String,
    val transactionHash: String,
    val transactionIndex: String,
    val type: String,
    val timeFormat: String,
    val fromAddress: String,
    val toAddress: String
)