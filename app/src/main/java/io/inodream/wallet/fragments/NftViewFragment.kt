package io.inodream.wallet.fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ClipboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.king.zxing.util.CodeUtils
import io.inodream.wallet.R
import io.inodream.wallet.activitys.NftDetailViewActivity
import io.inodream.wallet.activitys.NftImportActivity
import io.inodream.wallet.activitys.NftSendListActivity
import io.inodream.wallet.activitys.NftSendRequestActivity
import io.inodream.wallet.activitys.STANDARD_721
import io.inodream.wallet.core.adapters.NftRecycleAdapter
import io.inodream.wallet.databinding.FragmentNftViewBinding
import io.inodream.wallet.event.UpdateNftEvent
import io.inodream.wallet.refer.retrofit.RetrofitClient
import io.inodream.wallet.refer.retrofit.data.BaseResponse
import io.inodream.wallet.refer.retrofit.data.NFTData
import io.inodream.wallet.util.NftUtils
import io.inodream.wallet.util.UserManager
import io.inodream.wallet.util.encrypt.RequestUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NftViewFragment : BaseFragment() {

    private var _binding: FragmentNftViewBinding? = null
    private val binding get() = _binding!!
    private lateinit var nftItemAdapter: NftRecycleAdapter
    private val nftDataList: ArrayList<NFTData> = ArrayList()
    private val handler: Handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNftViewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventBus.getDefault().register(this)
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

        binding.tvHistory.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    NftSendListActivity::class.java
                )
            )
        }
        binding.tvRefresh.setOnClickListener { getNFTData() }

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
        qrView.findViewById<TextView>(R.id.tv_wallet_address).text =
            UserManager.getInstance().address
        // generate QRCode
        qrView.findViewById<ImageView>(R.id.iv_wallet_address)
            .setImageBitmap(CodeUtils.createQRCode(UserManager.getInstance().address, 600, null))
        qrView.findViewById<TextView>(R.id.close_button)?.setOnClickListener {
            qrViewDialog.dismiss()
        }
        qrView.findViewById<View>(R.id.copy)
            ?.setOnClickListener { ClipboardUtils.copyText(UserManager.getInstance().address) }
        binding.nftReceiveButton.setOnClickListener {
            qrViewDialog.show()
        }

        getNFTData()
    }

    private fun getNFTData() {
        val map: MutableMap<String, Any> = HashMap()
        map["walletAddress"] = UserManager.getInstance().walletData.address
        map["nfts"] = NftUtils.getNftList()
        RetrofitClient
            .remoteSimpleService
            .getNftList(map)
            .enqueue(object : Callback<BaseResponse<List<NFTData>>> {
                override fun onResponse(
                    call: Call<BaseResponse<List<NFTData>>>,
                    response: Response<BaseResponse<List<NFTData>>>
                ) {
                    if (!RequestUtil().checkResponse(response)) return
                    response.body()?.data?.let {
                        handler.removeCallbacksAndMessages(null)
                        nftDataList.clear()
                        nftItemAdapter.itemList.clear()
                        nftItemAdapter.notifyDataSetChanged()

                        out@ for (data in it) {
                            for (local in nftDataList) {
                                if (data.id == local.id && data.address == local.address) {
                                    continue@out
                                }
                            }
                            if (data.balanceOfUser != "0") {
                                nftDataList.add(data)
                            }
                        }
                        getMetaData()
                    }
                }

                override fun onFailure(call: Call<BaseResponse<List<NFTData>>>, t: Throwable) {
                    t.printStackTrace()
                    ToastUtils.showLong(t.message)
                }
            })
    }

    private fun getMetaData() {
        var url = ""
        var id = ""
        for (i in 0 until nftDataList.size) {
            val data = nftDataList[i]
            if (data.metadata != null) continue
            url = if (data.standard?.contains(STANDARD_721) == true) {
                data.uri ?: ""
            } else {
                (data.uri ?: "").replace("0x{id}", data.id)
            }
            id = data.id
            break
        }
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(id)) return
        RetrofitClient.remoteSimpleService.getData(url).enqueue(object : Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                handler.postDelayed({ getMetaData() }, 1100L)
                if (response.code() != 200) return
                response.body()?.let {
                    for (data in nftDataList) {
                        if (data.id == id) {
                            data.metadata =
                                Gson().fromJson(it, NFTData.MetaData::class.java)
                            nftItemAdapter.itemList.add(data)
                        }
                    }
                    nftItemAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                handler.postDelayed({ getMetaData() }, 1100L)
                t.printStackTrace()
                ToastUtils.showLong(t.message)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateNft(event: UpdateNftEvent) {
        getNFTData()
    }
}