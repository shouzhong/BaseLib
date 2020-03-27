package com.shouzhong.base.popup

abstract class BViewModel<T> {
    var popup: BPopup<*>? = null
    var data: T? = null

    open fun init() = Unit

    internal fun setData(data: Any?) {
        this.data = if (data == null) null else data as T
    }

    inline fun <reified T : BPopup<*>> getPopupWindow(): T? = when(popup) {
        is T -> popup as T
        else -> null
    }
}