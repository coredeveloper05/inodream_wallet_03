package io.inodream.wallet.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.inodream.wallet.R
import io.inodream.wallet.databinding.ActivityTermPrivacyPolicyBinding

class TermPrivacyPolicyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTermPrivacyPolicyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTermPrivacyPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            super.onBackPressed()
        }
    }
}