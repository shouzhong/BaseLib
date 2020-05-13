package com.shouzhong.base.demo.act

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import androidx.databinding.ObservableField
import com.shouzhong.base.act.BActivity
import com.shouzhong.base.act.BViewModel
import com.shouzhong.base.demo.R
import com.shouzhong.base.request.RequestUtils
import com.shouzhong.base.util.getApp
import com.shouzhong.base.util.startActivity

class MainActivity : BActivity<MainViewModel>(R.layout.act_main)

class MainViewModel : BViewModel() {
    val text = ObservableField<String>()

    fun onClickFragment(view: View) {
        Intent(getActivity(), FragmentActivity::class.java).startActivity(getActivity())
    }

    fun onClickDialog(view: View) {
        Intent(getActivity(), DialogActivity::class.java).startActivity(getActivity())
    }

    fun onClickPopup(view: View) {
        Intent(getActivity(), PopupActivity::class.java).startActivity(getActivity())
    }

    fun onClickPermission(view: View) {
        Intent(getActivity(), PermissionActivity::class.java).startActivity(getActivity())
    }

    fun onClickRequest(view: View) {
//        RequestUtils.startActivityForResult(
//            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
//                data = Uri.parse("package:${getApp().packageName}")
//            }
//        ) { resultCode, data ->
//            text.set("${resultCode}->${data?.getStringExtra("data")}")
//        }
        Intent(getActivity(), RequestActivity::class.java).apply {
            putExtra("data", "这是来自MainActivity的数据")
        }.startActivity(getActivity()) { resultCode, data ->
            text.set("${resultCode}->${data?.getStringExtra("data")}")
        }
    }
}
