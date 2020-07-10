package com.shouzhong.base.reflect

import java.lang.reflect.Field

class RefStaticLong(cls: Class<*>, field: Field) {
    private val field = cls.getDeclaredField(field.name).apply {
        isAccessible = true
    }

    fun get(): Long {
        return field.getLong(null);
    }

    fun set(value: Long) {
        field.setLong(null, value)
    }
}