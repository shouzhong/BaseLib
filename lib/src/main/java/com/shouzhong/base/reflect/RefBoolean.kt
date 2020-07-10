package com.shouzhong.base.reflect

import java.lang.reflect.Field

class RefBoolean(cls: Class<*>, field: Field) {
    private val field = cls.getDeclaredField(field.name).apply {
        isAccessible = true
    }

    fun get(obj: Any): Boolean {
        return field.getBoolean(obj)
    }

    fun set(obj: Any, value: Boolean) {
        field.setBoolean(obj, value)
    }
}