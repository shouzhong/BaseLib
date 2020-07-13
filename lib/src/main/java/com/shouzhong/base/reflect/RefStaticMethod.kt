package com.shouzhong.base.reflect

import java.lang.reflect.Field
import java.lang.reflect.Method

class RefStaticMethod<T>(cls: Class<*>, field: Field) {
    private val method: Method = when {
        field.isAnnotationPresent(MethodParams::class.java) -> {
            val value = field.getAnnotation(MethodParams::class.java).value
            val types = arrayOfNulls<Class<*>>(value.size)
            for (i in value.indices) {
                types[i] = value[i].java
                if (types[i]?.classLoader == javaClass.classLoader) {
                    try {
                        Class.forName(cls.name)
                        types[i] = types[i]!!.getField("TYPE").get(null) as Class<*>
                    } catch (e: Throwable) { }
                }
            }
            cls.getDeclaredMethod(field.name, *types)
        }
        field.isAnnotationPresent(MethodReflectParams::class.java) -> {
            val value = field.getAnnotation(MethodReflectParams::class.java).value
            val types = arrayOfNulls<Class<*>>(value.size)
            for (i in value.indices) {
                types[i] = getProtoType(value[i]) ?: Class.forName(value[i])
            }
            cls.getDeclaredMethod(field.name, *types)
        }
        else -> {
            val methods = cls.declaredMethods
            var temp: Method? = null
            for (m in methods) {
                if (m.name == field.name) {
                    temp = m
                    break
                }
            }
            temp!!
        }
    }?.apply {
        isAccessible = true
    }

    private fun getProtoType(typeName: String): Class<*>? {
        return when (typeName) {
            "int" -> java.lang.Integer.TYPE
            "long" -> java.lang.Long.TYPE
            "boolean" -> java.lang.Boolean.TYPE
            "byte" -> java.lang.Byte.TYPE
            "short" -> java.lang.Short.TYPE
            "char" -> java.lang.Character.TYPE
            "float" -> java.lang.Float.TYPE
            "double" -> java.lang.Double.TYPE
            "void" -> java.lang.Void.TYPE
            else -> null
        }

    }

    fun call(vararg args: Any?): T? {
        return method.invoke(null, *args) as? T
    }
}