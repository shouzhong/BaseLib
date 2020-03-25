package com.shouzhong.base.annotation

import com.shouzhong.base.dlg.BDialog
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class DialogCancelable(val value: KClass<out BDialog<*>>)