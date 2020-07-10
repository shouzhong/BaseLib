package com.shouzhong.base.reflect

import java.lang.reflect.Field

class RefDouble(cls: Class<*>, field: Field) {
    private val field = cls.getDeclaredField(field.name).apply {
        isAccessible = true
    }

    fun get(obj: Any): Double {
        return field.getDouble(obj)
    }

    fun set(obj: Any, value: Double) {
        field.setDouble(obj, value)
    }
}