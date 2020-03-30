package com.shouzhong.base.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.drakeet.multitype.ItemViewBinder
import com.shouzhong.base.util.*

open class BBinder<T, VH : BHolder<T>>(val layoutId: Int) : ItemViewBinder<T, VH>() {
    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): VH {
        val binding: ViewDataBinding = DataBindingUtil.inflate(inflater, layoutId, parent, false)
        val cls: Class<VH> = getGenericClass(1)
        val holder: VH = cls.getDeclaredConstructor(View::class.java, DataList::class.java).newInstance(binding.root, adapter.items as DataList)
        binding.javaClass.getDeclaredMethod("setHolder", cls)?.run {
            isAccessible = true
            invoke(binding, holder)
        }
        if (inflater.context is AppCompatActivity) {
            holder.initDialog(inflater.context as AppCompatActivity)
            holder.initPopup(inflater.context as AppCompatActivity)
        }
        return holder
    }

    override fun onBindViewHolder(holder: VH, item: T) {
        holder.data.set(null)
        holder.data.set(item)
        holder.onBind()
    }
}