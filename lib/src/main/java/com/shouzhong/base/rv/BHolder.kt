package com.shouzhong.base.rv

import android.content.ContextWrapper
import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.shouzhong.base.act.BActivity

open class BHolder<T>(itemView: View, dataList: DataList) : RecyclerView.ViewHolder(itemView) {
    val data = ObservableField<T>()
    val dataList = dataList

    var viewDataBinding: ViewDataBinding? = null
        internal set

    open fun onBind() = Unit

    inline fun <reified T : BActivity<*>> getActivity(): T? = when (itemView.context) {
        is T -> itemView.context as T
        is ContextWrapper -> if ((itemView.context as ContextWrapper).baseContext is T) (itemView.context as ContextWrapper).baseContext as T else null
        else -> null
    }

    inline fun <reified T : ViewDataBinding> getBinding(): T? = when(viewDataBinding) {
        is T -> viewDataBinding as T
        else -> null
    }
}