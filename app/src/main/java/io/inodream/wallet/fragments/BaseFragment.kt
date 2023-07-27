package io.inodream.wallet.fragments

import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import io.inodream.wallet.R
import io.inodream.wallet.activitys.CameraCaptureActivity
import io.inodream.wallet.event.LoadSendPageEvent
import io.inodream.wallet.util.UserManager
import org.greenrobot.eventbus.EventBus

open class BaseFragment : Fragment() {

    //    private var mLoadingDialog: MaterialDialog? = null
//
//    fun showLoadingDialog() {
//        context?.let {
//            dismissLoadingDialog()
//            mLoadingDialog = MaterialDialog(requireContext(), DEFAULT_BEHAVIOR)
//                .message(null, "Loading...", null)
//                .progress(true, 0).cancelable(false).show()
//        }
//    }
//
//    fun dismissLoadingDialog() {
//        if (mLoadingDialog?.isShowing == true) {
//            mLoadingDialog?.dismiss()
//            mLoadingDialog = null
//        }
//    }
    fun initToolBar(view: View) {
        view.findViewById<TextView>(R.id.accountName).text = UserManager.getInstance().email
        view.findViewById<View>(R.id.qrCameraViewButton).setOnClickListener {
            startActivityForResult(
                Intent(context, CameraCaptureActivity::class.java), 100
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == AppCompatActivity.RESULT_OK) {
            val result = data?.getStringExtra("result")
            result?.let {
                EventBus.getDefault().post(LoadSendPageEvent(result))
            }
        }
    }
}