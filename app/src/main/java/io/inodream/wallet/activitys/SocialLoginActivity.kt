package io.inodream.wallet.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.inodream.wallet.R
import io.inodream.wallet.databinding.ActivitySocialLoginBinding

class SocialLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySocialLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySocialLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.socialLoginButton.setOnClickListener {
            startActivity(Intent(this, SocialTermAgreeActivity::class.java))
        }
    }
}