package com.shouzhong.base.rv

import android.content.ContextWrapper
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.shouzhong.base.act.BActivity
import com.shouzhong.base.dlg.BDialog
import com.shouzhong.base.frgm.BFragment
import com.shouzhong.base.popup.BPopup
import kotlinx.coroutines.CoroutineScope

open class BHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val data = MutableLiveData<T>()
    var dataList: DataList? = null
        internal set
    internal var viewDataBinding: ViewDataBinding? = null
    internal var lifecycleOwner: LifecycleOwner? = null

    open fun init() = Unit

    open fun onBind() = Unit

    fun <T : BActivity<*>> getActivity(): T? = try {
        itemView.context as T
    } catch (e: Throwable) {
        try {
            (itemView.context as ContextWrapper).baseContext as T
        } catch (e: Throwable) {
            null
        }
    }

    fun <T : ViewDataBinding> getBinding(): T? = try {
        viewDataBinding as? T
    } catch (e: Throwable) {
        null
    }

    fun getLifecycle(): LifecycleOwner? = lifecycleOwner

    fun getScope(): CoroutineScope? = try {
        when(lifecycleOwner) {
            is BActivity<*> -> (lifecycleOwner as BActivity<*>).getVm().getScope()
            is BFragment<*> -> (lifecycleOwner as BFragment<*>).getVm().getScope()
            is BDialog<*> -> (lifecycleOwner as BDialog<*>).getVm().getScope()
            is BPopup<*> -> (lifecycleOwner as BPopup<*>).getVm().getScope()
            else -> null
        }
    } catch (e: Throwable) {
        null
    }
}