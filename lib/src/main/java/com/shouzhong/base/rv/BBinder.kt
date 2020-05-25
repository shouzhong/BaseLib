package com.shouzhong.base.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.drakeet.multitype.ItemViewBinder
import com.shouzhong.base.util.*
import java.lang.Exception

open class BBinder<T, VH : BHolder<T>>(
    val lifecycleOwner: LifecycleOwner,
    val layoutId: Int
) : ItemViewBinder<T, VH>() {
    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): VH {
        val binding: ViewDataBinding = DataBindingUtil.inflate(inflater, layoutId, parent, false)
        binding.lifecycleOwner = lifecycleOwner
        return getGenericClass<VH>(1)?.let { cls ->
            cls.getDeclaredConstructor(View::class.java)
                .newInstance(binding.root).also { holder ->
                    holder.dataList = adapter.items as DataList
                    holder.viewDataBinding = binding
                    holder.lifecycleOwner = lifecycleOwner
                    holder.init()
                    binding.javaClass.getDeclaredMethod("setHolder", cls).run {
                        isAccessible = true
                        invoke(binding, holder)
                    }
                    if (inflater.context is AppCompatActivity) {
                        holder.initDialog(inflater.context as AppCompatActivity, lifecycleOwner)
                        holder.initPopup(inflater.context as AppCompatActivity, lifecycleOwner)
                    }
                }
        } ?: throw Exception("VH is null")
    }

    override fun onBindViewHolder(holder: VH, item: T) {
        holder.data.value = item
        holder.onBind()
    }
}