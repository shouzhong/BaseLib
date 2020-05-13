package com.shouzhong.base.demo

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import com.shouzhong.base.demo.act.MainActivity
import com.shouzhong.base.util.getApp
import com.shouzhong.base.util.startActivity

class TestService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        Handler(Looper.getMainLooper()).postDelayed({
            Intent(getApp(), MainActivity::class.java).startActivity()
        }, 5000)
    }
}