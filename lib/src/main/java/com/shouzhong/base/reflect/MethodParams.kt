package com.shouzhong.base.reflect

import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class MethodParams(val value: Array<KClass<*>>)