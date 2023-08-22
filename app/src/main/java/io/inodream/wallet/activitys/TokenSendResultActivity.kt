package io.inodream.wallet.activitys

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import io.inodream.wallet.databinding.ActivityTokenSendResultBinding
import io.inodream.wallet.event.UpdateNftEvent
import org.greenrobot.eventbus.EventBus

class TokenSendResultActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityTokenSendResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTokenSendResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener(this)
        binding.tokenSend4Tv.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        EventBus.getDefault().post(UpdateNftEvent())
        finish()
    }
}