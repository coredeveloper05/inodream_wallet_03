package io.inodream.wallet.fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ClipboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.king.zxing.util.CodeUtils
import io.inodream.wallet.App
import io.inodream.wallet.R
import io.inodream.wallet.activitys.TokenSendSelectionActivity
import io.inodream.wallet.activitys.TokenTransactHistoryActivity
import io.inodream.wallet.databinding.FragmentTokenViewBinding
import io.inodream.wallet.event.RefreshEvent
import io.inodream.wallet.event.UpdateTokenEvent
import io.inodream.wallet.refer.retrofit.RetrofitClient
import io.inodream.wallet.refer.retrofit.data.BalanceData
import io.inodream.wallet.refer.retrofit.data.BalancesData
import io.inodream.wallet.refer.retrofit.data.BaseResponse
import io.inodream.wallet.refer.retrofit.data.GoogleAuthData
import io.inodream.wallet.refer.retrofit.data.TokenInfoData
import io.inodream.wallet.refer.retrofit.data.TokenInfosData
import io.inodream.wallet.util.UserManager
import io.inodream.wallet.util.encrypt.RequestUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TokenViewFragment : Fragment() {

    private var _binding: FragmentTokenViewBinding? = null
    private val binding get() = _binding!!
    private var qrViewDialog: AlertDialog? = null
    private var mTokenInfos: TokenInfosData? = null
    private var mBalancesData: BalancesData? = null
    private var mTotalPrice: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTokenViewBinding.inflate(layoutInflater, container, false)
        EventBus.getDefault().register(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDialog()

        getUserWallet()
        getData()

        setListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    private fun initDialog() {
        val qrView = LayoutInflater.from(context).inflate(R.layout.view_token_receive, null)
        qrView.findViewById<TextView>(R.id.tv_wallet_address).text =
            UserManager.getInstance().address
        // 生成二维码
        qrView.findViewById<ImageView>(R.id.iv_wallet_address)
            .setImageBitmap(CodeUtils.createQRCode(UserManager.getInstance().address, 600, null))
        qrViewDialog = AlertDialog.Builder(context)
            .setView(qrView)
            .create()

        qrViewDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        qrView.findViewById<TextView>(R.id.close_button)?.setOnClickListener {
            qrViewDialog?.dismiss()
        }
        qrView.findViewById<View>(R.id.copy)?.setOnClickListener {
            ClipboardUtils.copyText(UserManager.getInstance().address)
            ToastUtils.showLong("클립보드에 복사됨.")
        }
    }

    private fun setListener() {
        binding.mainButton01.setOnClickListener {
            qrViewDialog?.show()
        }

        binding.mainButton02.setOnClickListener {
            val intent =
                Intent(requireContext(), TokenSendSelectionActivity::class.java).addFlags(
                    Intent.FLAG_ACTIVITY_NO_ANIMATION
                )
            intent.putExtra("key", mTokenInfos?.tokenInfos)
            startActivity(intent)
        }

        binding.mainButton03.setOnClickListener {
            requireActivity().findViewById<LinearLayout>(R.id.swapTabLl).performClick()
        }

        binding.mainButton04.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    TokenTransactHistoryActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            )
        }

        /*
        토큰 거래이력
         */
        binding.transactHistoryButton.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    TokenTransactHistoryActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            )
        }

    }

    private fun getUserWallet() {
        RetrofitClient
            .remoteSimpleService
            .getUserWallet(RequestUtil().getRequestHeader())
            .enqueue(object : Callback<BaseResponse<GoogleAuthData.WalletData>> {
                override fun onResponse(
                    call: Call<BaseResponse<GoogleAuthData.WalletData>>,
                    response: Response<BaseResponse<GoogleAuthData.WalletData>>
                ) {
                    if (!RequestUtil().checkResponse(response)) return
                    response.body()?.data?.let {
                        Log.e("auth", Gson().toJson(it))
                        UserManager.getInstance().setWallet(it)
                    }
                }

                override fun onFailure(
                    call: Call<BaseResponse<GoogleAuthData.WalletData>>,
                    t: Throwable
                ) {
                    t.printStackTrace()
                    ToastUtils.showLong(t.message)
                }
            })
    }

    private fun getData() {
        showDefaultView()
        getBalanceAll()
//        getBalance()
        getTokenInfos()
//        getTokenInfo()
    }

    private fun getBalanceAll() {
        RetrofitClient
            .remoteSimpleService
            .getBalanceAll(RequestUtil().getRequestHeader())
            .enqueue(object : Callback<BaseResponse<BalancesData>> {
                override fun onResponse(
                    call: Call<BaseResponse<BalancesData>>,
                    response: Response<BaseResponse<BalancesData>>
                ) {
                    if (!RequestUtil().checkResponse(response)) return
                    response.body()?.data?.let {
                        Log.e("auth", Gson().toJson(it))
                        mBalancesData = it
                        updateDataView()
                    }
                }

                override fun onFailure(call: Call<BaseResponse<BalancesData>>, t: Throwable) {
                    t.printStackTrace()
                    ToastUtils.showLong(t.message)
                }
            })
    }

    private fun getTokenInfos() {
        RetrofitClient
            .remoteSimpleService
            .getTokenInfos(RequestUtil().getRequestHeader())
            .enqueue(object : Callback<BaseResponse<TokenInfosData>> {
                override fun onResponse(
                    call: Call<BaseResponse<TokenInfosData>>,
                    response: Response<BaseResponse<TokenInfosData>>
                ) {
                    if (!RequestUtil().checkResponse(response)) return
                    response.body()?.data?.let {
                        Log.e("auth", Gson().toJson(it))
                        mTokenInfos = it
                        App.getInstance().tokenInfosData = it
                        EventBus.getDefault().post(UpdateTokenEvent())
                        updateDataView()
                    }
                }

                override fun onFailure(call: Call<BaseResponse<TokenInfosData>>, t: Throwable) {
                    t.printStackTrace()
                    ToastUtils.showLong(t.message)
                }
            })
    }

    private fun showDefaultView() {
        mTokenInfos = null
        mBalancesData = null
        mTotalPrice = 0.0
//        binding.tokenValueKo.text = String.format(getString(R.string.W_value), "0.0")
//        binding.tokenValueKo02.text = String.format(getString(R.string.W_value), "0.0")
//        binding.tokenValueKo03.text = String.format(getString(R.string.W_value), "0.0")
//        binding.tokenValue.text = "0.0ETH"
//        binding.tokenValue02.text = "0.0USDT"
//        binding.tokenValue03.text = "0.0FON"
    }

    private fun updateDataView() {
        mBalancesData?.balances?.forEach {
            when (it.symbol) {
                "ETH" -> {
                    binding.tokenValue.text = it.balance + "ETH"
                    analysisData("ETH", it.balance, binding.tokenSymbol, binding.tokenValueKo)
                }

                "USDT" -> {
                    binding.tokenValue02.text = it.balance + "USDT"
                    analysisData("USDT", it.balance, binding.tokenSymbol02, binding.tokenValueKo02)
                }

                "FON" -> {
                    binding.tokenValue03.text = it.balance + "FON"
                    analysisData("FON", it.balance, binding.tokenSymbol03, binding.tokenValueKo03)
                }
            }
        }
    }

    private fun analysisData(
        symbol: String,
        balance: String,
        tokenSymbol: ImageView,
        tokenValueKo: TextView
    ) {
        mTokenInfos?.tokenInfos?.forEach { token ->
            when (token.symbol) {
                symbol -> {
                    token.balance = balance
                    Glide.with(this).load(token.icon).into(tokenSymbol)
                    var price = 0.0
                    var bal = 0.0
                    try {
                        price = token.price.toDouble()
                        bal = balance.toDouble()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    val totalPrice = price * bal
                    mTotalPrice += totalPrice
                    tokenValueKo.text = String.format(getString(R.string.W_value), totalPrice)
                    binding.myAssetsValue.text =
                        String.format(getString(R.string.W_value), mTotalPrice)
                }
            }
        }
    }

    private fun getBalance() {
        val body: MutableMap<String, String> = HashMap()
        body["symbol"] = "FON"
        RetrofitClient
            .remoteSimpleService
            .getBalance(RequestUtil().getRequestHeader(), body)
            .enqueue(object : Callback<BaseResponse<BalanceData>> {
                override fun onResponse(
                    call: Call<BaseResponse<BalanceData>>,
                    response: Response<BaseResponse<BalanceData>>
                ) {
                    if (!RequestUtil().checkResponse(response)) return
                    response.body()?.let { baseResponse ->
                        baseResponse.data?.let {
                            Log.e("auth", Gson().toJson(it))
                        }
                    }
                }

                override fun onFailure(call: Call<BaseResponse<BalanceData>>, t: Throwable) {
                    t.printStackTrace()
                    ToastUtils.showLong(t.message)
                }
            })
    }

    private fun getTokenInfo() {
        val body: MutableMap<String, String> = HashMap()
        body["symbol"] = "ETH"
        RetrofitClient
            .remoteSimpleService
            .getTokenInfo(RequestUtil().getRequestHeader(), body)
            .enqueue(object : Callback<BaseResponse<TokenInfoData>> {
                override fun onResponse(
                    call: Call<BaseResponse<TokenInfoData>>,
                    response: Response<BaseResponse<TokenInfoData>>
                ) {
                    if (!RequestUtil().checkResponse(response)) return
                    response.body()?.let { baseResponse ->
                        baseResponse.data?.let {
                            Log.e("auth", Gson().toJson(it))
                        }
                    }
                }

                override fun onFailure(call: Call<BaseResponse<TokenInfoData>>, t: Throwable) {
                    t.printStackTrace()
                    ToastUtils.showLong(t.message)
                }
            })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshToken(event: RefreshEvent?) {
        getData()
    }

}