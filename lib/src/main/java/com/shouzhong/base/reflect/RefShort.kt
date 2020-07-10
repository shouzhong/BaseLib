package com.shouzhong.base.reflect

import java.lang.reflect.Field

class RefShort(cls: Class<*>, field: Field) {
    private val field = cls.getDeclaredField(field.name).apply {
        isAccessible = true
    }

    fun get(obj: Any): Short {
        return field.getShort(obj)
    }

    fun set(obj: Any, value: Short) {
        field.setShort(obj, value)
    }
}