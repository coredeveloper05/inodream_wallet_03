package io.inodream.wallet.activitys

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.inodream.wallet.R
import io.inodream.wallet.databinding.ActivitySendTransactPasswordBinding

class SendTransactPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySendTransactPasswordBinding
    private var symbol: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySendTransactPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        symbol = intent.getStringExtra("key") ?: ""

        binding.topToolbar.title.text = resources.getString(R.string.title_token_send)

        setListener()
    }

    private fun setListener() {
        binding.passwordRegisterButton.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    TokenSendRequestActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            )
        }
        binding.topToolbar.backButton.setOnClickListener {
            finish()
        }
    }
}