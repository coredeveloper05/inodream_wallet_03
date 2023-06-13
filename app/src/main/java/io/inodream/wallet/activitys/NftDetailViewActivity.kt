package io.inodream.wallet.activitys

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.inodream.wallet.R
import io.inodream.wallet.databinding.ActivityNftDetailViewBinding

class NftDetailViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNftDetailViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNftDetailViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topToolbar.title.text = resources.getString(R.string.title_nft_detail)

        binding.topToolbar.backButton.setOnClickListener {
            super.onBackPressed()
        }

        binding.nftSendTv.setOnClickListener {
            startActivity(Intent(this, NftSendRequestActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
        }
    }
}