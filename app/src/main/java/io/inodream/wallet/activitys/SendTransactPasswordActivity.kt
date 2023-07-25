package io.inodream.wallet.activitys

import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.JsonObject
import io.inodream.wallet.R
import io.inodream.wallet.databinding.ActivitySendTransactPasswordBinding
import io.inodream.wallet.refer.retrofit.RetrofitClient
import io.inodream.wallet.refer.retrofit.data.BaseResponse
import io.inodream.wallet.util.UserManager
import io.inodream.wallet.util.encrypt.RequestUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SendTransactPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySendTransactPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendTransactPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topToolbar.title.text = resources.getString(R.string.title_token_send)

        setListener()
    }

    private fun setListener() {
        binding.passwordRegisterButton.setOnClickListener {
            val text1 = binding.etPwd1.text.toString()
            val text2 = binding.etPwd2.text.toString()
            if (checkForm(text1, text2)) {
                encodeText(text1)
            }
        }
        binding.topToolbar.backButton.setOnClickListener {
            finish()
        }
    }

    private fun checkForm(text1: String, text2: String): Boolean {
        if (text1.length < 6) {
            ToastUtils.showLong(R.string.check_pwd_length_error)
            binding.etPwd1.requestFocus()
            return false
        } else if (text2.length < 6) {
            ToastUtils.showLong(R.string.check_pwd_length_error)
            binding.etPwd2.requestFocus()
            return false
        } else if (text1 != text2) {
            ToastUtils.showLong(R.string.check_pwd_inconsistency_error)
            binding.etPwd1.requestFocus()
            return false
        }
        return true
    }

    private fun encodeText(text1: String) {
        val map: MutableMap<String, String> = HashMap()
        map["text"] = text1
        RetrofitClient
            .remoteSimpleService
            .encodeText(map)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (!RequestUtil().checkResponse(response)) return
                    val text = response.body()?.get("encode")?.asString
                    if (!TextUtils.isEmpty(text)) {
                        setPwd(text!!)
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    t.printStackTrace()
                    ToastUtils.showLong(t.message)
                }
            })
    }

    private fun setPwd(text: String) {
        val map: MutableMap<String, String> = HashMap()
        map["password"] = text
        RetrofitClient
            .remoteSimpleService
            .withDrawPwd(RequestUtil().getRequestHeader(), map)
            .enqueue(object : Callback<BaseResponse<JsonObject>> {
                override fun onResponse(
                    call: Call<BaseResponse<JsonObject>>,
                    response: Response<BaseResponse<JsonObject>>
                ) {
                    if (!RequestUtil().checkResponse(response)) return
                    if (response.body()?.data?.get("result")?.asString == "true") {
                        ToastUtils.showLong(R.string.success_set_pwd)
                        UserManager.getInstance().setPwd(true)
                        finish()
                    }
                }

                override fun onFailure(call: Call<BaseResponse<JsonObject>>, t: Throwable) {
                    t.printStackTrace()
                    ToastUtils.showLong(t.message)
                }
            })
    }
}