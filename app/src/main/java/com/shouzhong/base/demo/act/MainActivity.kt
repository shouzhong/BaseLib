package com.shouzhong.base.demo.act

import android.content.Intent
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.shouzhong.base.act.BActivity
import com.shouzhong.base.act.BViewModel
import com.shouzhong.base.demo.R
import com.shouzhong.base.util.startActivity

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
        Intent(getActivity(), RequestActivity::class.java).apply {
            putExtra("data", "这是来自MainActivity的数据")
        }.startActivity { resultCode, data ->
            text.value = "${resultCode}->${data?.getStringExtra("data")}"
        }
    }

    fun onClickCoroutines(view: View) {
        Intent(getActivity(), CoroutinesActivity::class.java).startActivity(getActivity())
    }
}
