package io.inodream.wallet.activitys

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ClipboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.JsonObject
import io.inodream.wallet.R
import io.inodream.wallet.databinding.ActivityTokenSendRequestBinding
import io.inodream.wallet.refer.retrofit.RetrofitClient
import io.inodream.wallet.refer.retrofit.data.BaseResponse
import io.inodream.wallet.util.UserManager
import io.inodream.wallet.util.encrypt.RequestUtil
import io.inodream.wallet.util.view.GasConfirmBottomDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TokenSendRequestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTokenSendRequestBinding
    private var privateKey: String = ""
    private var symbol: String = ""

    private lateinit var failureViewDialog: AlertDialog
    private lateinit var dialog: GasConfirmBottomDialog

    private enum class TokenSendFailureType {
        TOKEN_DEFICIENCY,
        FEE_DEFICIENCY
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTokenSendRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        symbol = intent.getStringExtra("key") ?: ""
        binding.topToolbar.title.text = resources.getString(R.string.title_token_send)
        binding.sampleEditText.setText(intent.getStringExtra("address") ?: "")
        dialog = GasConfirmBottomDialog(this)

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
                    if (!RequestUtil().checkResponse(response)) return
                    privateKey = response.body()?.get("privateKeyEncode")?.asString ?: ""
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    t.printStackTrace()
                    ToastUtils.showLong(t.message)
                }
            })
    }

    private fun loadDialog(failureType: TokenSendFailureType) {
        val failureView = LayoutInflater.from(this).inflate(R.layout.view_send_token_failure, null)

        val failureTypeText = failureView?.findViewById<TextView>(R.id.token_send_failure_type)
        val faulureMessage = failureView?.findViewById<TextView>(R.id.token_send_failure_message)

        when(failureType) {
            TokenSendFailureType.TOKEN_DEFICIENCY -> {
                failureTypeText?.text = resources.getString(R.string.token_send_failure_type_01)
                faulureMessage?.text = resources.getString(R.string.token_send_failure_type_01_message)
            }
            TokenSendFailureType.FEE_DEFICIENCY -> {
                failureTypeText?.text = resources.getString(R.string.token_send_failure_type_02)
                faulureMessage?.text = resources.getString(R.string.token_send_failure_type_02_message)
            }
        }

        failureViewDialog = AlertDialog.Builder(this)
            .setView(failureView)
            .create()

        failureViewDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        failureView.findViewById<TextView>(R.id.tokenSend4Tv)?.setOnClickListener {
            failureViewDialog.dismiss()
        }

        failureViewDialog.show()
    }

    private fun setListener() {
        binding.topToolbar.backButton.setOnClickListener {
            finish()
        }

        binding.sendRequestButton.setOnClickListener {
            if (checkForm()) {
                estimateTransferGasFee()
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
        binding.tvPaste.setOnClickListener {
            binding.sampleEditText.setText(ClipboardUtils.getText())
        }
        dialog.listener = object : GasConfirmBottomDialog.OnConfirmListener {
            override fun onConfirm() {
                encodeText()
            }
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

    private fun estimateTransferGasFee() {
        val map: MutableMap<String, String> = HashMap()
        map["symbol"] = symbol
        map["to"] = binding.sampleEditText.text.toString()
        map["value"] = binding.etBalance.text.toString()
        map["seedEncode"] = UserManager.getInstance().walletData.seed
        binding.sendRequestButton.startAnimation()
        RetrofitClient
            .remoteSimpleService
            .estimateTransferGasFee(map)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (RequestUtil().checkResponse(response)) {
                        response.body()?.let {
                            if (it.get("status").toString() == "1") {
                                dialog.showGas(it.get("transactionFee").asString)
                            }
                        }
                    }
                    binding.sendRequestButton.revertAnimation()
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    binding.sendRequestButton.revertAnimation()
                    t.printStackTrace()
                    ToastUtils.showLong(t.message)
                }
            })
    }

    private fun encodeText() {
        val map: MutableMap<String, String> = HashMap()
        map["text"] = binding.etPwd.text.toString()
        binding.sendRequestButton.startAnimation()
        RetrofitClient
            .remoteSimpleService
            .encodeText(map)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (RequestUtil().checkResponse(response)) {
                        val text = response.body()?.get("encode")?.asString
                        if (!TextUtils.isEmpty(text)) {
                            sendCoin(text!!)
                            return
                        }
                    }
                    binding.sendRequestButton.revertAnimation()
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    binding.sendRequestButton.revertAnimation()
                    t.printStackTrace()
                    ToastUtils.showLong(t.message)
                }
            })
    }

    private fun sendCoin(pwd: String) {
        val map: MutableMap<String, String> = HashMap()
        map["symbol"] = symbol
        map["to"] = binding.sampleEditText.text.toString()
        map["value"] = binding.etBalance.text.toString()
        map["address"] = UserManager.getInstance().address
        map["privateKey"] = privateKey
        map["password"] = pwd
        RetrofitClient
            .remoteSimpleService
            .sendCoin(RequestUtil().getRequestHeader(), map)
            .enqueue(object : Callback<BaseResponse<Any>> {
                override fun onResponse(
                    call: Call<BaseResponse<Any>>,
                    response: Response<BaseResponse<Any>>
                ) {
                    binding.sendRequestButton.revertAnimation()
                    if (!RequestUtil().checkResponse(response)) return
                    if (response.body()?.status == "1") {
                        startActivity(
                            Intent(
                                this@TokenSendRequestActivity,
                                TokenSendResultActivity::class.java
                            ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        )
                        finish()
                    } else if (response.body()?.status == "2") {// FIXME: modify status
                        loadDialog(TokenSendFailureType.FEE_DEFICIENCY)
                    } else if (response.body()?.status == "3") {
                        loadDialog(TokenSendFailureType.TOKEN_DEFICIENCY)
                    } else {
                        ToastUtils.showLong(response.body()?.message)
                    }
                }

                override fun onFailure(call: Call<BaseResponse<Any>>, t: Throwable) {
                    binding.sendRequestButton.revertAnimation()
                    t.printStackTrace()
                    ToastUtils.showLong(t.message)
                }
            })
    }
}