package com.shouzhong.base.demo.act

import android.content.Intent
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.shouzhong.base.act.BActivity
import com.shouzhong.base.act.BViewModel
import com.shouzhong.base.demo.R
import com.shouzhong.base.util.getApp
import com.shouzhong.base.util.startActivity
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

class MainActivity : BActivity<MainViewModel>(R.layout.act_main)

class MainViewModel : BViewModel() {
    val text = MutableLiveData<String?>()

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
        }.startActivity { resultCode, data ->
            text.value = "${resultCode}->${data?.getStringExtra("data")}"
        }
    }

    fun onClickTest(view: View) {
        text.value = ""
        try {
            val scid = Settings.System.getString(getApp().contentResolver, "ZHVzY2Lk")
            text.value += "fun1:${scid}"
        } catch (e: Throwable) {
            text.value += "error1:${e.message}"
        }
        try {
            val scid = BufferedReader(InputStreamReader(FileInputStream(File("/sdcard/Android/ZHVzY2Lk")))).readLine()
            text.value += "\nfun2:${scid}"
        } catch (e: Throwable) {
            text.value += "\nerror2:${e.message}"
        }
        try {
            val scid = getApp().getSharedPreferences("${getApp().packageName}_dna", 0).getString("ZHVzY2Lk", null)
            text.value += "\nfun3:${scid}"
        } catch (e: Throwable) {
            text.value += "\nerror3:${e.message}"
        }
    }
}
