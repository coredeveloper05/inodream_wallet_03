package io.inodream.wallet.core.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.inodream.wallet.R
import io.inodream.wallet.core.data.NftItemData


class NftRecycleAdapter(val itemList: ArrayList<NftItemData>) : RecyclerView.Adapter<NftRecycleAdapter.NftItemViewHolder>() {

    private lateinit var itemClickListener: OnItemClickEventListener

    enum class EventType {
        VIEW,
        SEND
    }

    interface OnItemClickEventListener {
        fun onItemClick(nft_view: View?, nft_position: Int, nft_event_type: EventType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NftItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_nft_item, parent, false)

        return NftItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: NftItemViewHolder, position: Int) {
        holder.nft_name.text = itemList[position].nftName

        if(itemList.size - 1 == position) holder.item_divider.visibility = View.GONE

        holder.bind()
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    fun setItemClickListener(listener: OnItemClickEventListener) {
        this.itemClickListener = listener
    }

    inner class NftItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nft_name: TextView = itemView.findViewById<TextView>(R.id.nft_name)
        val item_divider: View = itemView.findViewById<View>(R.id.item_divider)

        fun bind() {
            val pos = adapterPosition
            if(pos!= RecyclerView.NO_POSITION) {
                itemView.findViewById<ImageView>(R.id.nft_symbol).setOnClickListener {
                    itemClickListener?.onItemClick(itemView, pos, EventType.VIEW)
                }

                itemView.findViewById<ImageView>(R.id.nft_send_button).setOnClickListener {
                    itemClickListener?.onItemClick(itemView, pos, EventType.SEND)
                }
            }
        }
    }
}