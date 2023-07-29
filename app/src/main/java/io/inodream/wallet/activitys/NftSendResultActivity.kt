package io.inodream.wallet.activitys

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.inodream.wallet.databinding.ActivityNftSendResultBinding

class NftSendResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNftSendResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNftSendResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener { finish() }
        binding.tokenSend4Tv.setOnClickListener { finish() }
    }
}