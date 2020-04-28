package com.shouzhong.base.demo.act

import android.os.Bundle
import com.blankj.utilcode.util.LogUtils
import com.shouzhong.base.act.BActivity
import com.shouzhong.base.act.BViewModel
import com.shouzhong.base.demo.R
import com.shouzhong.base.util.getActivities
import com.shouzhong.base.util.getTopActivity

class FragmentActivity  : BActivity<FragmentViewModel>(R.layout.act_fragment)

class FragmentViewModel : BViewModel() {
    private fun log(name: String) {
        LogUtils.e(StringBuffer(name).apply {
            for (act in getActivities()) {
                append("\n").append(act.javaClass?.name)
            }
            append("\ntopAct:").append(getTopActivity()?.javaClass?.name)
        })
    }

    override fun init() {
        log("init")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        log("onCreate")
    }

    override fun onStart() {
        log("onStart")
    }

    override fun onResume() {
        log("onResume")
    }

    override fun onPause() {
        log("onPause")
    }

    override fun onStop() {
        log("onStop")
    }

    override fun onDestroy() {
        log("onDestroy")
    }
}