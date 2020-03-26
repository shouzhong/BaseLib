package com.shouzhong.base.rv

import android.content.ContextWrapper
import android.view.View
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import com.shouzhong.base.act.BActivity

open class BHolder<T>(itemView: View, dataList: DataList) : RecyclerView.ViewHolder(itemView) {
    val act: BActivity<*>? = when {
        itemView.context is BActivity<*> -> itemView.context as BActivity<*>
        itemView.context is ContextWrapper && (itemView.context as ContextWrapper).baseContext is BActivity<*> -> (itemView.context as ContextWrapper).baseContext as BActivity<*>
        else -> null
    }

    val data = ObservableField<T>()
    val dataList = dataList

    open fun onBind() = Unit

    inline fun <reified T : BActivity<*>> getActivity(): T? = when (act) {
        is T -> act
        else -> null
    }
}