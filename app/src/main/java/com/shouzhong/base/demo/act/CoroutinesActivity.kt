package com.shouzhong.base.demo.act

import android.os.Looper
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.shouzhong.base.act.BActivity
import com.shouzhong.base.act.BViewModel
import com.shouzhong.base.demo.R
import kotlinx.coroutines.*

class CoroutinesActivity : BActivity<CoroutinesViewModel>(R.layout.act_coroutines)

class CoroutinesViewModel : BViewModel() {
    fun onClickRunBlocking(v: View) {
        LogUtils.e("主线程id：${Thread.currentThread().id}")
        runBlocking {
            repeat(5) {
                LogUtils.e(System.currentTimeMillis(), "协程线程id：${Thread.currentThread().id}")
                delay(1000)
            }
        }
        LogUtils.e("协程执行结束")
    }

    fun onClickLaunch(v: View) {
        LogUtils.e("主线程id：${Thread.currentThread().id}")
        val job = GlobalScope.launch {
            delay(5000)
            LogUtils.e(System.currentTimeMillis(), "协程线程id：${Thread.currentThread().id}")
        }
        LogUtils.e("协程执行结束")
    }

    fun onClickSuspend(v: View) {
        LogUtils.e(System.currentTimeMillis())
        GlobalScope.launch {
            val token = getToken()
            val userInfo = getUserInfo(token)
            setUserInfo(userInfo)
        }
        repeat(8){
            LogUtils.e("主线程执行$it")
        }
    }

    fun onClickAsync(v: View) {
        LogUtils.e(System.currentTimeMillis())
        GlobalScope.launch {
            val result1 = GlobalScope.async {
                getResult1()
            }
            val result2 = GlobalScope.async {
                getResult2()
            }
            val result = result1.await() + result2.await()
            LogUtils.e(System.currentTimeMillis(), result)
        }
    }

    private fun setUserInfo(userInfo: String) {
        LogUtils.e(System.currentTimeMillis(), userInfo)
    }

    private suspend fun getToken(): String {
        delay(2000)
        return "token"
    }

    private suspend fun getUserInfo(token: String): String {
        delay(2000)
        return "$token - userInfo"
    }

    private suspend fun getResult1(): Int {
        delay(3000)
        return 1
    }

    private suspend fun getResult2(): Int {
        delay(4000)
        return 2
    }
}