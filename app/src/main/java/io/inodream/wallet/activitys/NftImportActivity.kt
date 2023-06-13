package io.inodream.wallet.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.inodream.wallet.R
import io.inodream.wallet.databinding.ActivityNftImportBinding

class NftImportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNftImportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNftImportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topToolbar.title.text = resources.getString(R.string.title_nft_import)

        binding.topToolbar.backButton.setOnClickListener {
            super.onBackPressed()
        }
    }
}