package com.shouzhong.base.demo.act

import android.content.Intent
import android.view.View
import com.shouzhong.base.act.BActivity
import com.shouzhong.base.act.BViewModel
import com.shouzhong.base.demo.R
import com.shouzhong.base.util.startActivity

class MainActivity : BActivity<MainViewModel>(R.layout.act_main)

class MainViewModel : BViewModel() {
    fun onClickFragment(view: View) {
        Intent(getActivity(), FragmentActivity::class.java).startActivity { resultCode, data ->
        }
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
}
