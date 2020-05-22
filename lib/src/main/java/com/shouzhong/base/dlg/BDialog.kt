package com.shouzhong.base.dlg

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.shouzhong.base.util.getGenericClass
import com.shouzhong.bridge.FragmentStack

abstract class BDialog<T : BViewModel<*>>(val layoutId: Int) : DialogFragment() {
    var viewDataBinding: ViewDataBinding? = null
        private set
    private var vm: T? = null

    var showSwitch: MutableLiveData<Boolean>? = null
    var data: Any? = null

    override fun show(manager: FragmentManager, tag: String?) {
        super.show(manager, tag)
        if (showSwitch?.value != true) showSwitch?.value = true
    }

    override fun dismiss() {
        super.dismiss()
        if (showSwitch?.value != false) showSwitch?.value = false
    }

    override fun dismissAllowingStateLoss() {
        super.dismissAllowingStateLoss()
        if (showSwitch?.value != false) showSwitch?.value = false
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (showSwitch?.value != false) showSwitch?.value = false
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        if (showSwitch?.value != false) showSwitch?.value = false
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
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val attributes = dialog?.window?.attributes
        attributes?.gravity = Gravity.CENTER
        initAttributes(attributes)
        dialog?.window?.attributes = attributes
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
        dialog?.setOnCancelListener(null)
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

    open fun initAttributes(attr: WindowManager.LayoutParams?) = Unit
}