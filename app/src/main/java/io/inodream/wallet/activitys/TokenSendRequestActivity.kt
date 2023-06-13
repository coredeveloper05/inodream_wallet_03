package io.inodream.wallet.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.inodream.wallet.R
import io.inodream.wallet.databinding.ActivityTokenSendRequestBinding

class TokenSendRequestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTokenSendRequestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTokenSendRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topToolbar.title.text = resources.getString(R.string.title_token_send)

        binding.topToolbar.backButton.setOnClickListener {
            super.onBackPressed()
        }

        binding.sendRequestButton.setOnClickListener {
            startActivity(Intent(this, TokenSendResultActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
        }
    }
}