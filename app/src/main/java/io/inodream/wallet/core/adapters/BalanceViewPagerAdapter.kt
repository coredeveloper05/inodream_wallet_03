package io.inodream.wallet.core.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class BalanceViewPagerAdapter(fragment: Fragment, itemArray: Array<Fragment>): FragmentStateAdapter(fragment) {

    private val viewItemCount by lazy {
        itemArray.size
    }
    private val viewItemArray by lazy {
        itemArray
    }

    override fun getItemCount(): Int {
        return viewItemCount
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> viewItemArray[0]
            1 -> viewItemArray[1]
            else -> viewItemArray[0]
        }
    }
}