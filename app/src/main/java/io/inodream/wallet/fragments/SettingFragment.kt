package io.inodream.wallet.fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.inodream.wallet.R
import io.inodream.wallet.activitys.SetWalletInitActivity
import io.inodream.wallet.activitys.TokenSendRequestActivity
import io.inodream.wallet.databinding.FragmentSettingBinding
import io.inodream.wallet.util.UserManager

class SettingFragment : BaseFragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private lateinit var withdrawalInitDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.actSetWalletInitTv.setOnClickListener {
            startActivity(Intent(requireContext(), SetWalletInitActivity::class.java))
        }

        binding.actSetSecurityTv.setOnClickListener {
            loadDialog()
        }
    }

    private fun loadDialog() {
        val withdrawalInitView = LayoutInflater.from(requireContext()).inflate(R.layout.view_withdrawal_init, null)

        withdrawalInitDialog = AlertDialog.Builder(requireContext())
            .setView(withdrawalInitView)
            .create()

        withdrawalInitDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        withdrawalInitView.findViewById<TextView>(R.id.initCloseBtn)?.setOnClickListener {
            withdrawalInitDialog?.dismiss()
        }

        withdrawalInitDialog?.show()
    }
}