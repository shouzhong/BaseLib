package com.shouzhong.base.reflect

import java.lang.reflect.Field

class RefStaticDouble(cls: Class<*>, field: Field) {
    private val field = cls.getDeclaredField(field.name).apply {
        isAccessible = true
    }

    fun get(): Double {
        return field.getDouble(null)
    }

    fun set(value: Double) {
        field.setDouble(null, value)
    }
}