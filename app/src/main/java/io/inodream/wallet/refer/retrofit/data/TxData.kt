package io.inodream.wallet.refer.retrofit.data

/**
 * <pre>
 *     author : zhen
 *     time   : 2023/07/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */
data class TxData(
    // 토큰의 심볼. e.g. ETH
    val symbol: String,
    // 조회한 계정의 주소. '0x'로 시작되는 hex string
    val address: String,
    val data: List<Data>
) {
    data class Data(
        // 고유번호
        val idx: String,
        // 체인아이디
        val chainId: String,
        // 트랜젝션아이디
        val txId: String,
        // 심볼
        val symbol: String,
        // 보낸주소
        val from: String,
        // 받는주소
        val to: String,
        // 컴펌블럭번호
        val blockNumberConfirm: String,
        // 상태 0생성,1진행,2확인,3완료
        val status: String,
        // 전송갯수
        val value: String,
        // 네트워크 수수료
        val gasPrice: String
    )
}