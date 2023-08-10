package io.inodream.wallet.core.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.inodream.wallet.R
import io.inodream.wallet.refer.retrofit.data.NFTData
import io.inodream.wallet.util.StringUtils


class NftRecycleAdapter(var itemList: ArrayList<NFTData>) :
    RecyclerView.Adapter<NftRecycleAdapter.NftItemViewHolder>() {

    private lateinit var itemClickListener: OnItemClickEventListener
    private lateinit var context: Context

    enum class EventType {
        VIEW,
        SEND
    }

    interface OnItemClickEventListener {
        fun onItemClick(nft_view: View?, nft_position: Int, nft_event_type: EventType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NftItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.view_nft_item, parent, false)
        context = parent.context
        return NftItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: NftItemViewHolder, position: Int) {
        holder.nft_name.text =
            StringUtils.getShortAddressAndId(itemList[position].address, itemList[position].id)
        holder.nft_creator_name.text = StringUtils.getShortAddress(itemList[position].creator)
        Glide.with(context).load(itemList[position].metadata?.image ?: "")
            .placeholder(R.drawable.ic_nft_symbol_01).into(holder.nft_symbol)

        if (itemList.size - 1 == position) holder.item_divider.visibility = View.GONE

        holder.bind()
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    fun setItemClickListener(listener: OnItemClickEventListener) {
        this.itemClickListener = listener
    }

    inner class NftItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nft_symbol: ImageView = itemView.findViewById(R.id.nft_symbol)
        val nft_name: TextView = itemView.findViewById(R.id.nft_name)
        val nft_creator_name: TextView = itemView.findViewById(R.id.nft_creator_name)
        val item_divider: View = itemView.findViewById(R.id.item_divider)

        fun bind() {
            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                itemView.findViewById<ImageView>(R.id.nft_symbol).setOnClickListener {
                    itemClickListener.onItemClick(itemView, pos, EventType.VIEW)
                }

                itemView.findViewById<ImageView>(R.id.nft_send_button).setOnClickListener {
                    itemClickListener.onItemClick(itemView, pos, EventType.SEND)
                }
            }
        }
    }
}