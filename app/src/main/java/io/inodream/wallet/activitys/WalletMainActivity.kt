package io.inodream.wallet.activitys

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import io.inodream.wallet.R
import io.inodream.wallet.databinding.ActivityWalletMainBinding

class WalletMainActivity : AppCompatActivity(), View.OnClickListener  {

    private lateinit var binding: ActivityWalletMainBinding

    private lateinit var selectedBottomMenuItem: LinearLayout
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    private var stateBackButtonOnce = false
    private var selectedTabInex = 0
    private var appFinishCheckTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWalletMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.wallet_fragment_host) as NavHostFragment
        navController = navHostFragment.navController

        binding.walletTabLl.setOnClickListener(this)
        binding.swapTabLl.setOnClickListener(this)
        binding.payTabLl.setOnClickListener(this)
        binding.setupTabLl.setOnClickListener(this)

        selectedBottomMenuItem = binding.walletTabLl
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.walletTabLl -> changeBottomMenu(binding.walletTabLl, 0, "tab")
            R.id.swapTabLl -> changeBottomMenu(binding.swapTabLl, 1, "tab")
            R.id.payTabLl -> changeBottomMenu(binding.payTabLl, 2, "tab")
            R.id.setupTabLl -> changeBottomMenu(binding.setupTabLl, 3, "tab")
            else -> Toast.makeText(this, R.string.there_is_no_menu, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        navHostFragment?.let {
            when(it.childFragmentManager.primaryNavigationFragment?.javaClass?.simpleName) {
                "BalanceFragment" -> {
                    if(System.currentTimeMillis() - appFinishCheckTime >= 1500) {
                        appFinishCheckTime = System.currentTimeMillis()
                        Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        finish()
                    }
                }
                "SwapFragment", "PaymentFragment", "SettingFragment" -> {
                    binding.walletTabLl.performClick()
                }
                else -> {
                    navController?.let {navCon ->
                        if(navCon.popBackStack()) return
                    }
                }
            }
        }
    }

    private fun changeBottomMenu(tabItem: LinearLayout, tabIndex: Int, callType: String) {
        if(selectedTabInex == tabIndex) return
        tabItem.setBackgroundResource(R.drawable.circle_tab_select_ripple);
        (tabItem.getChildAt(0) as ImageView).setImageResource(when(tabIndex) {
            0 -> R.drawable.wallet_on
            1 -> R.drawable.swap_on
            2 -> R.drawable.pay_on
            3 -> R.drawable.setup_on
            else -> R.drawable.wallet_on
        })
        (tabItem.getChildAt(1) as TextView).setTextColor(Color.WHITE)

        if(tabItem.id != selectedBottomMenuItem.id) {
            selectedBottomMenuItem?.let {
                it.setBackgroundResource(R.drawable.circle_tab_default_ripple)
                (it.getChildAt(0) as ImageView).setImageResource(
                    when (selectedTabInex) {
                        0 -> R.drawable.wallet_off
                        1 -> R.drawable.swap_off
                        2 -> R.drawable.pay_off
                        3 -> R.drawable.setup_off
                        else -> R.drawable.wallet_off
                    }
                )
                (it.getChildAt(1) as TextView).setTextColor(Color.rgb(170, 170, 170))
            }
        }

        if(callType == "tab") {
            findNavController(R.id.wallet_fragment_host).navigate(
                when (tabIndex) {
                    0 -> R.id.balanceFragment
                    1 -> R.id.swapFragment
                    2 -> R.id.paymentFragment
                    3 -> R.id.settingFragment
                    else -> R.id.balanceFragment
                }
                , null, null)
        }

        selectedTabInex = tabIndex
        selectedBottomMenuItem = tabItem
    }
}