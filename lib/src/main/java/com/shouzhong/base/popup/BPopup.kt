package com.shouzhong.base.popup

import androidx.databinding.ViewDataBinding
import com.shouzhong.base.util.getGenericClass

class BPopup<T : BViewModel<*>>(val layoutId: Int) : PopupFragment() {
    private var binding: ViewDataBinding? = null
    private var vm: T? = null

    var data: Any? = null

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