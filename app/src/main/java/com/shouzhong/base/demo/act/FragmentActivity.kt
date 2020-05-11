package com.shouzhong.base.demo.act

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.blankj.utilcode.util.LogUtils
import com.shouzhong.base.act.BActivity
import com.shouzhong.base.act.BViewModel
import com.shouzhong.base.demo.R

class FragmentActivity  : BActivity<FragmentViewModel>(R.layout.act_fragment)

class FragmentViewModel : BViewModel() {
    override fun init() {
        LogUtils.e("init")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun a() {
        LogUtils.e("onCreate1")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun b() {
        LogUtils.e("onStart1")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun c() {
        LogUtils.e("onResume1")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun d() {
        LogUtils.e("onPause1")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun e() {
        LogUtils.e("onStop1")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun f() {
        LogUtils.e("onDestroy1")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        LogUtils.e("onCreate2")
    }

    override fun onStart() {
        LogUtils.e("onStart2")
    }

    override fun onResume() {
        LogUtils.e("onResume2")
    }

    override fun onPause() {
        LogUtils.e("onPause2")
    }

    override fun onStop() {
        LogUtils.e("onStop2")
    }

    override fun onDestroy() {
        LogUtils.e("onDestroy2")
    }
}