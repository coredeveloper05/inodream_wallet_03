package io.inodream.wallet.util

import android.text.TextUtils
import com.google.gson.Gson
import java.text.DecimalFormat

/**
 * <pre>
 *     author : zhen
 *     time   : 2023/07/18
 *     desc   : StringUtils
 *     version: 1.0
 * </pre>
 */
class StringUtils {
    companion object {
        fun map2Str(map: MutableMap<String, String>?): String {
            return if (map.isNullOrEmpty()) {
                "{}"
            } else Gson().toJson(map)
        }

        fun getTwoDigits(double: Double): String {
            val format = DecimalFormat("0.##")
            return format.format(double)
        }

        fun getSixDigits(double: Double): String {
            val format = DecimalFormat("0.######")
            return format.format(double)
        }

        fun getShortAddress(address: String?): String {
            if (TextUtils.isEmpty(address)) {
                return ""
            }
            if (address!!.length < 17) {
                return address
            }
            return address.substring(0, 7) + "..." + address.substring(address.length - 10)
        }

        fun getShortAddressAndId(address: String?, id: String?): String {
            val addressStr = if (TextUtils.isEmpty(address)) {
                ""
            } else if (address!!.length < 6) {
                address
            } else {
                address.substring(0, 3) + "..." + address.substring(address.length - 3)
            }
            val idStr = if (TextUtils.isEmpty(id)) {
                ""
            } else if (id!!.length < 6) {
                id
            } else {
                id.substring(0, 3) + "..." + id.substring(id.length - 3)
            }
            return "${addressStr}#${idStr}"
        }
    }
}