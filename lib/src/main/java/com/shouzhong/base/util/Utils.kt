package com.shouzhong.base.util

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import com.shouzhong.base.annotation.*
import com.shouzhong.base.dlg.BDialog
import com.shouzhong.base.dlg.BViewModel
import com.shouzhong.base.popup.BPopup
import com.shouzhong.base.popup.BPopupBean
import com.shouzhong.base.popup.PopupFragment
import com.shouzhong.request.Request
import java.lang.ref.WeakReference
import java.lang.reflect.ParameterizedType

private var bApp: Application? = null

fun getApp(): Application? {
    if (bApp != null) return bApp
    bApp = try {
        Class.forName("android.app.ActivityThread").let {
            it.getDeclaredMethod("getApplication").invoke(it.getDeclaredMethod("currentActivityThread").invoke(null))
        } as Application
    } catch (e: Throwable) {
        null
    }
    return bApp
}

/**
 * 获取顶层Activity，如果在onCreate中调用，将不是当前activity
 * 如果反射不好用 ，请尝试使用https://github.com/tiann/FreeReflection
 *
 */
fun getTopActivity(): Activity? {
    try {
        val activityThreadClass = Class.forName("android.app.ActivityThread")
        val currentActivityThreadMethod = activityThreadClass.getMethod("currentActivityThread").invoke(null)
        val mActivityListField = activityThreadClass.getDeclaredField("mActivities")
        mActivityListField.isAccessible = true
        val activities = mActivityListField.get(currentActivityThreadMethod) as Map<*, *>
        val last = activities.values.last()!!
        val activityField = last.javaClass.getDeclaredField("activity")
        activityField.isAccessible = true
        return activityField.get(last) as Activity
//        for (activityRecord in activities.values) {
//            val activityRecordClass = activityRecord!!.javaClass
//            val pausedField = activityRecordClass.getDeclaredField("paused")
//            pausedField.isAccessible = true
//            if (!pausedField.getBoolean(activityRecord)) {
//                val activityField = activityRecordClass.getDeclaredField("activity")
//                activityField.isAccessible = true
//                return activityField.get(activityRecord) as Activity
//            }
//        }
    } catch (e: Throwable) { }
    return null
}

/**
 * 获取activity栈，如果在onCreate中调用，将没有当前activity
 * 如果反射不好用 ，请尝试使用https://github.com/tiann/FreeReflection
 *
 */
fun getActivities(): List<WeakReference<Activity>> {
    var list = ArrayList<WeakReference<Activity>>()
    try {
        val mLoadedApkField = Application::class.java.getDeclaredField("mLoadedApk")
        mLoadedApkField.isAccessible = true
        val mLoadedApk = mLoadedApkField.get(getApp())
        val mActivityThreadField = mLoadedApk.javaClass.getDeclaredField("mActivityThread")
        mActivityThreadField.isAccessible = true
        val mActivityThread = mActivityThreadField.get(mLoadedApk)
        val mActivitiesField = mActivityThread.javaClass.getDeclaredField("mActivities")
        mActivitiesField.isAccessible = true
        val mActivities = mActivitiesField.get(mActivityThread) as Map<*, *>
        for (value in mActivities.values) {
            val activityField = value!!.javaClass.getDeclaredField("activity")
            activityField.isAccessible = true
            list.add(WeakReference(activityField.get(value) as Activity))
        }
    } catch (e: Throwable) { }
    return list
}

fun Intent.startActivity(act: Activity? = null, callback: ((Int, Intent?) -> Unit)? = null) {
    if (callback == null) {
        if (act == null) addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val ctx: Context? = act ?: getApp()
        ctx?.startActivity(this)
        return
    }
    val temp = act ?: getTopActivity() ?: return
    Request().apply {
        with(temp)
        setIntent(this@startActivity)
        setCallback(callback)
    }.start()
}

fun <T> Any.getGenericClass(index: Int): Class<T>? {
    val pt: ParameterizedType = javaClass.genericSuperclass as ParameterizedType
    return try {
        pt.actualTypeArguments[index] as Class<T>
    } catch (e: Throwable) {
        null
    }
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

