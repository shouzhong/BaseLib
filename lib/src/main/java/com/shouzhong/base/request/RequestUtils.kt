package com.shouzhong.base.request

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.shouzhong.base.util.getApp
import com.shouzhong.base.util.hashCode
import com.shouzhong.bridge.ActivityStack

/**
 * 实现在任何地方调用startActivityForResult
 *
 */
class RequestUtils private constructor() {
    companion object {
        fun startActivityForResult(
            ctx: Context? = null,
            intent: Intent,
            callback: (resultCode: Int, data: Intent?) -> Unit,
            error: ((errorMessage: String) -> Unit)? = null
        ) {
            RequestUtils().apply {
                this.intent = intent
                this.callback = callback
                this.error = error
            }.start(ctx)
        }
    }

    private val uniqueId = hashCode(this)
    private var flag = false
    private lateinit var intent: Intent
    private lateinit var callback: (resultCode: Int, data: Intent?) -> Unit
    private var error: ((errorMessage: String) -> Unit)? = null

    private fun start(context: Context?) {
        val ctx = context ?: getApp()
        if (ctx is AppCompatActivity) {
            getFragment(ctx).put(uniqueId, intent, callback)
            return
        }
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.getBooleanExtra("flag", false) == true) {
                    flag = true
                    return
                }
                getApp().unregisterReceiver(this)
                callback.invoke(
                    intent?.getIntExtra("result_code", 0) ?: Activity.RESULT_CANCELED,
                    intent?.getParcelableExtra("data")
                )
            }
        }
        getApp().registerReceiver(
            receiver,
            IntentFilter("${getApp().packageName}.shouzhong.receiver.action.START_ACTIVITY_FOR_RESULT_$uniqueId")
        )
        ctx.startActivity(
            Intent(ctx, RequestActivity::class.java).apply {
                if (ctx !is Activity) addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra("unique_id", uniqueId)
                putExtra("data", intent)
            }
        )
        Handler().postDelayed({
            if (flag) return@postDelayed
            getApp().unregisterReceiver(receiver)
            error?.invoke("launcher failure")
        }, 500)
    }

    private fun getFragment(act: AppCompatActivity): RequestFragment {
        val manager = act.supportFragmentManager
        var frgm: RequestFragment? = manager.findFragmentByTag(RequestFragment.TAG) as RequestFragment?
        if (frgm == null) {
            frgm = RequestFragment()
            manager.beginTransaction().add(frgm, RequestFragment.TAG).commitAllowingStateLoss()
            manager.executePendingTransactions()
        }
        return frgm
    }
}