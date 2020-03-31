package com.shouzhong.base.util

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import com.shouzhong.base.annotation.*
import com.shouzhong.base.dlg.BDialog
import com.shouzhong.base.dlg.BViewModel
import com.shouzhong.base.popup.BPopup
import com.shouzhong.base.popup.BPopupBean
import com.shouzhong.base.popup.PopupFragment
import java.lang.reflect.ParameterizedType

fun <T> Any.getGenericClass(index: Int): Class<T> {
    val pt: ParameterizedType = javaClass.genericSuperclass as ParameterizedType
    return pt.actualTypeArguments[index] as Class<T>
}

fun Any.initDialog(act: AppCompatActivity) {
    val dialogSwitchMap = HashMap<Class<out BDialog<out BViewModel<*>>>, ObservableBoolean>()
    val dialogDataMap = HashMap<Class<out BDialog<out BViewModel<*>>>, Any>()
    val dialogCancelableMap = HashMap<Class<out BDialog<out BViewModel<*>>>, ObservableBoolean>()
    javaClass.declaredFields?.forEach { field ->
        field.getAnnotation(DialogSwitch::class.java)?.also {
            field.isAccessible = true
            dialogSwitchMap[it.value.java] = field.get(this) as ObservableBoolean
        }
        field.getAnnotation(DialogData::class.java)?.also {
            field.isAccessible = true
            dialogDataMap[it.value.java] = field.get(this)
        }
        field.getAnnotation(DialogCancelable::class.java)?.also {
            field.isAccessible = true
            dialogCancelableMap[it.value.java] = field.get(this) as ObservableBoolean
        }
    }
    for((k, v) in dialogSwitchMap) {
        v.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (v.get()) {
                    val dialog = k.newInstance()
                    dialog.showSwitch = v
                    dialog.data = dialogDataMap[k]
                    dialog.isCancelable = dialogCancelableMap[k]?.get() ?: true
                    dialog.show(act.supportFragmentManager, k.name)
                } else {
                    act.supportFragmentManager.findFragmentByTag(k.name)?.also {
                        val dialog = it as BDialog<out BViewModel<*>>
                        dialog.dismiss()
                    }
                }
            }
        })
    }
    for ((k, v) in dialogCancelableMap) {
        v.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                act.supportFragmentManager.findFragmentByTag(k.name)?.also {
                    val dialog = it as BDialog<out BViewModel<*>>
                    dialog.isCancelable = v.get()
                }
            }
        })
    }
}

typealias BPopupViewModel = com.shouzhong.base.popup.BViewModel<out BPopupBean>

fun Any.initPopup(act: AppCompatActivity) {
    val popupSwitchMap = HashMap<Class<out BPopup<out BPopupViewModel>>, ObservableBoolean>()
    val popupDataMap = HashMap<Class<out BPopup<out BPopupViewModel>>, BPopupBean>()
    val popupCancelableMap = HashMap<Class<out BPopup<out BPopupViewModel>>, ObservableBoolean>()
    javaClass.declaredFields?.forEach { field ->
        field.getAnnotation(PopupSwitch::class.java)?.also {
            field.isAccessible = true
            popupSwitchMap[it.value.java] = field.get(this) as ObservableBoolean
        }
        field.getAnnotation(PopupData::class.java)?.also {
            field.isAccessible = true
            popupDataMap[it.value.java] = field.get(this) as BPopupBean
        }
        field.getAnnotation(PopupCancelable::class.java)?.also {
            field.isAccessible = true
            popupCancelableMap[it.value.java] = field.get(this) as ObservableBoolean
        }
    }
    for((k, v) in popupSwitchMap) {
        v.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (v.get()) {
                    val popup = k.newInstance()
                    popup.showSwitch = v
                    popup.data = popupDataMap[k]
                    popup.isCancelable = popupCancelableMap[k]?.get() ?: true
                    when(popup.data?.showStyle?.get()) {
                        PopupFragment.SHOW_STYLE_DROP_DOWN -> popup.showAsDropDown(act.supportFragmentManager, "${k.name}${popup.data?.tag?.get()}",
                            popup.data!!.relatedView.get()!!, popup.data!!.gravity.get(), popup.data!!.x.get(), popup.data!!.y.get())
                        PopupFragment.SHOW_STYLE_LOCATION -> popup.showAtLocation(act.supportFragmentManager, "${k.name}${popup.data?.tag?.get()}",
                            popup.data!!.relatedView.get()!!, popup.data!!.gravity.get(), popup.data!!.x.get(), popup.data!!.y.get())
                        PopupFragment.SHOW_STYLE_TOP -> popup.showOnTop(act.supportFragmentManager, "${k.name}${popup.data?.tag?.get()}",
                            popup.data!!.relatedView.get()!!, popup.data!!.gravity.get(), popup.data!!.x.get(), popup.data!!.y.get())
                        PopupFragment.SHOW_STYLE_LEFT -> popup.showOnLeft(act.supportFragmentManager, "${k.name}${popup.data?.tag?.get()}",
                            popup.data!!.relatedView.get()!!, popup.data!!.gravity.get(), popup.data!!.x.get(), popup.data!!.y.get())
                        PopupFragment.SHOW_STYLE_RIGHT -> popup.showOnRight(act.supportFragmentManager, "${k.name}${popup.data?.tag?.get()}",
                            popup.data!!.relatedView.get()!!, popup.data!!.gravity.get(), popup.data!!.x.get(), popup.data!!.y.get())
                        PopupFragment.SHOW_STYLE_BOTTOM -> popup.showOnBottom(act.supportFragmentManager, "${k.name}${popup.data?.tag?.get()}",
                            popup.data!!.relatedView.get()!!, popup.data!!.gravity.get(), popup.data!!.x.get(), popup.data!!.y.get())
                        else -> throw IllegalArgumentException("show style does not exit")
                    }
                } else {
                    act.supportFragmentManager.findFragmentByTag("${k.name}${popupDataMap[k]?.tag?.get()}")?.also {
                        val popup = it as BPopup<out BPopupViewModel>
                        popup.dismiss()
                    }
                }
            }
        })
    }
    for ((k, v) in popupCancelableMap) {
        v.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                act.supportFragmentManager.findFragmentByTag(k.name)?.also {
                    val popup = it as BPopup<out BPopupViewModel>
                    popup.isCancelable = v.get()
                }
            }
        })
    }
}

inline fun <reified T> Any.getField(name: String): T? {
    val field = javaClass.getDeclaredField(name)
    field.isAccessible = true
    return when(val result = field.get(this)) {
        is T -> result
        else -> null
    }
}

fun Any.setField(name: String, value: Any?) {
    val field = javaClass.getDeclaredField(name)
    field.isAccessible = true
    field.set(this, value)
}

fun Any.invokeMethod(name: String, vararg parameterTypes: Class<*>, args: Array<Any?>? = null) {
    val method = javaClass.getDeclaredMethod(name, *parameterTypes)
    method.isAccessible = true
    method.invoke(this, args)
}

