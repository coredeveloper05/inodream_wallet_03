package io.inodream.wallet.fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.blankj.utilcode.util.ToastUtils
import io.inodream.wallet.App
import io.inodream.wallet.R
import io.inodream.wallet.activitys.SendTransactPasswordActivity
import io.inodream.wallet.activitys.SetWalletInitActivity
import io.inodream.wallet.activitys.TokenLockActivity
import io.inodream.wallet.databinding.FragmentSettingBinding
import io.inodream.wallet.event.UpdateAssetsEvent
import io.inodream.wallet.refer.retrofit.RetrofitClient
import io.inodream.wallet.refer.retrofit.data.BaseResponse
import io.inodream.wallet.util.StringUtils
import io.inodream.wallet.util.UserManager
import io.inodream.wallet.util.encrypt.RequestUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class SettingFragment : BaseFragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private var withdrawalInitDialog: AlertDialog? = null
    private var requestValidatorTime: TextView? = null
    private var withdrawalInitView: View? = null
    private val compositeDisposable = CompositeDisposable()
    private var verifyToken: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSettingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventBus.getDefault().register(this)
        binding.actSetWalletInitTv.setOnClickListener {
            startActivity(Intent(requireContext(), SetWalletInitActivity::class.java))
        }
        binding.actEventLockTv.setOnClickListener {
            startActivity(Intent(requireContext(), TokenLockActivity::class.java))
        }
        binding.actSetSecurityTv.setOnClickListener {
            loadDialog()
        }
        binding.tvAccount.text = UserManager.getInstance().email
        binding.tvAddress.text = StringUtils.getShortAddress(UserManager.getInstance().address)
        binding.tvPrice.text = App.getInstance().value
    }

    private fun loadDialog() {

        if (withdrawalInitDialog == null) {
            withdrawalInitView =
                LayoutInflater.from(requireContext()).inflate(R.layout.view_withdrawal_init, null)

            withdrawalInitDialog = AlertDialog.Builder(requireContext())
                .setView(withdrawalInitView)
                .create()

            withdrawalInitDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            requestValidatorTime =
                withdrawalInitView?.findViewById<TextView>(R.id.requestValidatorTime)

            val btnSend: ConstraintLayout? =
                withdrawalInitView?.findViewById<ConstraintLayout>(R.id.requestCredentialBtn)
            btnSend?.setOnClickListener {
                if (App.getInstance().requestValidatorTime == App.REQUEST_VALIDATOR_TIME) {
                    signStart { state ->
                        when (state) {
                            RequestState.REQUEST_START -> {
                                btnSend.isClickable = false
                            }

                            RequestState.REQUEST_SUCCESS -> {
                                btnSend.isClickable = true
                                requestValidatorTime?.visibility = View.VISIBLE
                                ToastUtils.showLong("확인 코드가 성공적으로 전송되었습니다.")
                                App.getInstance().tickerRequestValidatorTime()
                                checkRequestCredentialTime()
                            }

                            else -> {
                                btnSend.isClickable = true
                            }
                        }
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(
                            R.string.request_credential_error_message,
                            App.getInstance().requestValidatorTime
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            val btnSubmit: LinearLayout? =
                withdrawalInitView?.findViewById<LinearLayout>(R.id.initBtn)
            btnSubmit?.setOnClickListener {
                val code =
                    withdrawalInitView?.findViewById<EditText>(R.id.et_balance)?.text.toString()
                signVerify(code) {
                    when (it) {
                        RequestState.REQUEST_START -> {
                            btnSend?.isClickable = false
                            btnSubmit.isClickable = false
                        }

                        RequestState.REQUEST_SUCCESS -> {
                            startActivity(
                                Intent(
                                    context,
                                    SendTransactPasswordActivity::class.java
                                ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                    .putExtra("token", verifyToken)
                                    .putExtra("code", code)
                            )
                            withdrawalInitDialog?.dismiss()
                            btnSend?.isClickable = true
                            btnSubmit.isClickable = true
                        }

                        else -> {
                            btnSend?.isClickable = true
                            btnSubmit.isClickable = true
                        }
                    }
                }
            }

            withdrawalInitDialog?.show()
        } else {
            withdrawalInitDialog?.show()
        }

        if (App.getInstance().requestValidatorTime != App.REQUEST_VALIDATOR_TIME) {
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

                if (App.getInstance().requestValidatorTime <= 0 || App.getInstance().requestValidatorTime == App.REQUEST_VALIDATOR_TIME) {
                    initRequestCredential()
                }
            })
    }

    private fun initRequestCredential() {
        compositeDisposable.clear()
        requestValidatorTime?.visibility = View.INVISIBLE
    }

    private fun signStart(result: (state: RequestState) -> Unit) {
        val map: MutableMap<String, String> = HashMap()
        map["email"] = UserManager.getInstance().email
        result(RequestState.REQUEST_START)
        RetrofitClient
            .remoteSimpleService
            .signStart(RequestUtil().getRequestHeader(), map)
            .enqueue(object : Callback<BaseResponse<String>> {
                override fun onResponse(
                    call: Call<BaseResponse<String>>,
                    response: Response<BaseResponse<String>>
                ) {
                    if (RequestUtil().checkResponse(response)) {
                        response.body()?.let {
                            verifyToken = it.data
                            result(RequestState.REQUEST_SUCCESS)
                            return
                        }
                    }
                    result(RequestState.REQUEST_FAIL)
                }

                override fun onFailure(call: Call<BaseResponse<String>>, t: Throwable) {
                    result(RequestState.REQUEST_FAIL)
                    t.printStackTrace()
                    ToastUtils.showLong(t.message)
                }
            })
    }

    private fun signVerify(code: String?, result: (state: RequestState) -> Unit) {
        if (TextUtils.isEmpty(verifyToken)) {
            ToastUtils.showLong("먼저 인증 코드를 보내주세요.")
        } else if (TextUtils.isEmpty(code)) {
            ToastUtils.showLong("인증 코드를 입력해 주세요.")
        } else {
            val map: MutableMap<String, String> = HashMap()
            map["verifyToken"] = verifyToken!!
            map["verifyCode"] = code!!
            result(RequestState.REQUEST_START)
            RetrofitClient
                .remoteSimpleService
                .signVerify(RequestUtil().getRequestHeader(), map)
                .enqueue(object : Callback<BaseResponse<Any>> {
                    override fun onResponse(
                        call: Call<BaseResponse<Any>>,
                        response: Response<BaseResponse<Any>>
                    ) {
                        if (RequestUtil().checkResponse(response)) {
                            response.body()?.let {
                                result(RequestState.REQUEST_SUCCESS)
                                return
                            }
                        }
                        result(RequestState.REQUEST_FAIL)
                    }

                    override fun onFailure(call: Call<BaseResponse<Any>>, t: Throwable) {
                        result(RequestState.REQUEST_FAIL)
                        t.printStackTrace()
                        ToastUtils.showLong(t.message)
                    }
                })
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateValue(event: UpdateAssetsEvent) {
        binding.tvPrice.text = App.getInstance().value
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }
}

enum class RequestState {
    REQUEST_START,
    REQUEST_SUCCESS,
    REQUEST_FAIL
}