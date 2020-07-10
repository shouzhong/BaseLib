package com.shouzhong.base.reflect

import java.lang.reflect.Field

class RefStaticChar(cls: Class<*>, field: Field) {
    private val field = cls.getDeclaredField(field.name).apply {
        isAccessible = true
    }

    fun get(): Char {
        return field.getChar(null)
    }

    fun set(value: Char) {
        field.setChar(null, value)
    }
}