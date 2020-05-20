package com.shouzhong.base.request

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shouzhong.base.util.getApp

class RequestActivity : AppCompatActivity() {
    private var uniqueId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uniqueId = intent.getIntExtra("unique_id", 0)
        getApp().sendBroadcast(
            Intent().apply {
                action = "${getApp().packageName}.shouzhong.receiver.action.START_ACTIVITY_FOR_RESULT_$uniqueId"
                putExtra("flag", true)
            }
        )
        startActivityForResult(
            intent.getParcelableExtra("data")!!,
            uniqueId and 0xffff
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == (uniqueId and 0x0000ffff)) {
            getApp().sendBroadcast(
                Intent().apply {
                    action = "${getApp().packageName}.shouzhong.receiver.action.START_ACTIVITY_FOR_RESULT_$uniqueId"
                    putExtra("result_code", resultCode)
                    putExtra("data", data)
                }
            )
            finish()
            return
        }
    }
}