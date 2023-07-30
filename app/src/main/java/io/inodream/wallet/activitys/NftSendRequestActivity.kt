package io.inodream.wallet.activitys

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ClipboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton
import com.google.gson.JsonObject
import io.inodream.wallet.R
import io.inodream.wallet.databinding.ActivityNftSendRequestBinding
import io.inodream.wallet.refer.retrofit.RetrofitClient
import io.inodream.wallet.refer.retrofit.data.NFTListData
import io.inodream.wallet.util.StringUtils
import io.inodream.wallet.util.UserManager
import io.inodream.wallet.util.encrypt.RequestUtil
import io.inodream.wallet.util.view.GasConfirmBottomDialog
import io.inodream.wallet.util.view.GasConfirmBottomDialog.OnConfirmListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val STANDARD_721 = "721"
const val STANDARD_1155 = "1155"

class NftSendRequestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNftSendRequestBinding
    private lateinit var et1: EditText
    private lateinit var et2: EditText
    private lateinit var et3: EditText
    private lateinit var et4: EditText
    private lateinit var btn: CircularProgressButton
    private lateinit var dialog: GasConfirmBottomDialog
    private var conformTip: Boolean = true
    private var nftData: NFTListData.NFTData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNftSendRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nftData = intent.getSerializableExtra("key") as NFTListData.NFTData?
        if (nftData == null) {
            finish()
            return
        }
        initView()
        setListener()

    }

    private fun initView() {
        binding.topToolbar.title.text = resources.getString(R.string.title_nft_send)
        binding.tvAccount.text = getString(R.string.nft_account, UserManager.getInstance().email)
        binding.tvAddress.text =
            getString(R.string.nft_address, StringUtils.getShortAddress(nftData?.address))
        binding.tvId.text = getString(R.string.nft_id, StringUtils.getShortAddress(nftData?.id))
        if (nftData?.standard?.contains(STANDARD_721) == true) {
            binding.llToken.visibility = View.GONE
        }

        dialog = GasConfirmBottomDialog(this)

        et1 = binding.et1
        et2 = binding.et2
        et3 = binding.et3
        et4 = binding.et4
        btn = binding.nftSend02Tv
    }

    private fun setListener() {
        binding.topToolbar.backButton.setOnClickListener { finish() }
        btn.setOnClickListener {
            if (checkForm()) {
                transferNft()
            }
        }
        binding.tvScan1.setOnClickListener { et1.setText(ClipboardUtils.getText()) }
        binding.tvScan2.setOnClickListener { et2.setText(ClipboardUtils.getText()) }
        binding.tvScan3.setOnClickListener { et3.setText(ClipboardUtils.getText()) }
        binding.ivScan1.setOnClickListener {
            startActivityForResult(
                Intent(
                    this@NftSendRequestActivity, CameraCaptureActivity::class.java
                ), 1001
            )
        }
        binding.ivScan2.setOnClickListener {
            startActivityForResult(
                Intent(
                    this@NftSendRequestActivity, CameraCaptureActivity::class.java
                ), 1002
            )
        }
        binding.ivScan3.setOnClickListener {
            startActivityForResult(
                Intent(
                    this@NftSendRequestActivity, CameraCaptureActivity::class.java
                ), 1003
            )
        }
        dialog.listener = object : OnConfirmListener {
            override fun onConfirm() {
                conformTip = false
                transferNft()
            }
        }
    }

    private fun checkForm(): Boolean {
        if (TextUtils.isEmpty(et1.text)) {
            ToastUtils.showLong("수신" + getString(R.string.nft_empty_editview))
            return false
        } else if (nftData?.standard?.contains(STANDARD_721) == false && TextUtils.isEmpty(et2.text)) {
            ToastUtils.showLong("NFT 자산" + getString(R.string.nft_empty_editview))
            return false
        } else if (nftData?.standard?.contains(STANDARD_721) == false && (et2.text.toString()
                .toInt() > (nftData?.balanceOfUser?.toInt() ?: 0))
        ) {
            ToastUtils.showLong("NFT 자산" + getString(R.string.nft_balance))
            return false
//        } else if (TextUtils.isEmpty(et3.text)) {
//            ToastUtils.showLong("토큰 ID" + getString(R.string.nft_empty_editview))
//            return false
//        } else if (TextUtils.isEmpty(et4.text)) {
//            ToastUtils.showLong("비밀번호" + getString(R.string.nft_empty_editview))
//            return false
        }
        return true
    }

    private fun transferNft() {
        val map: MutableMap<String, Any> = HashMap()
        map["seedEncode"] = UserManager.getInstance().walletData.seed
        map["nftAddress"] = nftData?.address ?: ""
        map["nftId"] = nftData?.id ?: ""
        map["toAddress"] = et1.text.toString()
        map["amount"] = et2.text.toString()
        map["estimateGas"] = conformTip
        btn.startAnimation()
        RetrofitClient
            .remoteSimpleService
            .transferNft(map)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    btn.revertAnimation()
                    if (!RequestUtil().checkResponse(response)) {
                        if (!conformTip) conformTip = true
                        return
                    }
                    response.body()?.let {
                        if (it.get("status").toString() == "1") {
                            if (conformTip) {
                                dialog.showGas(it.get("transactionFee").asString)
                            } else {
                                startActivity(
                                    Intent(
                                        this@NftSendRequestActivity,
                                        NftSendResultActivity::class.java
                                    )
                                )
                                finish()
                                return
                            }
                        }
                    }
                    if (conformTip) conformTip = true
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    if (conformTip) conformTip = true
                    btn.revertAnimation()
                    t.printStackTrace()
                    ToastUtils.showLong(t.message)
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val result = data?.getStringExtra("result")
            when (requestCode) {
                1001 -> {
                    et1.setText(result)
                }

                1002 -> {
                    et2.setText(result)
                }

                1003 -> {
                    et3.setText(result)
                }
            }
        }
    }
}