package io.inodream.wallet.util.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.inodream.wallet.App
import io.inodream.wallet.R
import io.inodream.wallet.util.StringUtils

/**
 * <pre>
 *     author : zhen
 *     time   : 2023/07/29
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class GasConfirmBottomDialog(context: Context) : BottomSheetDialog(context) {

    private lateinit var tvPrice: TextView
    var listener: OnConfirmListener? = null

    init {
        setCancelable(false)
        val inflater = LayoutInflater.from(context) as LayoutInflater
        val view = inflater.inflate(R.layout.dialog_token_bottom, null)
        setContentView(view)
        tvPrice = view.findViewById(R.id.tv_price)
        view.findViewById<View>(R.id.btn_cancel).setOnClickListener { dismiss() }
        view.findViewById<View>(R.id.btn_confirm).setOnClickListener {
            listener?.onConfirm()
            dismiss()
        }
    }

    fun showGas(amount: String) {
        var allPrice: String = ""
        App.getInstance().tokenInfosData.tokenInfos?.forEach { token ->
            when (token.symbol) {
                "ETH" -> {
                    var price = 0.0
                    var bal = 0.0
                    try {
                        price = token.price.toDouble()
                        bal = amount.toDouble()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    allPrice = StringUtils.getSixDigits(price * bal)
                }
            }
        }
        tvPrice.text = context.getString(R.string.dialog_token_bottom_price, amount, allPrice)
        show()
    }

    interface OnConfirmListener {
        fun onConfirm()
    }
}