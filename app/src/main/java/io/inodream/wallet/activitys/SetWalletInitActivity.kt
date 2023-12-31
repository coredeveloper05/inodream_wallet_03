package io.inodream.wallet.activitys

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ToastUtils
import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
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

    private lateinit var btn: CircularProgressButton

    private val binding: ActivitySetWalletInitBinding by lazy {
        ActivitySetWalletInitBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        btn = binding.wallProCom02Tv
        btn.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        btn.startAnimation()
        RetrofitClient
            .remoteSimpleService
            .logout(RequestUtil().getRefreshHeader())
            .enqueue(object : Callback<BaseResponse<GoogleAuthData>> {
                override fun onResponse(
                    call: Call<BaseResponse<GoogleAuthData>>,
                    response: Response<BaseResponse<GoogleAuthData>>
                ) {
                    btn.revertAnimation()
                    if (!RequestUtil().checkResponse(response)) return
                    if (response.body()?.status == 1) {
                        UserManager.getInstance().clearData()
                        NftUtils.clearNFTData()
                        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestId()
                            .requestProfile()
                            .requestIdToken("437260015304-vm1cnjps4l7gn7nevdaj0530kmem2cro.apps.googleusercontent.com")
                            .requestEmail()
                            .build()
                        GoogleSignIn.getClient(this@SetWalletInitActivity, gso).signOut()
                        GoogleSignIn.getClient(this@SetWalletInitActivity, gso).revokeAccess()
                        startActivity(
                            Intent(this@SetWalletInitActivity, SocialLoginActivity::class.java)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        )
                    }
                }

                override fun onFailure(call: Call<BaseResponse<GoogleAuthData>>, t: Throwable) {
                    btn.revertAnimation()
                    t.printStackTrace()
                    ToastUtils.showLong(t.message)
                }
            })
    }
}