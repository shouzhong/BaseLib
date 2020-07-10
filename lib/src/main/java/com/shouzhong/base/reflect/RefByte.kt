package com.shouzhong.base.reflect

import java.lang.reflect.Field

class RefByte(cls: Class<*>, field: Field) {
    private val field = cls.getDeclaredField(field.name).apply {
        isAccessible = true
    }

    fun get(obj: Any): Byte {
        return field.getByte(obj)
    }

    fun set(obj: Any, value: Byte) {
        field.setByte(obj, value)
    }
}