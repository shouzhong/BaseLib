package com.shouzhong.base.reflect

import java.lang.reflect.Field

class RefFloat(cls: Class<*>, field: Field) {
    private val field = cls.getDeclaredField(field.name).apply {
        isAccessible = true
    }

    fun get(obj: Any): Float {
        return field.getFloat(obj)
    }

    fun set(obj: Any, value: Float) {
        field.setFloat(obj, value)
    }
}