package io.inodream.wallet.refer.retrofit.data

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    val state: String,
    val message: String,
    val data: T?
) {
    val isSuccess: Boolean = "1" == state
}