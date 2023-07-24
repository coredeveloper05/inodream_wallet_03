package io.inodream.wallet.activitys

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.inodream.wallet.databinding.ActivityTokenSendResultBinding

class TokenSendResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTokenSendResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTokenSendResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener { finish() }
        binding.tokenSend4Tv.setOnClickListener { finish() }
    }
}