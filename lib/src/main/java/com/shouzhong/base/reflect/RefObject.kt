package com.shouzhong.base.reflect

import java.lang.reflect.Field

class RefObject<T>(cls: Class<*>, field: Field) {
    private val field = cls.getDeclaredField(field.name).apply {
        isAccessible = true
    }

    fun get(obj: Any): T? {
        return field.get(obj) as T
    }

    fun set(obj: Any, value: T?) {
        field.set(obj, value)
    }
}