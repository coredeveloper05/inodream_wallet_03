package io.inodream.wallet.activitys

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import io.inodream.wallet.R
import io.inodream.wallet.databinding.ActivityTokenSendSelectionBinding
import io.inodream.wallet.refer.retrofit.data.TokenInfosData

class TokenSendSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTokenSendSelectionBinding
    private var mTokenInfos: ArrayList<TokenInfosData.TokenInfo>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTokenSendSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mTokenInfos = intent.getSerializableExtra("key") as ArrayList<TokenInfosData.TokenInfo>?
        initView()
        setListener()
    }

    private fun initView() {
        binding.topToolbar.title.text = resources.getString(R.string.title_token_send)
        mTokenInfos?.forEach {
            when (it.symbol) {
                "ETH" -> {
                    Glide.with(this).load(it.icon).into(binding.ivTokenSymbol01)
                }

                "USDT" -> {
                    Glide.with(this).load(it.icon).into(binding.ivTokenSymbol02)
                }

                "FON" -> {
                    Glide.with(this).load(it.icon).into(binding.ivTokenSymbol04)
                }
            }
        }
    }

    private fun setListener() {
        binding.topToolbar.backButton.setOnClickListener {
            finish()
        }
        binding.selectionToken01.setOnClickListener {
            startPage("ETH")
        }
        binding.selectionToken02.setOnClickListener {
            startPage("USDT")
        }
        binding.selectionToken04.setOnClickListener {
            startPage("FON")
        }
    }

    private fun startPage(symbol: String) {
        val intent = Intent(
            this,
            TokenSendRequestActivity::class.java
        ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        intent.putExtra("key", symbol)
        startActivity(intent)
    }
}