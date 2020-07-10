package com.shouzhong.base.reflect

import java.lang.reflect.Field

class RefStaticShort(cls: Class<*>, field: Field) {
    private val field = cls.getDeclaredField(field.name).apply {
        isAccessible = true
    }

    fun get(): Short {
        return field.getShort(null)
    }

    fun set(value: Short) {
        field.setShort(null, value)
    }
}