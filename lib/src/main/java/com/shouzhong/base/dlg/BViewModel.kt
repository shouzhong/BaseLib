package com.shouzhong.base.dlg

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.shouzhong.bridge.FragmentStack

abstract class BViewModel<T> : ViewModel(), LifecycleObserver {
    var uniqueId: String? = null
    var data: T? = null

    open fun init() = Unit

    open fun onCreate(savedInstanceState: Bundle?) = Unit

    open fun onViewCreated(view: View, savedInstanceState: Bundle?) = Unit

    open fun onActivityCreated(savedInstanceState: Bundle?) = Unit

    open fun onStart() = Unit

    open fun onResume() = Unit

    open fun onPause() = Unit

    open fun onStop() = Unit

    open fun onDestroyView() = Unit

    open fun onDestroy() = Unit

    open fun onSaveInstanceState(outState: Bundle) = Unit

    internal fun setData(data: Any?) {
        this.data = if (data == null) null else data as T
    }

    fun getLifecycleOwner(): LifecycleOwner = getDialog()

    inline fun <reified T : BDialog<*>> getDialog(): T = FragmentStack.getFragment(uniqueId) as T

    inline fun <reified T : ViewDataBinding> getBinding(): T? =
        try {
            getDialog<BDialog<*>>().getBinding()
        } catch (e: Throwable) {
            null
        }
}