package com.shouzhong.base.demo

import android.app.Application
import com.shouzhong.bridge.Bridge

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Bridge.init(this)
    }
}