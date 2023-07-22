package io.inodream.wallet.activitys

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.JsonObject
import io.inodream.wallet.R
import io.inodream.wallet.databinding.ActivityTokenSendRequestBinding
import io.inodream.wallet.refer.retrofit.RetrofitClient
import io.inodream.wallet.refer.retrofit.data.BaseResponse
import io.inodream.wallet.util.UserManager
import io.inodream.wallet.util.encrypt.RequestUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TokenSendRequestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTokenSendRequestBinding
    private var privateKey: String = ""
    private var symbol: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTokenSendRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        symbol = intent.getStringExtra("key") ?: ""
        binding.topToolbar.title.text = resources.getString(R.string.title_token_send)

        getPrivateKey()
        setListener()
    }

    private fun getPrivateKey() {
        val map: MutableMap<String, String> = HashMap()
        map["seed"] = UserManager.getInstance().walletData.seed
        RetrofitClient
            .remoteSimpleService
            .decodeSeed(map)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    privateKey = response.body()?.get("privateKey")?.asString ?: ""
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }

    private fun setListener() {
        binding.topToolbar.backButton.setOnClickListener {
            finish()
        }
        binding.sendRequestButton.setOnClickListener {
            if (checkForm()) {
                sendCoin()
            }
        }
        binding.llScan.setOnClickListener {
            startActivityForResult(
                Intent(
                    this@TokenSendRequestActivity,
                    CameraCaptureActivity::class.java
                ), 100
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            binding.sampleEditText.setText(data?.getStringExtra("result"))
        }
    }

    private fun checkForm(): Boolean {
        if (TextUtils.isEmpty(binding.sampleEditText.text.toString())) {
            ToastUtils.showLong(R.string.check_error_empty_address)
            return false
        } else if (TextUtils.isEmpty(binding.etBalance.text.toString())) {
            ToastUtils.showLong(R.string.check_error_empty_quantity)
            return false
        } else if (TextUtils.isEmpty(binding.etPwd.text.toString())) {
            ToastUtils.showLong(R.string.check_error_empty_pwd)
            return false
        }
        return true
    }

    private fun sendCoin() {
        val map: MutableMap<String, String> = HashMap()
        map["symbol"] = symbol
        map["to"] = binding.sampleEditText.text.toString()
        map["value"] = binding.etBalance.text.toString()
        map["address"] = UserManager.getInstance().address
        map["privateKey"] = privateKey
        map["password"] = binding.etPwd.text.toString()
        RetrofitClient
            .remoteSimpleService
            .sendCoin(RequestUtil().getRequestHeader(), map)
            .enqueue(object : Callback<BaseResponse<Any>> {
                override fun onResponse(
                    call: Call<BaseResponse<Any>>,
                    response: Response<BaseResponse<Any>>
                ) {
                    if (!RequestUtil().checkResponse(response.code())) return
                    if (response.body()?.isSuccess == true) {
                        startActivity(
                            Intent(
                                this@TokenSendRequestActivity,
                                TokenSendResultActivity::class.java
                            ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        )
                        finish()
                    }
                }

                override fun onFailure(call: Call<BaseResponse<Any>>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }
}