package com.shouzhong.base.demo.act

import android.content.Intent
import android.view.View
import com.shouzhong.base.act.BActivity
import com.shouzhong.base.act.BViewModel
import com.shouzhong.base.demo.R

class MainActivity : BActivity<MainViewModel>(R.layout.act_main)

class MainViewModel : BViewModel() {
    fun onClickFragment(view: View) {
        getActivity<MainActivity>()?.startActivity(
            Intent(getActivity(), FragmentActivity::class.java)
        )
    }

    fun onClickDialog(view: View) {
        getActivity<MainActivity>()?.startActivity(
            Intent(getActivity(), DialogActivity::class.java)
        )
    }

    fun onClickPopup(view: View) {
        getActivity<MainActivity>()?.startActivity(
            Intent(getActivity(), PopupActivity::class.java)
        )
    }
}
