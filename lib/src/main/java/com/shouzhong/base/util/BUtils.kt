package com.shouzhong.base.util

import android.app.Application
import com.shouzhong.bridge.Bridge

class BUtils {
    companion object {

        /**
         * 初始化，在Application的onCreate中调用
         *
         */
        fun init(app: Application) {
            bApp = app
            Bridge.init(app)
        }
    }
}