package com.shouzhong.base.util

import android.app.Application
import com.shouzhong.bridge.Bridge

class BUtils {
    companion object {
        fun init(app: Application) {
            bApp = app
            Bridge.init(app)
        }
    }
}