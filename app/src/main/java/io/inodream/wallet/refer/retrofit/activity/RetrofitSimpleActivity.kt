package io.inodream.wallet.refer.retrofit.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.gson.JsonElement
import io.inodream.wallet.MainActivity
import io.inodream.wallet.R
import io.inodream.wallet.activitys.WalletMainActivity
import io.inodream.wallet.databinding.ActivityRetrofitSimpleBinding
import io.inodream.wallet.refer.retrofit.RetrofitClient
import io.inodream.wallet.refer.retrofit.data.RemoteSimpleData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class RetrofitSimpleActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRetrofitSimpleBinding

    private val TAG = "RetrofitSimpleActivity"

    private lateinit var resultValueBuilder: StringBuilder
    private lateinit var resultValueTokenBuilder: StringBuffer

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRetrofitSimpleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.remoteSimpleDataCallButton.setOnClickListener(this)

        resultValueBuilder = StringBuilder()
        resultValueTokenBuilder = StringBuffer()
    }

    override fun onClick(p0 : View?) {
        when(p0?.id) {
            R.id.remoteSimpleDataCallButton -> {
                callRemoteSimpleData()
            }
        }
    }

    private fun callRemoteSimpleData() {
        RetrofitClient
            .remoteSimpleService
            .getTokenInfos("TestSimpleParam_01")
            .enqueue(object: Callback<RemoteSimpleData> {
                override fun onResponse(
                    call : Call<RemoteSimpleData> , response : Response<RemoteSimpleData>
                ) {
                    Log.d(TAG , "onResponse:::::::::::")

                    binding.printView.text = ""
                    binding.printViewTokenInfos.text = ""
                    response.body()?.let {
                        resultValueBuilder.setLength(0)
                        resultValueTokenBuilder.setLength(0)

                        resultValueBuilder.append("status: ${it.status} \n")
                        resultValueBuilder.append("currency: ${it.data.currency} \n")
                        resultValueBuilder.append("message: ${it.message} \n")
                        resultValueBuilder.append("tokenInfos:")
                        resultValueBuilder.append(it.data.tokenInfos?.let {tpkenInfos ->
                            tpkenInfos.forEach { tokenInfo ->
                                resultValueTokenBuilder.append(":::::::::::::::::::::::::: \n")
                                resultValueTokenBuilder.append("type: ${tokenInfo.idx} \n")
                                resultValueTokenBuilder.append("name: ${tokenInfo.name} \n")
                                resultValueTokenBuilder.append("decimals: ${tokenInfo.decimals} \n")
                                resultValueTokenBuilder.append("contractAddress: ${tokenInfo.contractAddress} \n")
                                resultValueTokenBuilder.append("price: ${tokenInfo.price} \n")
                            }
                        } ?: "Empty Data")

                        binding.printView.text = resultValueBuilder.toString()
                        binding.printViewTokenInfos.text = resultValueTokenBuilder.toString()
                    }
                }

                override fun onFailure(call : Call<RemoteSimpleData> , t : Throwable) {
                    Log.d(TAG , "onFailure:::::::::::")
                }
            })
    }

    override fun onBackPressed() {
        startActivity(Intent(this, WalletMainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK))
    }
}