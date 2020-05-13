package com.shouzhong.base.popup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.shouzhong.base.util.getGenericClass
import com.shouzhong.bridge.FragmentStack

open class BPopup<T : BViewModel<*>>(val layoutId: Int) : PopupFragment() {
    var viewDataBinding: ViewDataBinding? = null
        private set
    private var vm: T? = null

    var showSwitch: ObservableBoolean? = null
    var data: BPopupBean? = null

    override fun show(
        manager: FragmentManager,
        tag: String,
        showStyle: String,
        view: View,
        gravity: Int,
        xoff: Int,
        yoff: Int
    ) {
        super.show(manager, tag, showStyle, view, gravity, xoff, yoff)
        shadowAlpha = data?.shadowAlpha?.get() ?: 1f
        showSwitch?.set(true)
    }

    override fun dismiss() {
        super.dismiss()
        showSwitch?.set(false)
        data?.relatedView?.set(null)
    }

    override fun dismissAllowingStateLoss() {
        super.dismissAllowingStateLoss()
        showSwitch?.set(false)
        data?.relatedView?.set(null)
    }

    override fun onDismiss() {
        super.onDismiss()
        showSwitch?.set(false)
        data?.relatedView?.set(null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(getVm())
        vm?.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        viewDataBinding?.javaClass?.getDeclaredMethod("setVm", getGenericClass<T>(0))?.apply {
            isAccessible = true
            invoke(viewDataBinding, vm)
        }
        return viewDataBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm?.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vm?.onActivityCreated(savedInstanceState)
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

    override fun onDestroyView() {
        super.onDestroyView()
        vm?.onDestroyView()
        viewDataBinding?.unbind()
        viewDataBinding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        vm?.onDestroy()
        vm = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        vm?.onSaveInstanceState(outState)
    }

    fun getVm(): T {
        if (vm != null) return vm!!
        vm = ViewModelProvider(this).get(getGenericClass<T>(0)!!)
        vm?.uniqueId = FragmentStack.getUniqueId(this)
        vm?.setData(data)
        vm?.init()
        return vm!!
    }

    inline fun <reified T : ViewDataBinding> getBinding(): T? = when(viewDataBinding) {
        is T -> viewDataBinding as T
        else -> null
    }
}