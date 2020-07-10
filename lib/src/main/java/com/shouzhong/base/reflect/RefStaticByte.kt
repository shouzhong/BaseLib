package com.shouzhong.base.reflect

import java.lang.reflect.Field

class RefStaticByte(cls: Class<*>, field: Field) {
    private val field = cls.getDeclaredField(field.name).apply {
        isAccessible = true
    }

    fun get(): Byte {
        return field.getByte(null)
    }

    fun set(value: Byte) {
        field.setByte(null, value)
    }
}