package com.shouzhong.base.reflect

import java.lang.reflect.Field

class RefInt(cls: Class<*>, field: Field) {
    private val field = cls.getDeclaredField(field.name).apply {
        isAccessible = true
    }

    fun get(obj: Any): Int {
        return field.getInt(obj)
    }

    fun set(obj: Any, value: Int) {
        field.setInt(obj, value)
    }
}