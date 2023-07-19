package io.inodream.wallet.util

import com.google.gson.Gson

/**
 * <pre>
 *     author : zhen
 *     time   : 2023/07/18
 *     desc   : StringUtils
 *     version: 1.0
 * </pre>
 */
class StringUtils {
    fun map2Str(map: MutableMap<String, String>?): String {
        return if (map.isNullOrEmpty()) {
            "{}"
        } else Gson().toJson(map)
    }
}