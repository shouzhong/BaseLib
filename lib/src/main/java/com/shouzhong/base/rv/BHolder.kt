package com.shouzhong.base.rv

import android.content.ContextWrapper
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.shouzhong.base.act.BActivity

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
}