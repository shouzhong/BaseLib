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
import com.shouzhong.base.util.getGenericClass

abstract class BDialog<T : BViewModel<*>>(val layoutId: Int) : DialogFragment() {
    private var binding: ViewDataBinding? = null
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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        showSwitch?.set(false)
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        showSwitch?.set(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        val cls: Class<T> = getGenericClass(this, 0)
        binding?.javaClass?.getDeclaredMethod("setVm", cls)?.apply {
            isAccessible = true
            invoke(binding, getVm())
        }
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        view?.post {
            val attributes = dialog?.window?.attributes
            attributes?.gravity = Gravity.CENTER
            initAttributes(attributes)
            dialog?.window?.attributes = attributes
        }
        getVm().onActivityCreated(savedInstanceState)
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
        vm = null
        binding?.unbind()
        binding = null
    }

    fun getVm(): T {
        if (vm != null) return vm!!
        val cls: Class<T> = getGenericClass(this, 0)
        vm = cls.newInstance().apply {
            dialog = this@BDialog
            setData(this@BDialog.data)
        }
        vm?.init()
        return vm!!
    }

    open fun initAttributes(attr: WindowManager.LayoutParams?) = Unit
}