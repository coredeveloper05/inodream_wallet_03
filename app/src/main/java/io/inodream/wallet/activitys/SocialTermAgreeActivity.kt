package io.inodream.wallet.activitys

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ToastUtils
import io.inodream.wallet.R
import io.inodream.wallet.databinding.ActivitySocialTermAgreeBinding
import io.inodream.wallet.util.UserManager

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
            if ((it as CheckBox).isChecked) {
                binding.checkTermService.isChecked = true
                binding.checkTermPrivacyPolicy.isChecked = true
                binding.checkTermLocationPolicy.isChecked = true
                binding.checkTermAdvertiseAlarmPolicy.isChecked = true
            } else if (!it.isChecked) {
                binding.checkTermService.isChecked = false
                binding.checkTermPrivacyPolicy.isChecked = false
                binding.checkTermLocationPolicy.isChecked = false
                binding.checkTermAdvertiseAlarmPolicy.isChecked = false
            }
        }
        binding.termConfirm.setOnClickListener {
            if (!binding.checkTermService.isChecked || !binding.checkTermPrivacyPolicy.isChecked) {
                ToastUtils.showLong(R.string.term_error_tip)
            } else {
                val sb: StringBuilder = StringBuilder()
                sb.append(if (binding.checkTermService.isChecked) "1," else "")
                sb.append(if (binding.checkTermPrivacyPolicy.isChecked) "2," else "")
                sb.append(if (binding.checkTermLocationPolicy.isChecked) "3," else "")
                sb.append(if (binding.checkTermAdvertiseAlarmPolicy.isChecked) "4," else "")
                UserManager.getInstance()
                    .saveProtocol(sb.toString().substring(0, sb.toString().length - 1))
                startActivity(Intent(this, WalletMainActivity::class.java))
                finish()
            }
        }
        if (!TextUtils.isEmpty(UserManager.getInstance().protocol)) {
            startActivity(Intent(this, WalletMainActivity::class.java))
            finish()
        }
    }
}