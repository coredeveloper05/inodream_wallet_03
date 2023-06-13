package io.inodream.wallet.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.inodream.wallet.R
import io.inodream.wallet.databinding.ActivityTermAdvertiesePolicyBinding

class TermAdvertiesePolicyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTermAdvertiesePolicyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTermAdvertiesePolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            super.onBackPressed()
        }
    }
}