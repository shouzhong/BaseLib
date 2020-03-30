package com.shouzhong.base.dlg

import android.os.Bundle

abstract class BViewModel<T> {
    var dlg: BDialog<*>? = null
    var data: T? = null

    open fun init() = Unit

    open fun onActivityCreated(savedInstanceState: Bundle?) = Unit

    open fun onSaveInstanceState(outState: Bundle) = Unit

    open fun onStart() = Unit

    open fun onResume() = Unit

    open fun onPause() = Unit

    open fun onStop() = Unit

    open fun onDestroy() = Unit

    internal fun setData(data: Any?) {
        this.data = if (data == null) null else data as T
    }

    inline fun <reified T : BDialog<*>> getDialog(): T? = when(dlg) {
        is T -> dlg as T
        else -> null
    }
}