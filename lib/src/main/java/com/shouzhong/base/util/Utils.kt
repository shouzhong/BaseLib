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

fun <T> getGenericClass(obj: Any, index: Int): Class<T> {
    val pt: ParameterizedType = obj.javaClass.genericSuperclass as ParameterizedType
    return pt.actualTypeArguments[index] as Class<T>
}

fun setDialog(obj: Any, act: AppCompatActivity) {
    val dialogSwitchMap = HashMap<Class<out BDialog<out BViewModel<*>>>, ObservableBoolean>()
    val dialogDataMap = HashMap<Class<out BDialog<out BViewModel<*>>>, Any>()
    val dialogCancelableMap = HashMap<Class<out BDialog<out BViewModel<*>>>, ObservableBoolean>()
    obj.javaClass.declaredFields?.forEach { field ->
        field.getAnnotation(DialogSwitch::class.java)?.also {
            field.isAccessible = true
            dialogSwitchMap[it.value.java] = field.get(obj) as ObservableBoolean
        }
        field.getAnnotation(DialogData::class.java)?.also {
            field.isAccessible = true
            dialogDataMap[it.value.java] = field.get(obj)
        }
        field.getAnnotation(DialogCancelable::class.java)?.also {
            field.isAccessible = true
            dialogCancelableMap[it.value.java] = field.get(obj) as ObservableBoolean
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