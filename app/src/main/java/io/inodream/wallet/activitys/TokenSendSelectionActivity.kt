package io.inodream.wallet.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.inodream.wallet.R
import io.inodream.wallet.databinding.ActivityTokenSendSelectionBinding

class TokenSendSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTokenSendSelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTokenSendSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topToolbar.title.text = resources.getString(R.string.title_token_send)

        binding.topToolbar.backButton.setOnClickListener {
            super.onBackPressed()
        }

        binding.selectionToken01.setOnClickListener {
            startActivity(Intent(this, SendTransactPasswordActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
        }
    }
}