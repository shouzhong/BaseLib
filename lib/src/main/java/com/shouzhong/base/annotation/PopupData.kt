package com.shouzhong.base.annotation

import com.shouzhong.base.popup.BPopup
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class PopupData(val value: KClass<out BPopup<*>>)