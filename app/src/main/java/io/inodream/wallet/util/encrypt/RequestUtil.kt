package io.inodream.wallet.util.encrypt

import android.content.Intent
import android.util.Log
import com.blankj.utilcode.util.Utils
import com.google.gson.Gson
import io.inodream.wallet.activitys.SocialLoginActivity
import io.inodream.wallet.event.RefreshEvent
import io.inodream.wallet.refer.retrofit.RetrofitClient
import io.inodream.wallet.refer.retrofit.data.BaseResponse
import io.inodream.wallet.refer.retrofit.data.GoogleAuthData
import io.inodream.wallet.util.UserManager
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * <pre>
 *     author : zhen
 *     time   : 2023/07/21
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class RequestUtil {
    private var isRequestRefresh: Boolean = false

    public fun getRequestHeader(): MutableMap<String, String> {
        val map: MutableMap<String, String> = HashMap()
        map["Authorization"] = "Bearer " + UserManager.getInstance().accToken
        return map
    }

    fun getRefreshHeader(): MutableMap<String, String> {
        val map: MutableMap<String, String> = HashMap()
        map["Authorization"] = "Bearer " + UserManager.getInstance().accToken
        map["Refresh"] = UserManager.getInstance().refreshToken
        return map
    }

    fun checkResponse(code: Int): Boolean {
        if (code == 401) {
            if (isRequestRefresh) return false
            isRequestRefresh = true
            RetrofitClient
                .remoteSimpleService
                .authRefresh(RequestUtil().getRefreshHeader())
                .enqueue(object : Callback<BaseResponse<GoogleAuthData>> {
                    override fun onResponse(
                        call: Call<BaseResponse<GoogleAuthData>>,
                        response: Response<BaseResponse<GoogleAuthData>>
                    ) {
                        isRequestRefresh = false
                        response.body()?.let { baseResponse ->
                            baseResponse.data?.let {
                                Log.e("auth", Gson().toJson(it))
                                UserManager.getInstance().accToken = it.accessToken
                                EventBus.getDefault().post(RefreshEvent())
                                return
                            }
                        }

                        Utils.getApp().startActivity(
                            Intent(Utils.getApp(), SocialLoginActivity::class.java).setFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                    }

                    override fun onFailure(call: Call<BaseResponse<GoogleAuthData>>, t: Throwable) {
                        isRequestRefresh = false
                        t.printStackTrace()
                        Utils.getApp().startActivity(
                            Intent(Utils.getApp(), SocialLoginActivity::class.java).setFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                    }
                })
            return false
        }
        return true
    }
}