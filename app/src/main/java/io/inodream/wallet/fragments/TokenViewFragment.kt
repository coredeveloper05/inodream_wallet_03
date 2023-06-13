package io.inodream.wallet.fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.navigation.fragment.findNavController
import io.inodream.wallet.R
import io.inodream.wallet.activitys.TokenSendSelectionActivity
import io.inodream.wallet.activitys.TokenTransactHistoryActivity
import io.inodream.wallet.databinding.FragmentTokenViewBinding

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
    }

}