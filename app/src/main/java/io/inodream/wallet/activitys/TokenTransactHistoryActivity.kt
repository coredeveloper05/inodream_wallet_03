package io.inodream.wallet.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import io.inodream.wallet.R
import io.inodream.wallet.databinding.ActivityTokenTransactHistoryBinding

class TokenTransactHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTokenTransactHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTokenTransactHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topToolbar.title.text = resources.getString(R.string.title_token_transact_history)

        binding.topToolbar.backButton.setOnClickListener {
            super.onBackPressed()
        }

        /**
         * 거래이력 상세 이력 유형
         * 1. 받기 : fragment_token_receive
         * 2. 보내기 : fragment_token_send
         * 3. 스왑 : fragment_token_swap
         */
        binding.traLinerLayout01.setOnClickListener {
            startActivity(Intent(this, TokenTransactHistoryDetailActivity::class.java))
        }
        binding.traLinerLayout02.setOnClickListener {
            startActivity(Intent(this, TokenTransactHistoryDetailActivity::class.java))
        }
        binding.traLinerLayout05.setOnClickListener {
            startActivity(Intent(this, TokenTransactHistoryDetailActivity::class.java))
        }
    }
}