package io.inodream.wallet.activitys

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils
import io.inodream.wallet.databinding.ActivitySetWalletInitBinding
import io.inodream.wallet.refer.retrofit.RetrofitClient
import io.inodream.wallet.refer.retrofit.data.BaseResponse
import io.inodream.wallet.refer.retrofit.data.GoogleAuthData
import io.inodream.wallet.util.NftUtils
import io.inodream.wallet.util.UserManager
import io.inodream.wallet.util.encrypt.RequestUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SetWalletInitActivity : AppCompatActivity() {

    private val binding: ActivitySetWalletInitBinding by lazy {
        ActivitySetWalletInitBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.wallProCom02Tv.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        RetrofitClient
            .remoteSimpleService
            .logout(RequestUtil().getRefreshHeader())
            .enqueue(object : Callback<BaseResponse<GoogleAuthData>> {
                override fun onResponse(
                    call: Call<BaseResponse<GoogleAuthData>>,
                    response: Response<BaseResponse<GoogleAuthData>>
                ) {
                    if (!RequestUtil().checkResponse(response)) return
                    if (response.body()?.status == "1") {
                        UserManager.getInstance().clearData()
                        NftUtils.clearNFTData()
                        Intent(Utils.getApp(), SocialLoginActivity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                }

                override fun onFailure(call: Call<BaseResponse<GoogleAuthData>>, t: Throwable) {
                    t.printStackTrace()
                    ToastUtils.showLong(t.message)
                }
            })
    }
}