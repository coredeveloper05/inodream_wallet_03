package io.inodream.wallet.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.inodream.wallet.R
import io.inodream.wallet.databinding.ActivitySendTransactPasswordBinding

class SendTransactPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySendTransactPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySendTransactPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.passwordRegisterButton.setOnClickListener {
            startActivity(Intent(this, TokenSendRequestActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
        }

        binding.topToolbar.title.text = resources.getString(R.string.title_token_send)

        binding.topToolbar.backButton.setOnClickListener {
            super.onBackPressed()
        }
    }
}