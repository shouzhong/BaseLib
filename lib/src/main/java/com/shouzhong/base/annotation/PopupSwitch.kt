package com.shouzhong.base.annotation

import com.shouzhong.base.popup.BPopup
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class PopupSwitch(val value: KClass<out BPopup<*>>)