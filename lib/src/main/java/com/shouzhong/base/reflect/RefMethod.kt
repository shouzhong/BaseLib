package com.shouzhong.base.reflect

import java.lang.reflect.Field
import java.lang.reflect.Method

class RefMethod<T>(cls: Class<*>, field: Field) {
    private lateinit var method: Method

    init {
        if (field.isAnnotationPresent(MethodParams::class.java)) {
            val value = field.getAnnotation(MethodParams::class.java).value
            val types = arrayOfNulls<Class<*>>(value.size)
            for (i in value.indices) {
                types[i] = value[i]::class.java
                if (types[i]?.classLoader == javaClass.classLoader) {
                    try {
                        Class.forName(cls.name)
                        types[i] = types[i]!!.getField("TYPE").get(null) as Class<*>
                    } catch (e: Throwable) {}
                }
            }
            method = cls.getDeclaredMethod(field.name, *types)
        } else if (field.isAnnotationPresent(MethodReflectParams::class.java)) {
            val value = field.getAnnotation(MethodReflectParams::class.java).value
            val types = arrayOfNulls<Class<*>>(value.size)
            for (i in value.indices) {
                types[i] = Class.forName(value[i])
            }
            method = cls.getDeclaredMethod(field.name, *types)
        } else {
            method = cls.getDeclaredMethod(field.name)
        }
        method.isAccessible = true
    }
}