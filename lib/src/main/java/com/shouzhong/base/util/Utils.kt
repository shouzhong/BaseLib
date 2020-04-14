package com.shouzhong.base.util

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.Fragment
import com.shouzhong.base.annotation.*
import com.shouzhong.base.dlg.BDialog
import com.shouzhong.base.dlg.BViewModel
import com.shouzhong.base.popup.BPopup
import com.shouzhong.base.popup.BPopupBean
import com.shouzhong.base.popup.PopupFragment
import com.shouzhong.request.Request
import java.lang.reflect.ParameterizedType

private var app: Application? = null

fun getApp(): Application? {
    if (app != null) return app
    app = try {
        Class.forName("android.app.ActivityThread").let {
            it.getDeclaredMethod("getApplication").invoke(it.getDeclaredMethod("currentActivityThread").invoke(null))
        } as Application
    } catch (e: Throwable) {
        null
    }
    return app
}

fun <T> Any.getGenericClass(index: Int): Class<T>? {
    val pt: ParameterizedType = javaClass.genericSuperclass as ParameterizedType
    return try {
        pt.actualTypeArguments[index] as Class<T>
    } catch (e: Throwable) {
        null
    }
}

fun Activity.startActivity(intent: Intent, callback: ((Int, Intent?) -> Unit)) {
    Request().apply {
        with(this@startActivity)
        setIntent(intent)
        setCallback(callback)
    }.start()
}

fun Fragment.startActivity(intent: Intent, callback: ((Int, Intent?) -> Unit)) {
    Request().apply {
        with(this@startActivity.activity)
        setIntent(intent)
        setCallback(callback)
    }.start()
}

fun Int.resToColor(): Int = getApp()!!.resources!!.getColor(this)

fun Int.resToDrawable(): Drawable = getApp()!!.resources!!.getDrawable(this)

fun Int.resToDimension(): Float = getApp()!!.resources!!.getDimension(this)

fun Int.resToString(): String = getApp()!!.resources!!.getString(this)

/**
 * View黑白化
 *
 */
fun View.gray() = setLayerType(View.LAYER_TYPE_HARDWARE, Paint().apply {
    colorFilter = ColorMatrixColorFilter(ColorMatrix().apply {
        setSaturation(0f)
    })
})

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
                    if (act.supportFragmentManager.findFragmentByTag(k.name) != null) return
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
                    if (act.supportFragmentManager.findFragmentByTag("${k.name}${popupDataMap[k]?.tag?.get()}") != null) return
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

