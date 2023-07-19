package io.inodream.wallet.fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import io.inodream.wallet.R
import io.inodream.wallet.activitys.SocialTermAgreeActivity
import io.inodream.wallet.activitys.TokenSendSelectionActivity
import io.inodream.wallet.activitys.TokenTransactHistoryActivity
import io.inodream.wallet.databinding.FragmentTokenViewBinding
import io.inodream.wallet.refer.retrofit.RetrofitClient
import io.inodream.wallet.refer.retrofit.data.BalanceData
import io.inodream.wallet.refer.retrofit.data.BaseResponse
import io.inodream.wallet.refer.retrofit.data.GoogleAuthData
import io.inodream.wallet.refer.retrofit.data.TokenInfoData
import io.inodream.wallet.refer.retrofit.data.TokenInfosData
import io.inodream.wallet.util.UserManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TokenViewFragment : Fragment() {

    private var _binding: FragmentTokenViewBinding? = null
    private val binding get() = _binding!!

    private lateinit var toteknArea: LinearLayoutCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTokenViewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val layoutInflater = LayoutInflater.from(context)
        val qrView = layoutInflater.inflate(R.layout.view_token_receive, null)
        val qrViewDialog = AlertDialog.Builder(context)
            .setView(qrView)
            .create()

        qrViewDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        qrView.findViewById<TextView>(R.id.close_button)?.setOnClickListener {
            qrViewDialog.dismiss()
        }

        binding.mainButton01.setOnClickListener {
            qrViewDialog.show()
        }

        binding.mainButton02.setOnClickListener {
            startActivity(Intent(requireContext(), TokenSendSelectionActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
        }

        binding.mainButton03.setOnClickListener {
            requireActivity().findViewById<LinearLayout>(R.id.swapTabLl).performClick()
        }

        binding.mainButton04.setOnClickListener {
            startActivity(Intent(requireContext(), TokenTransactHistoryActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
        }

        /*
        토큰 거래이력
         */
        binding.transactHistoryButton.setOnClickListener {
            startActivity(Intent(requireContext(), TokenTransactHistoryActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
        }

        getUserWallet()
        getBalanceAll()
        getBalance()
        getTokenInfos()
        getTokenInfo()
    }

    private fun getUserWallet() {
        val map: MutableMap<String, String> = HashMap()
        map["Authorization"] = "Bearer " + UserManager.getInstance().accToken
        RetrofitClient
            .remoteSimpleService
            .getUserWallet(map)
            .enqueue(object : Callback<BaseResponse<GoogleAuthData.WalletData>> {
                override fun onResponse(
                    call: Call<BaseResponse<GoogleAuthData.WalletData>>,
                    response: Response<BaseResponse<GoogleAuthData.WalletData>>
                ) {
                    response.body()?.let { baseResponse ->
                        baseResponse.data?.let {
                            Log.e("auth", Gson().toJson(it))
                        }
                    }
                }

                override fun onFailure(call: Call<BaseResponse<GoogleAuthData.WalletData>>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }

    private fun getBalanceAll() {
        val map: MutableMap<String, String> = HashMap()
        map["Authorization"] = "Bearer " + UserManager.getInstance().accToken
        RetrofitClient
            .remoteSimpleService
            .getBalanceAll(map)
            .enqueue(object : Callback<BaseResponse<BalanceData>> {
                override fun onResponse(
                    call: Call<BaseResponse<BalanceData>>,
                    response: Response<BaseResponse<BalanceData>>
                ) {
                    response.body()?.let { baseResponse ->
                        baseResponse.data?.let {
                            Log.e("auth", Gson().toJson(it))
                        }
                    }
                }

                override fun onFailure(call: Call<BaseResponse<BalanceData>>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }

    private fun getBalance() {
        val map: MutableMap<String, String> = HashMap()
        map["Authorization"] = "Bearer " + UserManager.getInstance().accToken
        val body: MutableMap<String, String> = HashMap()
        body["symbol"] = "ETH"
        RetrofitClient
            .remoteSimpleService
            .getBalance(map, body)
            .enqueue(object : Callback<BaseResponse<BalanceData>> {
                override fun onResponse(
                    call: Call<BaseResponse<BalanceData>>,
                    response: Response<BaseResponse<BalanceData>>
                ) {
                    response.body()?.let { baseResponse ->
                        baseResponse.data?.let {
                            Log.e("auth", Gson().toJson(it))
                        }
                    }
                }

                override fun onFailure(call: Call<BaseResponse<BalanceData>>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }

    private fun getTokenInfos() {
        val map: MutableMap<String, String> = HashMap()
        map["Authorization"] = "Bearer " + UserManager.getInstance().accToken
        RetrofitClient
            .remoteSimpleService
            .getTokenInfos(map)
            .enqueue(object : Callback<BaseResponse<TokenInfosData>> {
                override fun onResponse(
                    call: Call<BaseResponse<TokenInfosData>>,
                    response: Response<BaseResponse<TokenInfosData>>
                ) {
                    response.body()?.let { baseResponse ->
                        baseResponse.data?.let {
                            Log.e("auth", Gson().toJson(it))
                        }
                    }
                }

                override fun onFailure(call: Call<BaseResponse<TokenInfosData>>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }

    private fun getTokenInfo() {
        val map: MutableMap<String, String> = HashMap()
        map["Authorization"] = "Bearer " + UserManager.getInstance().accToken
        val body: MutableMap<String, String> = HashMap()
        body["symbol"] = "ETH"
        RetrofitClient
            .remoteSimpleService
            .getTokenInfo(map, body)
            .enqueue(object : Callback<BaseResponse<TokenInfoData>> {
                override fun onResponse(
                    call: Call<BaseResponse<TokenInfoData>>,
                    response: Response<BaseResponse<TokenInfoData>>
                ) {
                    response.body()?.let { baseResponse ->
                        baseResponse.data?.let {
                            Log.e("auth", Gson().toJson(it))
                        }
                    }
                }

                override fun onFailure(call: Call<BaseResponse<TokenInfoData>>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }
}