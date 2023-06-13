package io.inodream.wallet.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.inodream.wallet.R
import io.inodream.wallet.databinding.ActivityTermLocationPolicyBinding

class TermLocationPolicyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTermLocationPolicyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTermLocationPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            super.onBackPressed()
        }
    }
}