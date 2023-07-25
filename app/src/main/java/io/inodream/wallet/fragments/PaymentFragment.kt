package io.inodream.wallet.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import io.inodream.wallet.R
import io.inodream.wallet.refer.retrofit.activity.RetrofitSimpleActivity
import io.inodream.wallet.util.UserManager


class PaymentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_payment, container, false)
        view.findViewById<TextView>(R.id.accountName).text = UserManager.getInstance().email
        return view
    }

    override fun onResume() {
        super.onResume()

        startActivity(Intent(requireActivity(), RetrofitSimpleActivity::class.java))
    }
}