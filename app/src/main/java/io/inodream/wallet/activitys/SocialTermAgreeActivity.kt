package io.inodream.wallet.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import io.inodream.wallet.R
import io.inodream.wallet.databinding.ActivitySocialTermAgreeBinding

class SocialTermAgreeActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySocialTermAgreeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySocialTermAgreeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.checkTermAgreementAll.isChecked = false
        binding.checkTermService.isChecked = false
        binding.checkTermPrivacyPolicy.isChecked = false
        binding.checkTermLocationPolicy.isChecked = false
        binding.checkTermAdvertiseAlarmPolicy.isChecked = false

        binding.termDetailButton01.setOnClickListener {
            startActivity(Intent(this, TermServiceActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
        }
        binding.termDetailButton02.setOnClickListener {
            startActivity(Intent(this, TermPrivacyPolicyActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
        }
        binding.termDetailButton03.setOnClickListener {
            startActivity(Intent(this, TermLocationPolicyActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
        }
        binding.termDetailButton04.setOnClickListener {
            startActivity(Intent(this, TermAdvertiesePolicyActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
        }

        binding.checkTermAgreementAllLabel.setOnClickListener {
            binding.checkTermAgreementAll.performClick()
        }
        binding.checkTermAgreementAll.setOnClickListener {
            println((it as CheckBox).isChecked)
            if((it as CheckBox).isChecked) {
                binding.checkTermService.isChecked = true
                binding.checkTermPrivacyPolicy.isChecked = true
                binding.checkTermLocationPolicy.isChecked = true
                binding.checkTermAdvertiseAlarmPolicy.isChecked = true
            }
        }

        binding.termConfirm.setOnClickListener {
            startActivity(Intent(this, WalletMainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }
}