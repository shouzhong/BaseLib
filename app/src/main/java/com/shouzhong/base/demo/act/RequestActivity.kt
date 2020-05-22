package com.shouzhong.base.demo.act

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.shouzhong.base.act.BActivity
import com.shouzhong.base.act.BViewModel
import com.shouzhong.base.demo.R

class RequestActivity : BActivity<RequestViewModel>(R.layout.act_request)

class RequestViewModel : BViewModel() {
    val text = MutableLiveData<CharSequence?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        text.value = getActivity<RequestActivity>().intent?.getStringExtra("data")
    }

    fun onClickSetResult(v: View) {
        getActivity<RequestActivity>().run {
            setResult(
                Activity.RESULT_OK,
                Intent().apply {
                    putExtra("data", "这是来自RequestActivity的数据")
                }
            )
            finish()
        }
    }
}