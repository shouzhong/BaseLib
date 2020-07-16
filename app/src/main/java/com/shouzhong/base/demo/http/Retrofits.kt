package com.shouzhong.base.demo.http

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

class RetrofitCoroutineDSL<T> {
    internal var api: (suspend () -> Call<T>)? = null
    internal var onSuccess: ((T) -> Unit)? = null
        private set
    internal var onFail: ((Throwable?) -> Unit)? = null
        private set
    internal var onComplete: (() -> Unit)? = null
        private set

    fun api(block: suspend () -> Call<T>) {
        this.api = block
    }

    fun onSuccess(block: (T) -> Unit) {
        this.onSuccess = block
    }

    fun onFail(block: (Throwable?) -> Unit) {
        this.onFail = block
    }

    fun onComplete(block: () -> Unit) {
        this.onComplete = block
    }

    internal fun clear() {
        api = null
        onSuccess = null
        onFail = null
        onComplete = null
    }
}

fun <T> CoroutineScope.retrofit(block: RetrofitCoroutineDSL<T>.() -> Unit): Job {
    return this.launch(Dispatchers.Main) {
        val coroutine = RetrofitCoroutineDSL<T>().apply(block)
        var throwable: Throwable? = null
        coroutine.api?.invoke()?.let { call ->
            val deferred = async(Dispatchers.IO) {
                try {
                    call.execute()
                } catch (e: Throwable) {
                    throwable = e
                    null
                }
            }
            deferred.invokeOnCompletion {
                if (deferred.isCancelled) {
                    call.cancel()
                    coroutine.clear()
                }
            }
            val response = deferred.await()
            if (response == null) {
                try {
                    coroutine.onFail?.invoke(throwable)
                } catch (e: Throwable) {}
            } else {
                try {
                    coroutine.onSuccess?.invoke(response.body()!!)
                } catch (e: Throwable) {
                    try {
                        coroutine.onFail?.invoke(e)
                    } catch (e: Throwable) { }
                }
            }
            try {
                coroutine.onComplete?.invoke()
            } catch (e: Throwable) {}
        }
    }
}

class TaskCoroutineDSL<T> {
    internal var apply: (suspend () -> T)? = null
        private set
    internal var onSuccess: ((T) -> Unit)? = null
        private set
    internal var onFail: ((Throwable?) -> Unit)? = null
        private set
    internal var onComplete: (() -> Unit)? = null
        private set

    fun apply(block: suspend () -> T) {
        this.apply = block
    }

    fun onSuccess(block: (T) -> Unit) {
        this.onSuccess = block
    }

    fun onFail(block: (Throwable?) -> Unit) {
        this.onFail = block
    }

    fun onComplete(block: () -> Unit) {
        this.onComplete = block
    }

    internal fun clear() {
        apply = null
        onSuccess = null
        onFail = null
        onComplete = null
    }
}

fun <T> CoroutineScope.task(block: TaskCoroutineDSL<T>.() -> Unit): Job {
    return this.launch(Dispatchers.Main) {
        val coroutine = TaskCoroutineDSL<T>().apply(block)
        var throwable: Throwable? = null
        coroutine.apply?.let { apply ->
            val deferred = async(Dispatchers.IO) {
                try {
                    apply.invoke()
                } catch (e: Throwable) {
                    throwable = e
                    null
                }
            }
            deferred.invokeOnCompletion {
                if (deferred.isCancelled) {
                    coroutine.clear()
                }
            }
            val result = deferred.await()
            if (result == null) {
                try {
                    coroutine.onFail?.invoke(throwable)
                } catch (e: Throwable) {}
            } else {
                try {
                    coroutine.onSuccess?.invoke(result)
                } catch (e: Throwable) {
                    try {
                        coroutine.onFail?.invoke(e)
                    } catch (e: Throwable) {}
                }
            }
            try {
                coroutine.onComplete?.invoke()
            } catch (e: Throwable) {}
        }
    }
}