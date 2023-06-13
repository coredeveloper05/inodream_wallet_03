package io.inodream.wallet.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.inodream.wallet.R
import io.inodream.wallet.core.adapters.TopToolBarAdapter
import io.inodream.wallet.databinding.FragmentSwapBinding

class SwapFragment : Fragment() {

    private var _binding: FragmentSwapBinding? = null
    private val binding get() = _binding!!

    private lateinit var swapSendToken: LinearLayout
    private lateinit var swapReceiveToken: LinearLayout
    private lateinit var swapSlippage: LinearLayout
    private lateinit var swapBotomSheet: BottomSheetDialog
    private lateinit var slippageSheet: BottomSheetDialog
    private lateinit var slipapgeSb: SeekBar

    private var selectedSlippage = 1
    private var scopeSlippageLaArray: Array<TextView>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSwapBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        TopToolBarAdapter(view, this).initTopToolBarEvent()

        swapSendToken = binding.swapSendToken as LinearLayout
        swapReceiveToken = binding.swapReceiveToken as LinearLayout
        swapSlippage = binding.swapSlippage as LinearLayout

        val inflater = LayoutInflater.from(requireContext()) as LayoutInflater
        val view = inflater.inflate(R.layout.swap_bottom_sheet, null)

        swapBotomSheet = BottomSheetDialog(requireContext())
        swapBotomSheet.setContentView(view)




        val slippageView = inflater.inflate(R.layout.swap_bottom_sheet_slippage, null)
        slippageSheet = BottomSheetDialog(requireContext())
        slippageSheet.setContentView(slippageView)

        slipapgeSb = slippageView.findViewById<View>(R.id.slipapgeSb) as SeekBar
        slipapgeSb.progress = 2


        var slippageLa1 = slippageView.findViewById<View>(R.id.slippageLa1) as TextView
        var slippageLa2 = slippageView.findViewById<View>(R.id.slippageLa2) as TextView
        var slippageLa3 = slippageView.findViewById<View>(R.id.slippageLa3) as TextView
        var slippageLa4 = slippageView.findViewById<View>(R.id.slippageLa4) as TextView

        scopeSlippageLaArray = arrayOf(slippageLa1, slippageLa2, slippageLa3, slippageLa4)

        swapSendToken.setOnClickListener { swapBotomSheet.show() }
        swapReceiveToken.setOnClickListener { swapBotomSheet.show() }
        swapSlippage.setOnClickListener { slippageSheet.show() }

        slipapgeSb.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                setSlippageLabel(p1)
                binding.currentSlippage.text = "${p1}%"
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        binding.swaptxt01.setOnClickListener {
            //findNavController().navigate(R.id.action_swapFragment_to_swapResultSuccessFragment)
        }
    }

    fun setSlippageLabel(select: Int) {
        if (selectedSlippage == select) {
            return;
        }
        scopeSlippageLaArray?.let {
            for((index, item) in scopeSlippageLaArray!!.withIndex()) {
                when(select) {
                    index + 1 -> { item.setBackgroundResource(R.drawable.slippage_label) }
                    else -> { item.setBackgroundColor(Color.WHITE) }
                }
            }
        }

        selectedSlippage = select
    }
}