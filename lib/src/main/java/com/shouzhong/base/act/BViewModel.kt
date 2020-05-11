package com.shouzhong.base.act

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import com.shouzhong.bridge.ActivityStack

abstract class BViewModel : ViewModel(), LifecycleObserver {
    var uniqueId: String? = null

    open fun init() = Unit

    open fun onCreate(savedInstanceState: Bundle?) = Unit

    open fun onRestart() = Unit

    open fun onStart() = Unit

    open fun onResume() = Unit

    open fun onPause() = Unit

    open fun onStop() = Unit

    open fun onDestroy() = Unit

    open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) = Unit

    open fun onNewIntent(intent: Intent?) = Unit

    open fun onBackPressed(): Boolean = false

    open fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean = false

    open fun onConfigurationChanged(newConfig: Configuration) = Unit

    open fun onSaveInstanceState(outState: Bundle) = Unit

    open fun onRestoreInstanceState(savedInstanceState: Bundle) = Unit

    inline fun <reified T : BActivity<*>> getActivity(): T = ActivityStack.getActivity(uniqueId) as T
}