package io.inodream.wallet.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.inodream.wallet.R
import io.inodream.wallet.databinding.ActivityNftSendRequestBinding

class NftSendRequestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNftSendRequestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNftSendRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topToolbar.title.text = resources.getString(R.string.title_nft_send)

        binding.topToolbar.backButton.setOnClickListener {
            super.onBackPressed()
        }

        binding.nftSend02Tv.setOnClickListener {
            startActivity(Intent(this, NftSendResultActivity::class.java))
        }
    }
}