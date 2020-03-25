package com.shouzhong.base.rv

import androidx.databinding.ObservableInt
import com.drakeet.multitype.MultiTypeAdapter

class DataList : ArrayList<Any>() {
    var adapter: MultiTypeAdapter? = null
    var isRefresh: Boolean = true
    val length = ObservableInt(size)

    override fun add(element: Any): Boolean {
        add(size, element)
        return true
    }

    override fun add(index: Int, element: Any) {
        if (index < 0 || index > size) return
        super.add(index, element)
        if (isRefresh) adapter?.notifyItemInserted(index)
        length.set(size)
    }

    override fun addAll(elements: Collection<Any>): Boolean {
        return addAll(size, elements)
    }

    override fun addAll(index: Int, elements: Collection<Any>): Boolean {
        if (elements.isEmpty() || index < 0 || index > size) return false
        val boo = super.addAll(index, elements)
        if (boo && isRefresh) adapter?.notifyItemRangeInserted(index, elements.size)
        length.set(size)
        return boo
    }

    override fun set(index: Int, element: Any): Any {
        if (index < 0 || index >= size) return element
        val old = super.set(index, element)
        if (isRefresh) adapter?.notifyItemChanged(index)
        return old
    }

    override fun remove(element: Any): Boolean {
        val index = indexOf(element)
        if (index == -1) return false
        removeAt(index)
        return true
    }

    override fun removeAt(index: Int): Any {
        if (index < 0 || index >= size) return false
        val old = super.removeAt(index)
        if (isRefresh) adapter?.notifyItemRemoved(index)
        length.set(size)
        return old
    }

    override fun removeAll(elements: Collection<Any>): Boolean {
        var boo = false
        elements.forEach {
            if (remove(it)) boo = true
        }
        return boo
    }

    override fun clear() {
        if (isEmpty()) return
        super.clear()
        adapter?.notifyDataSetChanged()
        length.set(size)
    }

    fun notifyItemChanged(element: Any) {
        val index = indexOf(element)
        if (index == -1) return
        notifyItemChangedAt(index)
    }

    fun notifyItemChangedAt(index: Int) = adapter?.notifyItemChanged(index)

    fun notifyDataSetChanged() = adapter?.notifyDataSetChanged()
}