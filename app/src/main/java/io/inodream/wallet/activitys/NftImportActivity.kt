package io.inodream.wallet.activitys

import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ToastUtils
import io.inodream.wallet.R
import io.inodream.wallet.databinding.ActivityNftImportBinding
import io.inodream.wallet.refer.retrofit.RetrofitClient
import io.inodream.wallet.refer.retrofit.data.BaseResponse
import io.inodream.wallet.util.NftUtils
import io.inodream.wallet.util.UserManager
import io.inodream.wallet.util.encrypt.RequestUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NftImportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNftImportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNftImportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topToolbar.title.text = resources.getString(R.string.title_nft_import)

        setListener()
    }

    private fun setListener() {
        binding.topToolbar.backButton.setOnClickListener { finish() }
        binding.importCancelButton.setOnClickListener { finish() }
        binding.tvConfirm.setOnClickListener {
            val address = binding.etAddress.text.toString()
            val id = binding.etId.text.toString()
            if (TextUtils.isEmpty(address)) {
                ToastUtils.showLong("컨트랙트 주소 " + getString(R.string.nft_empty_editview))
            } else if (TextUtils.isEmpty(id)) {
                ToastUtils.showLong("토큰 ID " + getString(R.string.nft_empty_editview))
            } else {
                checkData(address, id)
            }
        }
    }

    private fun checkData(address: String, id: String) {
        val map: MutableMap<String, Any> = HashMap()
        map["nftAddress"] = address
        map["nftId"] = id
        map["walletAddress"] = UserManager.getInstance().address
        RetrofitClient
            .remoteSimpleService
            .validateNft(map)
            .enqueue(object : Callback<BaseResponse<Any>> {
                override fun onResponse(
                    call: Call<BaseResponse<Any>>,
                    response: Response<BaseResponse<Any>>
                ) {
                    if (RequestUtil().checkResponse(response)) {
                        ToastUtils.showLong("가져오기 성공")
                        NftUtils.saveNftData(address, id)
                        runOnUiThread { finish() }
                    }
                }

                override fun onFailure(call: Call<BaseResponse<Any>>, t: Throwable) {
                    t.printStackTrace()
                    ToastUtils.showLong(t.message)
                }
            })
    }
}