package com.shouzhong.base.frgm

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import com.shouzhong.base.act.BActivity
import com.shouzhong.bridge.FragmentStack

abstract class BViewModel : ViewModel(), LifecycleObserver {
    var uniqueId: String? = null

    open fun init() = Unit

    open fun onFragmentFirstVisible() = Unit

    open fun onFragmentVisibleChange(isVisible: Boolean) = Unit

    open fun onCreate(savedInstanceState: Bundle?) = Unit

    open fun onViewCreated(view: View, savedInstanceState: Bundle?) = Unit

    open fun onActivityCreated(savedInstanceState: Bundle?) = Unit

    open fun onStart() = Unit

    open fun onResume() = Unit

    open fun onPause() = Unit

    open fun onStop() = Unit

    open fun onDestroyView() = Unit

    open fun onDestroy() = Unit

    open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) = Unit

    open fun onConfigurationChanged(newConfig: Configuration) = Unit

    open fun onSaveInstanceState(outState: Bundle) = Unit

    inline fun <reified T : BFragment<*>> getFragment(): T = FragmentStack.getFragment(uniqueId) as T

    inline fun <reified T : BActivity<*>> getActivity(): T = getFragment<BFragment<*>>().activity as T

    inline fun <reified T : ViewDataBinding> getBinding(): T? =
        try {
            getFragment<BFragment<*>>().getBinding()
        } catch (e: Throwable) {
            null
        }
}