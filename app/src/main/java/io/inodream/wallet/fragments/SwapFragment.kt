package io.inodream.wallet.fragments

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonObject
import io.inodream.wallet.App
import io.inodream.wallet.R
import io.inodream.wallet.core.adapters.TopToolBarAdapter
import io.inodream.wallet.databinding.FragmentSwapBinding
import io.inodream.wallet.event.UpdateTokenEvent
import io.inodream.wallet.refer.retrofit.RetrofitClient
import io.inodream.wallet.refer.retrofit.data.TokenQuoteData
import io.inodream.wallet.util.UserManager
import io.inodream.wallet.util.encrypt.RequestUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SwapFragment : BaseFragment() {

    private var _binding: FragmentSwapBinding? = null
    private val binding get() = _binding!!

    private lateinit var swapSendToken: LinearLayout
    private lateinit var swapReceiveToken: LinearLayout
    private lateinit var swapSlippage: LinearLayout
    private lateinit var swapBottomSheet: BottomSheetDialog
    private lateinit var slippageView: View
    private lateinit var slippageSheet: BottomSheetDialog
    private lateinit var slippapgeSb: SeekBar

    // swapBottomSheetView
    private lateinit var swapBottomSheetView: View

    //
    private var inSymbol: String = ""
    private var outSymbol: String = ""
    private var sheetType = 1
    private var tokenMap: MutableMap<String, String> = HashMap()
    private var lastRequestTime = 0L

    private var selectedSlippage = 1
    private var scopeSlippageLaArray: Array<TextView>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSwapBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        EventBus.getDefault().register(this)
        TopToolBarAdapter(view, this).initTopToolBarEvent()

        initView()
        initDialog()
        setListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

    private fun initView() {
        binding.topToolbar.accountName.text = UserManager.getInstance().email
        swapSendToken = binding.swapSendToken
        swapReceiveToken = binding.swapReceiveToken
        swapSlippage = binding.swapSlippage
    }

    private fun initDialog() {
        val inflater = LayoutInflater.from(requireContext()) as LayoutInflater
        swapBottomSheetView = inflater.inflate(R.layout.swap_bottom_sheet, null)
        swapBottomSheet = BottomSheetDialog(requireContext())
        swapBottomSheet.setContentView(swapBottomSheetView)
        updateSwapDialogView()

        slippageView = inflater.inflate(R.layout.swap_bottom_sheet_slippage, null)
        slippageSheet = BottomSheetDialog(requireContext())
        slippageSheet.setContentView(slippageView)
        slippapgeSb = slippageView.findViewById<View>(R.id.slipapgeSb) as SeekBar
        slippapgeSb.progress = 2

        val slippageLa1 = slippageView.findViewById<View>(R.id.slippageLa1) as TextView
        val slippageLa2 = slippageView.findViewById<View>(R.id.slippageLa2) as TextView
        val slippageLa3 = slippageView.findViewById<View>(R.id.slippageLa3) as TextView
        val slippageLa4 = slippageView.findViewById<View>(R.id.slippageLa4) as TextView
        scopeSlippageLaArray = arrayOf(slippageLa1, slippageLa2, slippageLa3, slippageLa4)
    }

    private fun updateSwapDialogView() {
        App.getInstance().tokenInfosData?.tokenInfos?.forEach {
            when (it.symbol) {
                "ETH" -> {
                    swapBottomSheetView.findViewById<ImageView>(R.id.iv_swap_01)?.let { iv ->
                        Glide.with(this).load(it.icon).into(iv)
                    }
                    swapBottomSheetView.findViewById<TextView>(R.id.tv_swap_value_01)?.text =
                        it.balance + "ETH"
                    tokenMap["ETH"] = it.balance ?: ""
                }

                "USDT" -> {
                    swapBottomSheetView.findViewById<ImageView>(R.id.iv_swap_02)?.let { iv ->
                        Glide.with(this).load(it.icon).into(iv)
                    }
                    swapBottomSheetView.findViewById<TextView>(R.id.tv_swap_value_02)?.text =
                        it.balance + "USDT"
                    tokenMap["USDT"] = it.balance ?: ""
                }

                "FON" -> {
                    swapBottomSheetView.findViewById<ImageView>(R.id.iv_swap_03)?.let { iv ->
                        Glide.with(this).load(it.icon).into(iv)
                    }
                    swapBottomSheetView.findViewById<TextView>(R.id.tv_swap_value_03)?.text =
                        it.balance + "FON"
                    tokenMap["FON"] = it.balance ?: ""
                }
            }
        }
    }

    private fun setListener() {
        binding.swapReload.setOnClickListener {
            binding.etSwap01.setText("0")
            binding.tvSwap.text = "0"
        }
        binding.swaptxt01.setOnClickListener {
            if (TextUtils.isEmpty(inSymbol) || TextUtils.isEmpty(outSymbol)) {
                ToastUtils.showLong(R.string.check_error_empty_symbol)
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(binding.etSwap01.text.toString())
                || binding.etSwap01.text.toString() == "."
                || binding.etSwap01.text.toString().toDouble() == 0.0
            ) {
                ToastUtils.showLong(R.string.check_error_empty_amount)
                return@setOnClickListener
            }
            if (inSymbol == "ETH" && (tokenMap[inSymbol]?.toDoubleOrNull() ?: 0.0) < 0.005) {
                ToastUtils.showLong(R.string.check_error_amount_few)
                return@setOnClickListener
            }
            val maxNum = tokenMap[inSymbol]?.toDoubleOrNull() ?: 0.0
            val currentNum = binding.etSwap01.text.toString().toDouble()
            if (currentNum > maxNum) {
                ToastUtils.showLong(R.string.check_error_amount)
                return@setOnClickListener
            }
            swapChange()
        }
        binding.ivChange.setOnClickListener {
            if (TextUtils.isEmpty(inSymbol) || TextUtils.isEmpty(outSymbol)) {
                return@setOnClickListener
            }
            binding.tvSwapToken01.text = outSymbol
            binding.tvSwapToken02.text = inSymbol
            inSymbol = outSymbol
            outSymbol = binding.tvSwapToken02.text.toString()
            val temp = binding.etSwap01.text.toString()
            binding.etSwap01.setText(binding.tvSwap.text)
            binding.tvSwap.text = temp
        }
        binding.tvMax.setOnClickListener {
            tokenMap[inSymbol]?.let { binding.etSwap01.setText(tokenMap[inSymbol]) }
        }

        swapBottomSheetView.findViewById<View>(R.id.ll_swap_sheet_01)
            .setOnClickListener { setSymbol("ETH") }
        swapBottomSheetView.findViewById<View>(R.id.ll_swap_sheet_02)
            .setOnClickListener { setSymbol("USDT") }
        swapBottomSheetView.findViewById<View>(R.id.ll_swap_sheet_03)
            .setOnClickListener { setSymbol("FON") }
        swapSendToken.setOnClickListener {
            sheetType = 1
            swapBottomSheet.show()
        }
        swapReceiveToken.setOnClickListener {
            sheetType = 2
            swapBottomSheet.show()
        }
        slippageView.findViewById<View>(R.id.btn_save).setOnClickListener {
            val num =
                slippageView.findViewById<EditText>(R.id.et_page).text.toString().toDoubleOrNull()
                    ?: 0.0
            if (num in 0.05..50.0) {
                binding.currentSlippage.text = "${num}%"
                slippageSheet.dismiss()
            } else {
                ToastUtils.showLong("값은 0.05-50 사이여야 합니다.")
            }
        }
        slippageView.findViewById<View>(R.id.btn_change).setOnClickListener {
            if (slippageView.findViewById<View>(R.id.ll_et).visibility == View.VISIBLE) {
                slippageView.findViewById<View>(R.id.ll_et).visibility = View.GONE
                slippageView.findViewById<View>(R.id.slipapgeSb).visibility = View.VISIBLE
                slippageView.findViewById<View>(R.id.ll_slip).visibility = View.VISIBLE
            } else {
                slippageView.findViewById<View>(R.id.ll_et).visibility = View.VISIBLE
                slippageView.findViewById<View>(R.id.slipapgeSb).visibility = View.GONE
                slippageView.findViewById<View>(R.id.ll_slip).visibility = View.GONE
            }
        }
        swapSlippage.setOnClickListener {
            val dou = doubleArrayOf(1.0, 2.0, 3.0, 4.0)
            val current = binding.currentSlippage.text.toString().replace("%", "").toDouble()
            if (dou.any { current == it }) {
                slippageView.findViewById<View>(R.id.ll_et).visibility = View.GONE
                slippageView.findViewById<View>(R.id.slipapgeSb).visibility = View.VISIBLE
                slippageView.findViewById<View>(R.id.ll_slip).visibility = View.VISIBLE
                setSlippageLabel(current.toInt())
                slippapgeSb.progress = current.toInt()
            } else {
                slippageView.findViewById<View>(R.id.ll_et).visibility = View.VISIBLE
                slippageView.findViewById<EditText>(R.id.et_page).setText(current.toString())
                slippageView.findViewById<View>(R.id.slipapgeSb).visibility = View.GONE
                slippageView.findViewById<View>(R.id.ll_slip).visibility = View.GONE
            }
            slippageSheet.show()
        }
        slippapgeSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                setSlippageLabel(p1)
                binding.currentSlippage.text = "${p1}.0%"
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
        binding.etSwap01.addTextChangedListener {
            if (TextUtils.isEmpty(inSymbol) || TextUtils.isEmpty(outSymbol)) {
                ToastUtils.showLong(R.string.check_error_empty_symbol)
                return@addTextChangedListener
            } else if (!TextUtils.isEmpty(binding.etSwap01.text.toString())
                && binding.etSwap01.text.toString() != "."
            ) {
                lastRequestTime = System.currentTimeMillis()
                quoteToken()
            }
        }
    }

    private fun setSymbol(symbol: String) {
        swapBottomSheet.dismiss()
        if (sheetType == 1) {
            inSymbol = symbol
            binding.tvSwapToken01.text = symbol
        } else {
            outSymbol = symbol
            binding.tvSwapToken02.text = symbol
        }
    }

    fun setSlippageLabel(select: Int) {
        if (selectedSlippage == select) {
            return
        }
        scopeSlippageLaArray?.let {
            for ((index, item) in scopeSlippageLaArray!!.withIndex()) {
                when (select) {
                    index + 1 -> {
                        item.setBackgroundResource(R.drawable.slippage_label)
                    }

                    else -> {
                        item.setBackgroundColor(Color.WHITE)
                    }
                }
            }
        }

        selectedSlippage = select
    }

    fun quoteToken() {
        val map: MutableMap<String, String> = HashMap()
        map["tokenInSymbol"] = inSymbol
        map["tokenOutSymbol"] = outSymbol
        map["tokenInAmount"] = binding.etSwap01.text.toString()
        map["quoteType"] = "forward"
//        showLoadingDialog()
        binding.swaptxt01.startAnimation()
        RetrofitClient
            .remoteSimpleService
            .quoteToken(map)
            .enqueue(object : Callback<TokenQuoteData> {
                override fun onResponse(
                    call: Call<TokenQuoteData>,
                    response: Response<TokenQuoteData>
                ) {
                    binding.swaptxt01.revertAnimation()
//                    dismissLoadingDialog()
                    if (!RequestUtil().checkResponse(response)) return
                    response.body()?.let {
                        binding.tvSwap.text = it.tokenOutAmount
                    }
                }

                override fun onFailure(call: Call<TokenQuoteData>, t: Throwable) {
//                    dismissLoadingDialog()
                    t.printStackTrace()
                    ToastUtils.showLong(t.message)
                }
            })
    }

    fun swapChange() {
        val map: MutableMap<String, String> = HashMap()
        map["seedEncode"] = UserManager.getInstance().walletData.seed
        map["tokenInSymbol"] = inSymbol
        map["tokenOutSymbol"] = outSymbol
        map["tokenInAmount"] = binding.etSwap01.text.toString()
        map["tokenOutAmount"] = binding.tvSwap.text.toString()
        map["slippage"] = binding.currentSlippage.text.toString().replace("%", "")
        binding.swaptxt01.startAnimation()
        RetrofitClient
            .remoteSimpleService
            .swapChange(map)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    binding.swaptxt01.revertAnimation()
                    if (!RequestUtil().checkResponse(response)) return
                    if (response.body()?.get("status").toString() == "1") {
                        ToastUtils.showLong("swap성공하다")
                    } else {
                        ToastUtils.showLong("swap실패하다")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    binding.swaptxt01.revertAnimation()
                    t.printStackTrace()
                    ToastUtils.showLong(t.message)
                }
            })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateToken(event: UpdateTokenEvent) {
        updateSwapDialogView()
    }
}