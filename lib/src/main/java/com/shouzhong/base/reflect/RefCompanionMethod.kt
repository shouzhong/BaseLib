package com.shouzhong.base.reflect

import java.lang.reflect.Field
import kotlin.jvm.internal.Reflection
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaType

class RefCompanionMethod<T>(cls: Class<*>, field: Field) {
    private val kClass: KClass<*> = Reflection.createKotlinClass(cls)

    private val func: KFunction<*> = kClass.companionObject.let {
        val types: Array<Class<*>?>? = when {
            field.isAnnotationPresent(MethodParams::class.java) -> {
                val value = field.getAnnotation(MethodParams::class.java).value
                val temps = arrayOfNulls<Class<*>>(value.size)
                for (i in value.indices) {
                    temps[i] = value[i].java
                    if (temps[i]?.classLoader == javaClass.classLoader) {
                        try {
                            Class.forName(cls.name)
                            temps[i] = temps[i]!!.getField("TYPE").get(null) as Class<*>
                        } catch (e: Throwable) { }
                    }
                }
                temps
            }
            field.isAnnotationPresent(MethodReflectParams::class.java) -> {
                val value = field.getAnnotation(MethodReflectParams::class.java).value
                val temps = arrayOfNulls<Class<*>>(value.size)
                for (i in value.indices) {
                    temps[i] = getProtoType(value[i]) ?: Class.forName(value[i])
                }
                temps
            }
            else -> null
        }
        var function: KFunction<*>? = null
        for (f in it!!.declaredFunctions!!) {
            if (f.name != field.name) continue
            if (f.parameters.size - 1 != (types?.size ?: 0)) continue
            if (f.parameters.size == 1) {
                function = f
                break
            }
            for (i in f.parameters.indices) {
                if (i == 0) continue
                if (f.parameters[i].type.javaType != types!![i - 1]) break
                if (i == f.parameters.size - 1) function = f
            }
        }
        function?.isAccessible = true
        function!!
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
        return func?.call(kClass.companionObjectInstance, *args) as? T
    }
}