package io.inodream.wallet.fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import io.inodream.wallet.App
import io.inodream.wallet.R
import io.inodream.wallet.activitys.SetWalletInitActivity
import io.inodream.wallet.activitys.TokenSendRequestActivity
import io.inodream.wallet.databinding.FragmentSettingBinding
import io.inodream.wallet.util.UserManager
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SettingFragment : BaseFragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private var withdrawalInitDialog: AlertDialog? = null
    private var requestValidatorTime: TextView? = null
    private var withdrawalInitView: View? = null
    private val compositeDisposable = CompositeDisposable()

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

        if(withdrawalInitDialog == null) {
            withdrawalInitView = LayoutInflater.from(requireContext()).inflate(R.layout.view_withdrawal_init, null)

            withdrawalInitDialog = AlertDialog.Builder(requireContext())
                .setView(withdrawalInitView)
                .create()

            withdrawalInitDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            requestValidatorTime = withdrawalInitView?.findViewById<TextView>(R.id.requestValidatorTime)

            withdrawalInitView?.findViewById<ConstraintLayout>(R.id.requestCredentialBtn)?.setOnClickListener {
                requestValidatorTime?.visibility = View.VISIBLE

                if (App.getInstance().requestValidatorTime == App.REQUEST_VALIDATOR_TIME) {
                    App.getInstance().tickerRequestValidatorTime()
                    checkRequestCredentialTime()
                } else {
                    Toast.makeText(requireContext(), getString(R.string.request_credential_error_message, App.getInstance().requestValidatorTime), Toast.LENGTH_SHORT).show()
                }
            }
            withdrawalInitView?.findViewById<LinearLayout>(R.id.initBtn)?.setOnClickListener {
                withdrawalInitDialog?.dismiss()
            }

            withdrawalInitDialog?.show()
        } else {
            withdrawalInitDialog?.show()
        }

        if(App.getInstance().requestValidatorTime != App.REQUEST_VALIDATOR_TIME) {
            checkRequestCredentialTime()
            requestValidatorTime?.visibility = View.VISIBLE
        }
    }

    private fun checkRequestCredentialTime() {
        compositeDisposable.add(Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                requestValidatorTime?.text = "${App.getInstance().requestValidatorTime} 초"

                if(App.getInstance().requestValidatorTime <= 0 || App.getInstance().requestValidatorTime == App.REQUEST_VALIDATOR_TIME) {
                    initRequestCredential()
                }
            })
    }

    private fun initRequestCredential() {
        compositeDisposable.clear()
        requestValidatorTime?.visibility = View.INVISIBLE
    }
}