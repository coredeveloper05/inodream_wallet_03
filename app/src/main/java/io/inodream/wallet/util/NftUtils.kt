package io.inodream.wallet.util

import android.text.TextUtils
import com.blankj.utilcode.util.SPUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.inodream.wallet.refer.retrofit.data.NFTListData

/**
 * <pre>
 *     author : zhen
 *     time   : 2023/07/30
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class NftUtils {
    companion object {
        private const val SP_FILE_NFT = "nft"
        private val allData = ArrayList<NFTListData.NFTData>()
        private val nftData = arrayListOf(
            NFTListData.NFTData(
                "0xf4910c763ed4e47a585e2d34baa9a4b611ae448c",
                "76039954904669227696257918303144183647734773931256600432704802769631046533220"
            ),
            NFTListData.NFTData(
                "0xf4910c763ed4e47a585e2d34baa9a4b611ae448c",
                "76039954904669227696257918303144183647734773931256600432704802770730558160996"
            )
        )

        fun getNftList(): ArrayList<NFTListData.NFTData> {
            if (allData.size == 0) {
                allData.addAll(nftData)
                val string =
                    SPUtils.getInstance(SP_FILE_NFT).getString(UserManager.getInstance().uid)
                if (!TextUtils.isEmpty(string)) {
                    val list = Gson().fromJson<ArrayList<NFTListData.NFTData>>(
                        string, object : TypeToken<ArrayList<NFTListData.NFTData>>() {}.type
                    )
                    allData.addAll(list)
                }
            }
            return allData
        }

        fun saveNftData(address: String, id: String) {
            if (TextUtils.isEmpty(address) || TextUtils.isEmpty(id)) return
            val data = NFTListData.NFTData(address, id)
            allData.add(data)
            val string = SPUtils.getInstance(SP_FILE_NFT).getString(UserManager.getInstance().uid)
            val list: ArrayList<NFTListData.NFTData> = if (TextUtils.isEmpty(string)) {
                ArrayList()
            } else {
                Gson().fromJson(
                    string,
                    object : TypeToken<ArrayList<NFTListData.NFTData>>() {}.type
                )
            }
            list.add(data)
            SPUtils.getInstance(SP_FILE_NFT).put(UserManager.getInstance().uid, Gson().toJson(list))
        }

        fun clearNFTData() {
            allData.clear()
        }
    }
}