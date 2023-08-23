package io.inodream.wallet.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import io.inodream.wallet.R
import io.inodream.wallet.core.adapters.LockRecycleAdapter
import io.inodream.wallet.core.data.LockData
import io.inodream.wallet.databinding.ActivityTokenLockBinding

class TokenLockActivity : AppCompatActivity() {

    private val binding: ActivityTokenLockBinding by lazy {
        ActivityTokenLockBinding.inflate(layoutInflater)
    }

    private var lockDataList = mutableListOf<LockData>()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.topToolbar.title.text = resources.getString(R.string.title_token_lockup)

        binding.topToolbar.backButton.setOnClickListener {
            super.onBackPressed()
        }

        lockDataList.add(LockData("2023년 06월 11일 08:00", "32.3%", "1.0000 FON"))
        lockDataList.add(LockData("2023년 07월 11일 08:00", "50.3%", "9000 FON"))
        lockDataList.add(LockData("2023년 08월 11일 08:00", "10.3%", "9000 FON"))

        val recyclerAdapter = LockRecycleAdapter(lockDataList)

        with(binding) {
            unlockedScheduleRecycler.adapter = recyclerAdapter
            unlockedScheduleRecycler.layoutManager = LinearLayoutManager(this@TokenLockActivity)

        }
    }
}