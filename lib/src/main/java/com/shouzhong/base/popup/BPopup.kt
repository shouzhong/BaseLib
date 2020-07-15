package com.shouzhong.base.popup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.shouzhong.base.util.getGenericClass
import com.shouzhong.bridge.FragmentStack

/**
 * PopupWindow基类
 *
 */
open class BPopup<T : BViewModel<*>>(val layoutId: Int) : PopupFragment() {
    private var viewDataBinding: ViewDataBinding? = null
    private var vm: T? = null

    var showSwitch: MutableLiveData<Boolean>? = null
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
        shadowAlpha = data?.shadowAlpha ?: 1f
        if (showSwitch?.value != true) showSwitch?.value = true
    }

    override fun dismiss() {
        super.dismiss()
        if (showSwitch?.value != false) showSwitch?.value = false
        data?.relatedView = null
    }

    override fun dismissAllowingStateLoss() {
        super.dismissAllowingStateLoss()
        if (showSwitch?.value != false) showSwitch?.value = false
        data?.relatedView = null
    }

    override fun onDismiss() {
        super.onDismiss()
        if (showSwitch?.value != false) showSwitch?.value = false
        data?.relatedView = null
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
        viewDataBinding?.lifecycleOwner = this
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

    fun <T : ViewDataBinding> getBinding(): T? = try {
        viewDataBinding as? T
    } catch (e: Throwable) {
        null
    }
}