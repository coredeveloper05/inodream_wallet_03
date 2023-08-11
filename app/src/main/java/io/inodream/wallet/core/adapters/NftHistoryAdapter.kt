package io.inodream.wallet.core.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import io.inodream.wallet.R
import io.inodream.wallet.refer.retrofit.data.NFTHistoryData
import io.inodream.wallet.util.StringUtils

/**
 * <pre>
 *     author : zhen
 *     time   : 2023/07/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class NftHistoryAdapter : BaseQuickAdapter<NFTHistoryData, QuickViewHolder>() {

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        // ViewHolder
        return QuickViewHolder(R.layout.view_nft_history, parent)
    }

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: NFTHistoryData?) {
        holder.getView<TextView>(R.id.tv_price).text = StringUtils.getShortAddress(item?.address)
        holder.getView<TextView>(R.id.tv_date).text = item?.timeFormat
        if (item?.type.equals("out", true)) {
            // send
            holder.getView<View>(R.id.ll_icon)
                .setBackgroundResource(R.drawable.circle_tab_select_minus)
            holder.getView<ImageView>(R.id.iv_icon).setImageResource(R.drawable.minus_circle)
            holder.getView<TextView>(R.id.tv_address).text =
                StringUtils.getShortAddress(item?.toAddress)
        } else if (item?.type.equals("in", true)) {
            // receive
            holder.getView<View>(R.id.ll_icon)
                .setBackgroundResource(R.drawable.circle_tab_select_plus)
            holder.getView<ImageView>(R.id.iv_icon).setImageResource(R.drawable.plus_circle)
            holder.getView<TextView>(R.id.tv_address).text =
                StringUtils.getShortAddress(item?.fromAddress)
        }
    }
}