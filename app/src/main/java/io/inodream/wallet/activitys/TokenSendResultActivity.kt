package io.inodream.wallet.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.inodream.wallet.R
import io.inodream.wallet.databinding.ActivityTokenSendResultBinding

class TokenSendResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTokenSendResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTokenSendResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}