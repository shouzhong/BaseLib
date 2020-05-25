package com.shouzhong.base.demo

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.LogUtils

/**
 * 双向绑定
 *
 */
object Binding {
    @JvmStatic
    @BindingAdapter("tvText")
    fun setTextViewText(
        tv: TextView,
        newValue: CharSequence?
    ) {
        LogUtils.e(tv.text, newValue)
        if (TextUtils.equals(tv.text, newValue)) return
        tv.text = newValue
    }

    @JvmStatic
    @InverseBindingAdapter(
        attribute = "tvText",
        event = "tvTextChanged"
    )
    fun getTextViewText(tv: TextView): CharSequence? = tv.text

    @JvmStatic
    @BindingAdapter(
        "tvTextChanged",
        requireAll = false
    )
    fun setTextViewTextChanged(
        tv: TextView,
        listener: InverseBindingListener?
    ) {
        if (listener == null) return
        tv.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = Unit

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = listener.onChange()
        })
    }

    @JvmStatic
    @BindingAdapter("srlRefresh")
    fun setSwipeRefreshLayoutRefresh(
        srl: SwipeRefreshLayout,
        newValue: Boolean
    ) {
        if (srl.isRefreshing == newValue) return
        srl.isRefreshing = newValue
    }

    @JvmStatic
    @BindingAdapter(
        "srlRefreshChanged",
        requireAll = false
    )
    fun setSwipeRefreshLayoutRefreshChanged(
        srl: SwipeRefreshLayout,
        listener: InverseBindingListener?
    ) {
        if (listener == null) return
        srl.setOnRefreshListener {
            listener.onChange()
        }
    }

    @JvmStatic
    @InverseBindingAdapter(
        attribute = "srlRefresh",
        event = "srlRefreshChanged"
    )
    fun getSwipeRefreshLayoutRefresh(srl: SwipeRefreshLayout): Boolean = srl.isRefreshing
}