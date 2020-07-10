package com.shouzhong.base.reflect

import java.lang.reflect.Field

class RefStaticBoolean(cls: Class<*>, field: Field) {
    private val field = cls.getDeclaredField(field.name).apply {
        isAccessible = true
    }

    fun get(): Boolean {
        return field.getBoolean(null)
    }

    fun set(value: Boolean) {
        field.setBoolean(null, value)
    }
}