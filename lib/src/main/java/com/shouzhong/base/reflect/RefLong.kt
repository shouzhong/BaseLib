package com.shouzhong.base.reflect

import java.lang.reflect.Field

class RefLong(cls: Class<*>, field: Field) {
    private val field = cls.getDeclaredField(field.name).apply {
        isAccessible = true
    }

    fun get(obj: Any): Long {
        return field.getLong(obj)
    }

    fun set(obj: Any, value: Long) {
        field.setLong(obj, value)
    }
}