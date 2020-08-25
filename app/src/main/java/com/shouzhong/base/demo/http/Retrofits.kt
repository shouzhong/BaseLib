package com.shouzhong.base.demo.http

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitCreator {
    companion object {
        private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://ccdcapi.alipay.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        fun <T> create(cls: Class<T>): T = retrofit.create(cls)
    }
}
