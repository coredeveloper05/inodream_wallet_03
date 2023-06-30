package io.inodream.wallet.fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import io.inodream.wallet.R
import io.inodream.wallet.activitys.NftDetailViewActivity
import io.inodream.wallet.activitys.NftImportActivity
import io.inodream.wallet.activitys.NftSendRequestActivity
import io.inodream.wallet.core.adapters.NftRecycleAdapter
import io.inodream.wallet.core.data.NftItemData
import io.inodream.wallet.databinding.FragmentNftViewBinding

class NftViewFragment : Fragment(), NftRecycleAdapter.OnItemClickEventListener {

    private var _binding: FragmentNftViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNftViewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
    override fun onItemClick(
        nft_view: View?,
        nft_position: Int,
        nft_event_type: NftRecycleAdapter.EventType
    ) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nftItemList = arrayListOf<NftItemData>(
            NftItemData("1", "Titlesdj...#2", "Writer", ""),
            NftItemData("3", "Titlesdj...#2", "Writer", ""),
            NftItemData("5", "Titlesdj...#2", "Writer", ""),
            NftItemData("6", "Titlesdj...#2", "Writer", ""),
        )

        val nftItemAdapter = NftRecycleAdapter(nftItemList)
        nftItemAdapter.notifyDataSetChanged()

        //Key
        nftItemAdapter.setItemClickListener(object: NftRecycleAdapter.OnItemClickEventListener {
            override fun onItemClick(nft_view: View?, nft_position: Int, nft_event_type: NftRecycleAdapter.EventType) {
                startActivity(Intent(requireContext(), when(nft_event_type) {
                    NftRecycleAdapter.EventType.VIEW -> {
                        NftDetailViewActivity::class.java
                    }
                    NftRecycleAdapter.EventType.SEND -> {
                        NftSendRequestActivity::class.java
                    }
                }).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
            }
        })

        binding.nftRecycleview.adapter = nftItemAdapter
        binding.nftRecycleview.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.nftAddCall.setOnClickListener {
            startActivity(Intent(requireContext(), NftImportActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
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
    }
}