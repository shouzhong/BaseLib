package com.shouzhong.base.demo.frgm

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.blankj.utilcode.util.LogUtils
import com.shouzhong.base.demo.R
import com.shouzhong.base.demo.rv.TestBinder1
import com.shouzhong.base.frgm.BFragment
import com.shouzhong.base.frgm.BViewModel
import com.shouzhong.base.rv.DataList

class TestFragment : BFragment<TestViewModel>(R.layout.frgm_test)

class TestViewModel : BViewModel() {
    val dataList = DataList().apply {
        adapter.run {
            register(String::class, TestBinder1())
        }
    }

    override fun onFragmentFirstVisible() {
        for (i in 0..30)  {
            dataList.add("${dataList.size}")
        }
    }

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