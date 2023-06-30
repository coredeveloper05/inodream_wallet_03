package io.inodream.wallet.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.inodream.wallet.R
import io.inodream.wallet.refer.retrofit.activity.RetrofitSimpleActivity


class PaymentFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }

    override fun onResume() {
        super.onResume()

        startActivity(Intent(requireActivity(), RetrofitSimpleActivity::class.java))
    }
}