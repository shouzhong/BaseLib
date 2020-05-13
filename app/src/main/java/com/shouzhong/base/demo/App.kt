package com.shouzhong.base.demo

import android.app.Application
import com.shouzhong.base.util.BUtils

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        BUtils.init(this)
    }
}