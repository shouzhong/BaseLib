package com.shouzhong.base.request

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.shouzhong.base.util.getApp
import com.shouzhong.base.util.hashCode

class RequestUtils private constructor() {
    companion object {
        fun startActivityForResult(
            ctx: Context? = null,
            intent: Intent,
            callback: (resultCode: Int, data: Intent?) -> Unit
        ) {
            RequestUtils().apply {
                this.intent = intent
                this.callback = callback
            }.start(ctx)
        }
    }

    private val uniqueId = hashCode(this)
    private lateinit var intent: Intent
    private lateinit var callback: (resultCode: Int, data: Intent?) -> Unit

    private fun start(ctx: Context?) {
        getApp().registerReceiver(
            object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    getApp().unregisterReceiver(this)
                    callback.invoke(
                        intent?.getIntExtra("result_code", 0) ?: Activity.RESULT_CANCELED,
                        intent?.getParcelableExtra("data")
                    )
                }
            },
            IntentFilter("${getApp().packageName}.shouzhong.receiver.action.START_ACTIVITY_FOR_RESULT_$uniqueId")
        )
        (ctx ?: getApp()).startActivity(
            Intent(ctx ?: getApp(), RequestActivity::class.java).apply {
                if (ctx == null || ctx !is Activity) addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra("unique_id", uniqueId)
                putExtra("data", intent)
            }
        )
    }
}