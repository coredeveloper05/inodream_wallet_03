package io.inodream.wallet.activitys

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ScreenUtils
import com.bumptech.glide.Glide
import io.inodream.wallet.R
import io.inodream.wallet.databinding.ActivityNftDetailViewBinding
import io.inodream.wallet.refer.retrofit.data.NFTData
import io.inodream.wallet.util.StringUtils

class NftDetailViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNftDetailViewBinding
    private lateinit var nftData: NFTData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNftDetailViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getSerializableExtra("key") as NFTData?
        if (data == null) {
            finish()
            return
        }
        nftData = data

        initView()
        setListener()
    }

    private fun initView() {
        binding.topToolbar.title.text = resources.getString(R.string.title_nft_detail)
        val ivNftSymbol = binding.ivNftDetail
        val lp = ivNftSymbol.layoutParams as LinearLayout.LayoutParams
        lp.height = ScreenUtils.getScreenWidth() - ConvertUtils.dp2px(52 * 2f)
        Glide.with(this).load(nftData.metadata?.image).fitCenter()
            .placeholder(R.drawable.ic_nft_symbol_01).into(ivNftSymbol)
        binding.tvNftId.text = StringUtils.getShortAddressAndId(nftData.address, nftData.id)
        binding.tvNftName.text = StringUtils.getShortAddress(nftData.creator)
        binding.tvNftPrice.text = ""
        binding.tvNftDesc.text = nftData.metadata?.description
        binding.tvBalance.text = nftData.balanceOfUser
        binding.tvContractName.text = nftData.contractName
        binding.tvAddress.text = StringUtils.getShortAddress(nftData.address)
        binding.tvId.text = StringUtils.getShortAddress(nftData.id)
        binding.tvStandard.text = nftData.standard
        binding.tvChain.text = "Ethereum"
    }

    private fun setListener() {
        binding.topToolbar.backButton.setOnClickListener {
            finish()
        }
        binding.nftSendTv.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    NftSendRequestActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION).putExtra("key", nftData)
            )
        }
    }
}