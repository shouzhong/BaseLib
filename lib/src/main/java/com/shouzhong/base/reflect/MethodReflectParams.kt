package com.shouzhong.base.reflect

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class MethodReflectParams(val value: Array<String>)