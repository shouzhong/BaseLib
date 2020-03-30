package com.shouzhong.base.popup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import com.shouzhong.base.util.getGenericClass

open class BPopup<T : BViewModel<*>>(val layoutId: Int) : PopupFragment() {
    private var binding: ViewDataBinding? = null
    private var vm: T? = null

    var showSwitch: ObservableBoolean? = null
    var data: BPopupBean? = null

    override fun showAsDropDown(
        manager: FragmentManager,
        tag: String,
        anchor: View,
        gravity: Int,
        xoff: Int,
        yoff: Int
    ) {
        super.showAsDropDown(manager, tag, anchor, gravity, xoff, yoff)
        shadowAlpha = data?.shadowAlpha?.get() ?: 1f
        showSwitch?.set(true)
    }

    override fun showAtLocation(
        manager: FragmentManager,
        tag: String,
        parent: View,
        gravity: Int,
        x: Int,
        y: Int
    ) {
        super.showAtLocation(manager, tag, parent, gravity, x, y)
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        val cls: Class<T> = getGenericClass(0)
        binding?.javaClass?.getDeclaredMethod("setVm", cls)?.apply {
            isAccessible = true
            invoke(binding, getVm())
        }
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vm?.onActivityCreated(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        vm?.onSaveInstanceState(outState)
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
        vm?.onDestroy()
        vm?.popup = null
        vm = null
        binding?.unbind()
        binding = null
    }

    fun getVm(): T {
        if (vm != null) return vm!!
        val cls: Class<T> = getGenericClass(0)
        vm = cls.newInstance().apply {
            popup = this@BPopup
            setData(this@BPopup.data)
        }
        vm?.init()
        return vm!!
    }
}