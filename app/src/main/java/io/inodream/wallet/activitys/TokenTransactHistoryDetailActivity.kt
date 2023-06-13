package io.inodream.wallet.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.inodream.wallet.R
import io.inodream.wallet.databinding.ActivityTokenTransactHistoryDetailBinding

class TokenTransactHistoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTokenTransactHistoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTokenTransactHistoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}