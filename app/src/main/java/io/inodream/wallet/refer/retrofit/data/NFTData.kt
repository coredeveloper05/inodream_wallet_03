package io.inodream.wallet.refer.retrofit.data

import java.io.Serializable

data class NFTData(
    val address: String,
    val id: String,
    val standard: String? = "",
    var balanceOfUser: String = "0",
    val uri: String?,
    val contractName: String?,
    val creator: String?,
    var metadata: MetaData?
) : Serializable {
    constructor(address: String, id: String) : this(
        address,
        id,
        null,
        "0",
        null,
        null,
        null,
        null
    )

    data class MetaData(
        val image: String? = "",
        val name: String? = "",
        val description: String? = "",
        val external_link: String?,
        val animation_url: String?
    ) : Serializable
}