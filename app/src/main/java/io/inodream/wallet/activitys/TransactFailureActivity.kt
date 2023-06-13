package io.inodream.wallet.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.inodream.wallet.R
import io.inodream.wallet.databinding.ActivityTransactFailureBinding

class TransactFailureActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransactFailureBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTransactFailureBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}