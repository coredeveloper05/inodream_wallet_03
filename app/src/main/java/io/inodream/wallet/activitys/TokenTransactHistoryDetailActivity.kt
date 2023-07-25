package io.inodream.wallet.activitys

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import io.inodream.wallet.R
import io.inodream.wallet.databinding.ActivityTokenTransactHistoryDetailBinding
import io.inodream.wallet.refer.retrofit.data.TxData
import io.inodream.wallet.util.UserManager


class TokenTransactHistoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTokenTransactHistoryDetailBinding
    private var txData: TxData.Data? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTokenTransactHistoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        txData = intent.getSerializableExtra("key") as TxData.Data?
        if (txData == null) {
            finish()
            return
        }

        initView()

        binding.btnTx.setOnClickListener {
            // FIXME: modify debug config
            val uri =
                Uri.parse((if (true) "https://sepolia.etherscan.io/tx/" else "https://etherscan.io/tx/") + txData?.txId)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        binding.backBu.setOnClickListener { finish() }
    }

    private fun initView() {
        val symbol: String = when (txData?.symbol) {
            "USDT" -> "USDT | 이더리움"
            "FON" -> "FON | 이더리움"
            else -> "ETH | 이더리움"
        }
        binding.tvSymbol1.text = symbol
        binding.tvValue1.text = txData?.value + " " + txData?.symbol
        var state: String = when (txData?.status) {
            "0" -> getString(R.string.tx_create)
            "1" -> getString(R.string.tx_progress)
            "2" -> getString(R.string.tx_confirm)
            else -> getString(R.string.tx_complete)
        }
        if (txData?.from == UserManager.getInstance().address) {
            state = getString(R.string.tx_send) + state
        } else {
            state = getString(R.string.tx_receive) + state
        }
        binding.tvState.text = state
        binding.tvSymbol2.text = symbol + "\n" + txData?.value
        binding.tvDate.text = txData?.updatedAt
        binding.tvGas.text = txData?.gasPrice + " ETH"
        binding.btnTx.isEnabled = !TextUtils.isEmpty(txData?.idx)
    }
}