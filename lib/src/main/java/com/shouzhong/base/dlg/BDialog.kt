package com.shouzhong.base.dlg

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.shouzhong.base.util.*
import com.shouzhong.bridge.FragmentStack

abstract class BDialog<T : BViewModel<*>>(val layoutId: Int) : DialogFragment() {
    var binding: ViewDataBinding? = null
        private set
    private var vm: T? = null

    var showSwitch: ObservableBoolean? = null
    var data: Any? = null

    override fun show(manager: FragmentManager, tag: String?) {
        super.show(manager, tag)
        showSwitch?.set(true)
    }

    override fun dismiss() {
        super.dismiss()
        showSwitch?.set(false)
    }

    override fun dismissAllowingStateLoss() {
        super.dismissAllowingStateLoss()
        showSwitch?.set(false)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        showSwitch?.set(false)
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        showSwitch?.set(false)
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
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding?.javaClass?.getDeclaredMethod("setVm", getGenericClass<T>(0))?.apply {
            isAccessible = true
            invoke(binding, vm)
        }
        return binding?.root
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
        binding?.unbind()
        binding = null
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

    open fun initAttributes(attr: WindowManager.LayoutParams?) = Unit
}