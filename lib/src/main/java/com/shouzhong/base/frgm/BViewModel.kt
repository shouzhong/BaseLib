package com.shouzhong.base.frgm

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle

abstract class BViewModel {
    internal var frgm: BFragment<*>? = null

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
}