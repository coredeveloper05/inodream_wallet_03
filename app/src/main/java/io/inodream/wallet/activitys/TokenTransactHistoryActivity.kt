package io.inodream.wallet.activitys

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import io.inodream.wallet.R
import io.inodream.wallet.core.adapters.TxAdapter
import io.inodream.wallet.databinding.ActivityTokenTransactHistoryBinding
import io.inodream.wallet.refer.retrofit.RetrofitClient
import io.inodream.wallet.refer.retrofit.data.BaseResponse
import io.inodream.wallet.refer.retrofit.data.TxData
import io.inodream.wallet.util.UserManager
import io.inodream.wallet.util.encrypt.RequestUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TokenTransactHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTokenTransactHistoryBinding
    private lateinit var adapter: TxAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTokenTransactHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topToolbar.title.text = resources.getString(R.string.title_token_transact_history)

        binding.topToolbar.backButton.setOnClickListener {
            super.onBackPressed()
        }

        initView()
        getTxList()
    }

    private fun initView() {
        adapter = TxAdapter()
        binding.recycleview.layoutManager = LinearLayoutManager(this)
        binding.recycleview.adapter = adapter
        adapter.setOnItemClickListener { adapter, view, position ->
            val intent = Intent(this, TokenTransactHistoryDetailActivity::class.java)
            intent.putExtra("key", adapter.getItem(position))
            startActivity(intent)
        }
    }

    private fun getTxList() {
        val map: MutableMap<String, String> = HashMap()
        map["address"] = UserManager.getInstance().address
        RetrofitClient
            .remoteSimpleService
            .listTX(RequestUtil().getRequestHeader(), map)
            .enqueue(object : Callback<BaseResponse<List<TxData.Data>>> {
                override fun onResponse(
                    call: Call<BaseResponse<List<TxData.Data>>>,
                    response: Response<BaseResponse<List<TxData.Data>>>
                ) {
                    if (!RequestUtil().checkResponse(response)) return
                    adapter.submitList(response.body()?.data?.reversed())
                }

                override fun onFailure(call: Call<BaseResponse<List<TxData.Data>>>, t: Throwable) {
                    t.printStackTrace()
                    ToastUtils.showLong(t.message)
                }
            })
    }
}