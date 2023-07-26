package io.inodream.wallet.util.encrypt

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.blankj.utilcode.util.ToastUtils
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

    fun getRequestHeader(): MutableMap<String, String> {
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

    fun checkResponse(response: Response<*>): Boolean {
        if (response.code() == 401) {
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
                        if (!RequestUtil().checkResponse(response)) return
                        response.body()?.let { baseResponse ->
                            baseResponse.data?.let {
                                Log.e("auth", Gson().toJson(it))
                                UserManager.getInstance().accToken = it.accessToken
                                EventBus.getDefault().post(RefreshEvent())
                                isRequestRefresh = false
                            }
                        }
                        if (isRequestRefresh) {
                            isRequestRefresh = false
                            UserManager.getInstance().clearData()
                            Utils.getApp().startActivity(
                                Intent(Utils.getApp(), SocialLoginActivity::class.java).setFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            )
                        }
                    }

                    override fun onFailure(call: Call<BaseResponse<GoogleAuthData>>, t: Throwable) {
                        isRequestRefresh = false
                        t.printStackTrace()
                        ToastUtils.showLong(t.message)
                        UserManager.getInstance().clearData()
                        Handler(Looper.getMainLooper()).post {
                            Utils.getApp().startActivity(
                                Intent(Utils.getApp(), SocialLoginActivity::class.java).setFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            )
                        }
                    }
                })
            return false
        } else if (response.code() != 200) {
            ToastUtils.showLong(
                response.raw().request().url().uri().path + "=" + response.raw().message()
            )
            return false
        } else {
            val errMsg: String
            if (response.body() is BaseResponse<*>) {
                if ((response.body() as BaseResponse<*>).status != "1") {
                    errMsg = (response.body() as BaseResponse<*>).message
                    ToastUtils.showLong(response.raw().request().url().uri().path + ":$errMsg")
                }
            }
        }
        return true
    }
}