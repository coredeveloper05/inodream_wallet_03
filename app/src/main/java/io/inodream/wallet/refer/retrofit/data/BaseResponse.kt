package io.inodream.wallet.refer.retrofit.data

data class BaseResponse<T>(
    val status: String,
    val message: String,
    val errorCode: String,
    val data: T?
)