package com.shouzhong.base.reflect

import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Modifier

class RefClass {
    companion object {
        private val REF_TYPES = HashMap<Class<*>, Constructor<*>>().apply {
            put(RefObject::class.java, RefObject::class.java.getConstructor(Class::class.java, Field::class.java))
            put(RefInt::class.java, RefInt::class.java.getConstructor(Class::class.java, Field::class.java))
            put(RefLong::class.java, RefLong::class.java.getConstructor(Class::class.java, Field::class.java))
            put(RefByte::class.java, RefByte::class.java.getConstructor(Class::class.java, Field::class.java))
            put(RefShort::class.java, RefShort::class.java.getConstructor(Class::class.java, Field::class.java))
            put(RefDouble::class.java, RefDouble::class.java.getConstructor(Class::class.java, Field::class.java))
            put(RefFloat::class.java, RefFloat::class.java.getConstructor(Class::class.java, Field::class.java))
            put(RefBoolean::class.java, RefBoolean::class.java.getConstructor(Class::class.java, Field::class.java))
            put(RefChar::class.java, RefChar::class.java.getConstructor(Class::class.java, Field::class.java))

            put(RefStaticObject::class.java, RefStaticObject::class.java.getConstructor(Class::class.java, Field::class.java))
            put(RefStaticInt::class.java, RefStaticInt::class.java.getConstructor(Class::class.java, Field::class.java))
            put(RefStaticLong::class.java, RefStaticLong::class.java.getConstructor(Class::class.java, Field::class.java))
            put(RefStaticByte::class.java, RefStaticByte::class.java.getConstructor(Class::class.java, Field::class.java))
            put(RefStaticShort::class.java, RefStaticShort::class.java.getConstructor(Class::class.java, Field::class.java))
            put(RefStaticDouble::class.java, RefStaticDouble::class.java.getConstructor(Class::class.java, Field::class.java))
            put(RefStaticFloat::class.java, RefStaticFloat::class.java.getConstructor(Class::class.java, Field::class.java))
            put(RefStaticBoolean::class.java, RefStaticBoolean::class.java.getConstructor(Class::class.java, Field::class.java))
            put(RefStaticChar::class.java, RefStaticChar::class.java.getConstructor(Class::class.java, Field::class.java))

            put(RefMethod::class.java, RefMethod::class.java.getConstructor(Class::class.java, Field::class.java))
            put(RefStaticMethod::class.java, RefStaticMethod::class.java.getConstructor(Class::class.java, Field::class.java))
            put(RefCompanionMethod::class.java, RefCompanionMethod::class.java.getConstructor(Class::class.java, Field::class.java))

            put(RefConstructor::class.java, RefConstructor::class.java.getConstructor(Class::class.java, Field::class.java))
        }

        fun load(mappingClass: Class<*>, realClass: Class<*>? = null, clsName: String? = null): Class<*> {
            val cls = realClass ?: Class.forName(clsName!!)
            try {
                val fields = mappingClass.declaredFields;
                for (field in fields) {
                    try {
                        if (!Modifier.isStatic(field.modifiers)) continue
                        val constructor = REF_TYPES[field.type]
                        if (constructor != null) {
                            field.set(null, constructor.newInstance(cls, field))
                        }
                    } catch (e: Throwable) { }
                }
            } catch (e: Throwable) {}
            return cls;
        }
    }
}