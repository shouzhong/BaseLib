package com.shouzhong.base.reflect

import java.lang.reflect.Field

class RefStaticObject<T>(cls: Class<*>, field: Field) {
    private val field = cls.getDeclaredField(field.name).apply {
        isAccessible = true
    }

    fun get(): T? {
        return field.get(null) as T
    }

    fun set(value: T?) {
        field.set(null, value)
    }
}