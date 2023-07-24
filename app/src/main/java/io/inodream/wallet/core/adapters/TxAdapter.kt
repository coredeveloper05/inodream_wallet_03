package io.inodream.wallet.core.adapters

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import io.inodream.wallet.R
import io.inodream.wallet.refer.retrofit.data.TxData
import io.inodream.wallet.util.UserManager

/**
 * <pre>
 *     author : zhen
 *     time   : 2023/07/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class TxAdapter : BaseQuickAdapter<TxData.Data, QuickViewHolder>() {

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        // ViewHolder
        return QuickViewHolder(R.layout.view_tx, parent)
    }

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: TxData.Data?) {
        val state: String = when (item?.status) {
            "0" -> context.getString(R.string.tx_create)
            "1" -> context.getString(R.string.tx_progress)
            "2" -> context.getString(R.string.tx_confirm)
            else -> context.getString(R.string.tx_complete)
        }
        if (item?.from == UserManager.getInstance().address) {
            // send
            holder.getView<View>(R.id.ll_icon)
                .setBackgroundResource(R.drawable.circle_tab_select_minus)
            holder.getView<ImageView>(R.id.iv_icon).setImageResource(R.drawable.minus_circle)
            holder.getView<TextView>(R.id.tv_address).text = getShortAddress(item?.to ?: "")
            holder.getView<TextView>(R.id.tv_price).text = item?.value + " " + item?.symbol
            holder.getView<TextView>(R.id.tv_date).text = item?.updatedAt
            holder.getView<View>(R.id.ll_state).setBackgroundResource(R.drawable.swap_squ_minus)
//            holder.getView<ImageView>(R.id.iv_state).setImageResource(R.drawable.check_circle_3)
            holder.getView<TextView>(R.id.tv_state).setTextColor(Color.parseColor("#7961ef"))
            holder.getView<TextView>(R.id.tv_state).text =
                context.getString(R.string.tx_send) + state
        } else if (item?.to == UserManager.getInstance().address) {
            // receive
            holder.getView<View>(R.id.ll_icon)
                .setBackgroundResource(R.drawable.circle_tab_select_plus)
            holder.getView<ImageView>(R.id.iv_icon).setImageResource(R.drawable.plus_circle)
            holder.getView<TextView>(R.id.tv_address).text = getShortAddress(item?.from ?: "")
            holder.getView<TextView>(R.id.tv_price).text = item?.value + " " + item?.symbol
            holder.getView<TextView>(R.id.tv_date).text = item?.updatedAt
            holder.getView<View>(R.id.ll_state).setBackgroundResource(R.drawable.swap_squ_orange)
//            holder.getView<ImageView>(R.id.iv_state).setImageResource(R.drawable.check_circle)
            holder.getView<TextView>(R.id.tv_state).setTextColor(Color.parseColor("#ff9c21"))
            holder.getView<TextView>(R.id.tv_state).text =
                context.getString(R.string.tx_receive) + state
        }
    }

    private fun getShortAddress(address: String): String {
        if (address.length < 17) {
            return address
        }
        return address.substring(0, 7) + "..." + address.substring(address.length - 10)
    }
}