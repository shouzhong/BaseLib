package com.shouzhong.base.reflect

import java.lang.reflect.Field

class RefStaticInt(cls: Class<*>, field: Field) {
    private val field = cls.getDeclaredField(field.name).apply {
        isAccessible = true
    }

    fun get(): Int {
        return field.getInt(null)
    }

    fun set(value: Int) {
        field.setInt(null, value)
    }
}