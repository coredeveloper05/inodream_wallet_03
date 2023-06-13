package io.inodream.wallet.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.inodream.wallet.R
import io.inodream.wallet.databinding.ActivityTermServiceBinding

class TermServiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTermServiceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTermServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            super.onBackPressed()
        }
    }
}