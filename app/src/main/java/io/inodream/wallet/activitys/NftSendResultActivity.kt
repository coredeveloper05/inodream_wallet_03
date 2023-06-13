package io.inodream.wallet.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.inodream.wallet.R
import io.inodream.wallet.databinding.ActivityNftSendResultBinding

class NftSendResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNftSendResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNftSendResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}