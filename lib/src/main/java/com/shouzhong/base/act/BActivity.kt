package com.shouzhong.base.act

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.shouzhong.base.util.getGenericClass
import com.shouzhong.base.util.initDialog
import com.shouzhong.base.util.initPopup

abstract class BActivity<T : BViewModel>(val layoutId: Int) : AppCompatActivity() {
    var binding: ViewDataBinding? = null
        private set
    private var vm: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
//        savedInstanceState?.putParcelable("android:support:fragments", null)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        val cls: Class<T> = getGenericClass( 0)
        binding?.javaClass?.getDeclaredMethod("setVm", cls)?.apply {
            isAccessible = true
            invoke(binding, getVm())
        }
        vm?.onCreate(savedInstanceState)
    }

    override fun onRestart() {
        super.onRestart()
        vm?.onRestart()
    }

    override fun onStart() {
        super.onStart()
        vm?.onStart()
    }

    override fun onResume() {
        super.onResume()
        vm?.onResume()
    }

    override fun onPause() {
        super.onPause()
        vm?.onPause()
    }

    override fun onStop() {
        super.onStop()
        vm?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        vm?.onDestroy()
        vm = null
        binding?.unbind()
        binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        vm?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        vm?.onNewIntent(intent)
    }

    override fun onBackPressed() {
        if (vm?.onBackPressed() != true) super.onBackPressed()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (vm?.onKeyDown(keyCode, event) != true) super.onKeyDown(keyCode, event) else true
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        vm?.onConfigurationChanged(newConfig)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        vm?.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        vm?.onRestoreInstanceState(savedInstanceState)
    }

    fun getVm(): T {
        if (vm != null) return vm!!
        val cls: Class<T> = getGenericClass(0)
        vm = cls.newInstance().apply {
            act = this@BActivity
        }
        vm?.initDialog(this)
        vm?.initPopup(this)
        vm?.init()
        return vm!!
    }
}