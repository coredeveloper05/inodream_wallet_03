package io.inodream.wallet.activitys

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import io.inodream.wallet.R
import io.inodream.wallet.core.adapters.NftHistoryAdapter
import io.inodream.wallet.databinding.ActivityTokenTransactHistoryBinding
import io.inodream.wallet.refer.retrofit.RetrofitClient
import io.inodream.wallet.refer.retrofit.data.BaseResponse
import io.inodream.wallet.refer.retrofit.data.NFTHistoryData
import io.inodream.wallet.util.NftUtils
import io.inodream.wallet.util.UserManager
import io.inodream.wallet.util.encrypt.RequestUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * <pre>
 *     author : zhen
 *     time   : 2023/08/09
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class NftSendListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTokenTransactHistoryBinding
    private lateinit var adapter: NftHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTokenTransactHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topToolbar.title.text = resources.getString(R.string.title_token_transact_history)

        binding.topToolbar.backButton.setOnClickListener {
            super.onBackPressed()
        }

        initView()
        getList()
    }

    private fun initView() {
        adapter = NftHistoryAdapter()
        binding.recycleview.layoutManager = LinearLayoutManager(this)
        binding.recycleview.adapter = adapter
    }

    private fun getList() {
        val map: MutableMap<String, Any> = HashMap()
        map["walletAddress"] = UserManager.getInstance().walletData.address
        map["nfts"] = NftUtils.getNftList()
        RetrofitClient
            .remoteSimpleService
            .getUserNftHistory(map)
            .enqueue(object : Callback<BaseResponse<List<NFTHistoryData>>> {
                override fun onResponse(
                    call: Call<BaseResponse<List<NFTHistoryData>>>,
                    response: Response<BaseResponse<List<NFTHistoryData>>>
                ) {
                    if (!RequestUtil().checkResponse(response)) return
                    adapter.submitList(response.body()?.data)
                }

                override fun onFailure(
                    call: Call<BaseResponse<List<NFTHistoryData>>>,
                    t: Throwable
                ) {
                    t.printStackTrace()
                    ToastUtils.showLong(t.message)
                }
            })
    }
}