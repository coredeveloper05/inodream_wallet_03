package io.inodream.wallet.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import io.inodream.wallet.R
import io.inodream.wallet.core.adapters.BalanceViewPagerAdapter
import io.inodream.wallet.databinding.FragmentBalanceBinding

class BalanceFragment : Fragment() {

    private var _binding: FragmentBalanceBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewFragmentItemArray: Array<Fragment>;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBalanceBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabItemArray = arrayOf(getString(R.string.token), getString(R.string.nft))

        val tabLayout = binding.balanceTab
        val viewPager = binding.balanceViewPager

        viewFragmentItemArray = arrayOf(
            TokenViewFragment(),
            NftViewFragment()
        )

        viewPager.adapter = BalanceViewPagerAdapter(this, viewFragmentItemArray)

        TabLayoutMediator(tabLayout, viewPager) { tab, position  ->
            tab.text = tabItemArray[position]
        }.attach()
    }
}