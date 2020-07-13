package com.shouzhong.base.reflect

import java.lang.reflect.Constructor
import java.lang.reflect.Field

class RefConstructor<T>(cls: Class<*>, field: Field) {
    private val ctor: Constructor<*> = when {
        field.isAnnotationPresent(MethodParams::class.java) -> {
            val value = field.getAnnotation(MethodParams::class.java).value
            val types = arrayOfNulls<Class<*>>(value.size)
            for (i in value.indices) {
                types[i] = value[i].java
            }
            cls.getDeclaredConstructor(*types)
        }
        field.isAnnotationPresent(MethodReflectParams::class.java) -> {
            val value = field.getAnnotation(MethodReflectParams::class.java).value
            val types = arrayOfNulls<Class<*>>(value.size)
            for (i in value.indices) {
                types[i] = Class.forName(value[i])
            }
            cls.getDeclaredConstructor(*types)
        }
        else -> {
            cls.getDeclaredConstructor()
        }
    }.apply {
        isAccessible = true
    }

    fun newInstance(vararg params: Any?): T {
        return ctor.newInstance(*params) as T
    }
}