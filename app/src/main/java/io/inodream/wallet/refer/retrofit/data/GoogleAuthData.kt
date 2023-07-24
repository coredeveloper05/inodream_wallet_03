package io.inodream.wallet.refer.retrofit.data

/**
 * <pre>
 *     author : zhen
 *     time   : 2023/07/18
 *     desc   : GoogleAuthData
 *     version: 1.0
 * </pre>
 */
data class GoogleAuthData(
    val uid: String,
    val email: String,
    val username: String,
    // 회원 상태 0 정상, 1 블럭
    val status: String,
    var accessToken: String,
    val accessExpiresIn: String,
    val refreshToken: String,
    val refreshExpiresIn: String,
    var withdrawPw: Boolean,
    var wallet: WalletData
) {
    data class WalletData(
        val address: String,
        val type: String,
        val seed: String,

        val uid: String,
        val createdAt: String,
        val updatedAt: String
    )
}