package io.inodream.wallet.fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.inodream.wallet.R
import io.inodream.wallet.activitys.NftDetailViewActivity
import io.inodream.wallet.activitys.NftImportActivity
import io.inodream.wallet.activitys.NftSendRequestActivity
import io.inodream.wallet.activitys.STANDARD_721
import io.inodream.wallet.core.adapters.NftRecycleAdapter
import io.inodream.wallet.databinding.FragmentNftViewBinding
import io.inodream.wallet.refer.retrofit.RetrofitClient
import io.inodream.wallet.refer.retrofit.data.NFTListData
import io.inodream.wallet.util.UserManager
import io.inodream.wallet.util.encrypt.RequestUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NftViewFragment : BaseFragment() {

    private var _binding: FragmentNftViewBinding? = null
    private val binding get() = _binding!!
    private lateinit var nftItemAdapter: NftRecycleAdapter
    private val nftDataList: ArrayList<NFTListData.NFTData> = ArrayList()
    private val handler: Handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNftViewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private val nftData = arrayListOf(
        NFTListData.NFTData(
            "0xf4910c763ed4e47a585e2d34baa9a4b611ae448c",
            "76039954904669227696257918303144183647734773931256600432704802769631046533220"
        ),
        NFTListData.NFTData(
            "0xf4910c763ed4e47a585e2d34baa9a4b611ae448c",
            "76039954904669227696257918303144183647734773931256600432704802770730558160996"
        )
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nftItemAdapter = NftRecycleAdapter(ArrayList())
        //Key
        nftItemAdapter.setItemClickListener(object : NftRecycleAdapter.OnItemClickEventListener {
            override fun onItemClick(
                nft_view: View?,
                nft_position: Int,
                nft_event_type: NftRecycleAdapter.EventType
            ) {
                startActivity(
                    Intent(
                        requireContext(), when (nft_event_type) {
                            NftRecycleAdapter.EventType.VIEW -> {
                                NftDetailViewActivity::class.java
                            }

                            NftRecycleAdapter.EventType.SEND -> {
                                NftSendRequestActivity::class.java
                            }
                        }
                    ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        .putExtra("key", nftItemAdapter.itemList.get(nft_position))
                )
            }
        })

        binding.nftRecycleview.adapter = nftItemAdapter
        binding.nftRecycleview.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.nftAddCall.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    NftImportActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            )
        }

        val layoutInflater = LayoutInflater.from(context)
        val qrView = layoutInflater.inflate(R.layout.view_token_receive, null)
        val qrViewDialog = AlertDialog.Builder(context)
            .setView(qrView)
            .create()

        qrViewDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        qrView.findViewById<TextView>(R.id.close_button)?.setOnClickListener {
            qrViewDialog.dismiss()
        }

        binding.nftReceiveButton.setOnClickListener {
            qrViewDialog.show()
        }

        getNFTData()
    }

    private fun getNFTData() {
        nftItemAdapter.itemList.clear()
        nftDataList.clear()

        val map: MutableMap<String, Any> = HashMap()
        map["walletAddress"] = UserManager.getInstance().walletData.address
        map["nfts"] = nftData
        RetrofitClient
            .remoteSimpleService
            .getNftList(map)
            .enqueue(object : Callback<NFTListData> {
                override fun onResponse(
                    call: Call<NFTListData>,
                    response: Response<NFTListData>
                ) {
                    if (!RequestUtil().checkResponse(response)) return
                    response.body()?.userNfts?.let {
                        for (data in it) {
                            if (data.balanceOfUser != "0") {
                                nftDataList.add(data)
                            }
                        }
                        for (i in 0 until nftDataList.size) {
                            val data = nftDataList[i]
                            val url: String = if (data.standard?.contains(STANDARD_721) == true) {
                                data.uri ?: ""
                            } else {
                                (data.uri ?: "").replace("0x{id}", data.id)
                            }
                            handler.postDelayed(Runnable {
                                getMetaData(url, data.id)
                            }, i * 2100L)
                        }
                    }
                }

                override fun onFailure(call: Call<NFTListData>, t: Throwable) {
                    t.printStackTrace()
                    ToastUtils.showLong(t.message)
                }
            })
    }

    private fun getMetaData(url: String, id: String) {
        LogUtils.d("time:" + System.currentTimeMillis())
        RetrofitClient.remoteSimpleService.getData(url).enqueue(object : Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                if (!RequestUtil().checkResponse(response)) return
                response.body()?.let {
                    for (data in nftDataList) {
                        if (data.id == id) {
                            data.metadata =
                                Gson().fromJson(it, NFTListData.NFTData.MetaData::class.java)
                            nftItemAdapter.itemList.add(data)
                            nftItemAdapter.notifyItemInserted(nftItemAdapter.itemList.size - 1)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                t.printStackTrace()
                ToastUtils.showLong(t.message)
            }
        })
    }
}