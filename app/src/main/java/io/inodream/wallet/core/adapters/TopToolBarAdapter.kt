package io.inodream.wallet.core.adapters

import android.app.Application
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import io.inodream.wallet.R

class TopToolBarAdapter {

    constructor(view: View, context: Fragment) {
        this.view = view
        this.context = context
    }

    private var view: View
    private var context: Fragment

    fun initTopToolBarEvent() {
        view.findViewById<AppCompatImageView>(R.id.account_button)?.let {
            it.setOnClickListener {
                //context.findNavController().navigate(R.id.accountMngFragment)
            }
        }
        // 2023-07-08 collraborator grant test !

        view.findViewById<ImageButton>(R.id.qrCameraViewButton)?.let {
            it.setOnClickListener {
                Toast.makeText(context.requireContext(), "QRCODE 카메라 연동", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
