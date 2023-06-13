package io.inodream.wallet.core.data

data class TokenItemData(var tokenType: String, var tokenValue: String, var lastItemFlag: Boolean = false)

data class NftItemData(val nftType: String, val nftName: String, val nftCreatorName: String, val nftValue: String)