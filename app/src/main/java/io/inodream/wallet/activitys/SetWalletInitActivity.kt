package io.inodream.wallet.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.inodream.wallet.R
import io.inodream.wallet.databinding.ActivitySetWalletInitBinding

class SetWalletInitActivity: AppCompatActivity() {

    private val binding: ActivitySetWalletInitBinding by lazy {
        ActivitySetWalletInitBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
    }
}