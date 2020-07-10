package com.shouzhong.base.reflect

import java.lang.reflect.Field

class RefStaticFloat(cls: Class<*>, field: Field) {
    private val field = cls.getDeclaredField(field.name).apply {
        isAccessible = true
    }

    fun get(): Float {
        return field.getFloat(null)
    }

    fun set(value: Float) {
        field.setFloat(null, value)
    }
}