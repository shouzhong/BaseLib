package com.shouzhong.base.rv

import android.view.View
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView

class BHolder<T>(itemView: View, dataList: DataList) : RecyclerView.ViewHolder(itemView) {
    val data = ObservableField<T>()
    val dataList = dataList

    open fun onBind() = Unit
}