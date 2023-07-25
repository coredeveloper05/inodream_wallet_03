package io.inodream.wallet.activitys

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ToastUtils
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.inodream.wallet.databinding.ActivitySocialLoginBinding
import io.inodream.wallet.refer.retrofit.RetrofitClient
import io.inodream.wallet.refer.retrofit.data.BaseResponse
import io.inodream.wallet.refer.retrofit.data.GoogleAuthData
import io.inodream.wallet.util.UserManager
import io.inodream.wallet.util.encrypt.RequestUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SocialLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySocialLoginBinding
    val RC_SIGN_IN = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySocialLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!TextUtils.isEmpty(UserManager.getInstance().accToken)) {
            startActivity(Intent(this, SocialTermAgreeActivity::class.java))
//            authRefresh()
        } else
            binding.socialLoginButton.setOnClickListener {
                login()
            }
    }

    fun login() {
        //初始化gso，server_client_id为添加的客户端id
        //初始化gso，server_client_id为添加的客户端id
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestId()
            .requestProfile()
            .requestIdToken("437260015304-vm1cnjps4l7gn7nevdaj0530kmem2cro.apps.googleusercontent.com")
            .requestEmail()
            .build()
        //初始化Google登录实例,activity为当前activity
        //初始化Google登录实例,activity为当前activity
        val mGoogleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(this, gso)
        //登录前可以查看是否已经授权，已经授权则可不必重复授权，如果返回的额account不为空则已经授权，同理activity为当前activity
        //登录前可以查看是否已经授权，已经授权则可不必重复授权，如果返回的额account不为空则已经授权，同理activity为当前activity
        val account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)
        //如果未授权则可以调用登录，mGoogleSignInClient为初始化好的Google登录实例，RC_SIGN_IN为随意唯一返回标识码，int即可。
        //如果未授权则可以调用登录，mGoogleSignInClient为初始化好的Google登录实例，RC_SIGN_IN为随意唯一返回标识码，int即可。
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            val account: GoogleSignInAccount
            try {
                account = task.getResult(ApiException::class.java)
                val map: MutableMap<String, String> = HashMap()
                map["name"] = account.displayName!!
                map["email"] = account.email!!
                map["googleId"] = account.id!!
                map["1"] = "1"
                auth(map)
//                val map: MutableMap<String, Any> = java.util.HashMap()
//                map["account"] = account.account!!.name
//                map["token"] = account.idToken!!
//                Log.e("Login", "account--------------->>>" + Gson().toJson(map))
            } catch (e: ApiException) {
                e.printStackTrace()
            }
        }
    }

    private fun auth(map: MutableMap<String, String>) {
        RetrofitClient
            .remoteSimpleService
            .auth(map)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (!RequestUtil().checkResponse(response)) return
                    response.body()?.let {
                        authGoogle(it.get("encode")?.asString?:"")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    t.printStackTrace()
                    ToastUtils.showLong(t.message)
                }
            })
    }

    private fun authGoogle(token: String) {
        if (TextUtils.isEmpty(token)) {
            ToastUtils.showLong("获取信息失败")
            return
        }
        val map: MutableMap<String, String> = HashMap()
        map["token"] = token
        RetrofitClient
            .remoteSimpleService
            .authGoogle(map)
            .enqueue(object : Callback<BaseResponse<GoogleAuthData>> {
                override fun onResponse(
                    call: Call<BaseResponse<GoogleAuthData>>,
                    response: Response<BaseResponse<GoogleAuthData>>
                ) {
                    if (!RequestUtil().checkResponse(response)) return
                    response.body()?.let { baseResponse ->
                        baseResponse.data?.let {
                            UserManager.getInstance().setUser(it)
                            Log.e("auth", Gson().toJson(it))
                            startActivity(
                                Intent(
                                    this@SocialLoginActivity,
                                    SocialTermAgreeActivity::class.java
                                )
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<BaseResponse<GoogleAuthData>>, t: Throwable) {
                    t.printStackTrace()
                    ToastUtils.showLong(t.message)
                }
            })
    }

    private fun authRefresh() {
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
                            startActivity(Intent(this@SocialLoginActivity, WalletMainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                        }
                    }
                }

                override fun onFailure(call: Call<BaseResponse<GoogleAuthData>>, t: Throwable) {
                    t.printStackTrace()
                    ToastUtils.showLong(t.message)
                }
            })
    }
}