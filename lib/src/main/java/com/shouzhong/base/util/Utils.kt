package com.shouzhong.base.util

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import com.shouzhong.base.annotation.DialogCancelable
import com.shouzhong.base.annotation.DialogData
import com.shouzhong.base.annotation.DialogSwitch
import com.shouzhong.base.dlg.BDialog
import com.shouzhong.base.dlg.BViewModel
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

inline fun <reified T> Any.getField(name: String): T? {
    val field = javaClass.getDeclaredField(name)
    field.isAccessible = true
    val result = field.get(this)
    return when(result) {
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

