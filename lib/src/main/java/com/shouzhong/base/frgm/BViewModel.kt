package com.shouzhong.base.frgm

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import com.shouzhong.base.act.BActivity

abstract class BViewModel {
    var frgm: BFragment<*>? = null

    open fun init() = Unit

    open fun onFragmentFirstVisible() = Unit

    open fun onFragmentVisibleChange(isVisible: Boolean) = Unit

    open fun onCreated(savedInstanceState: Bundle?) = Unit

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

    inline fun <reified T : BFragment<*>> getFragment(): T? = when(frgm) {
        is T -> frgm as T
        else -> null
    }

    inline fun <reified T : BActivity<*>> getActivity(): T? = when(frgm?.activity) {
        is T -> frgm?.activity as  T
        else -> null
    }
}