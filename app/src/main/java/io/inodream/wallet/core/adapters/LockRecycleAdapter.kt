package io.inodream.wallet.core.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.inodream.wallet.R
import io.inodream.wallet.core.data.LockData

class LockRecycleAdapter(private val itemList: MutableList<LockData>): RecyclerView.Adapter<LockRecycleAdapter.Holder>() {

    override fun onCreateViewHolder(parent : ViewGroup , viewType : Int) : Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_unlock_item, parent, false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder : Holder , position : Int) {
        holder.unlockDate.text = itemList[position].unlockDate
        holder.unlockTokenRate.text = itemList[position].unlockTokenRate
        holder.unlockTokenBalance.text = itemList[position].unLockTokenBalance
    }

    override fun getItemCount() : Int {
        return itemList.count()
    }

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val unlockDate: TextView = itemView.findViewById(R.id.unlock_date)
        val unlockTokenRate: TextView = itemView.findViewById(R.id.unlock_token_rate)
        val unlockTokenBalance: TextView = itemView.findViewById(R.id.unlock_token_balance)
    }
}