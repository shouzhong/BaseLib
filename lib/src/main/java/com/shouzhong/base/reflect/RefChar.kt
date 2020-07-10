package com.shouzhong.base.reflect

import java.lang.reflect.Field

class RefChar(cls: Class<*>, field: Field) {
    private val field = cls.getDeclaredField(field.name).apply {
        isAccessible = true
    }

    fun get(obj: Any): Char {
        return field.getChar(obj)
    }

    fun set(obj: Any, value: Char) {
        field.setChar(obj, value)
    }
}