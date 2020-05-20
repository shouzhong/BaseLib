package com.shouzhong.base.util

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import com.shouzhong.base.annotation.*
import com.shouzhong.base.dlg.BDialog
import com.shouzhong.base.dlg.BViewModel
import com.shouzhong.base.permission.PermissionUtils
import com.shouzhong.base.popup.BPopup
import com.shouzhong.base.popup.BPopupBean
import com.shouzhong.base.popup.PopupFragment
import com.shouzhong.base.request.RequestUtils
import java.io.File
import java.lang.reflect.ParameterizedType


// ****************************************************************************************************
// 跨进程的activity栈和eventbus，请关注：https://github.com/shouzhong/Bridge
// 屏幕适配，请关注：https://github.com/shouzhong/ScreenHelper
// 扫码、身份证、银行卡等，请关注：https://github.com/shouzhong/Scanner
// ****************************************************************************************************

internal var bApp: Application? = null

fun getApp(): Application {
    if (bApp != null) return bApp!!
    bApp = try {
        Class.forName("android.app.ActivityThread").let {
            it.getDeclaredMethod("getApplication").invoke(it.getDeclaredMethod("currentActivityThread").invoke(null))
        } as Application
    } catch (e: Throwable) {
        null
    }
    return bApp!!
}

///**
// * 获取顶层Activity，如果在onCreate中调用，将不是当前activity
// * 如果反射无法使用 ，请尝试使用https://github.com/tiann/FreeReflection
// *
// */
//fun getTopActivity(): Activity? {
//    try {
//        val activityThreadClass = Class.forName("android.app.ActivityThread")
//        val currentActivityThreadMethod = activityThreadClass.getMethod("currentActivityThread").invoke(null)
//        val mActivityListField = activityThreadClass.getDeclaredField("mActivities")
//        mActivityListField.isAccessible = true
//        val activities = mActivityListField.get(currentActivityThreadMethod) as Map<*, *>
//        val last = activities.values.last()!!
//        val activityField = last.javaClass.getDeclaredField("activity")
//        activityField.isAccessible = true
//        return activityField.get(last) as Activity
////        for (activityRecord in activities.values) {
////            val activityRecordClass = activityRecord!!.javaClass
////            val pausedField = activityRecordClass.getDeclaredField("paused")
////            pausedField.isAccessible = true
////            if (!pausedField.getBoolean(activityRecord)) {
////                val activityField = activityRecordClass.getDeclaredField("activity")
////                activityField.isAccessible = true
////                return activityField.get(activityRecord) as Activity
////            }
////        }
//    } catch (e: Throwable) { }
//    return null
//}
//
///**
// * 获取activity栈，如果在onCreate中调用，将没有当前activity
// * 如果反射无法使用 ，请尝试使用：https://github.com/tiann/FreeReflection
// *
// */
//fun getActivities(): List<Activity> {
//    var list = ArrayList<Activity>()
//    try {
//        val mLoadedApkField = Application::class.java.getDeclaredField("mLoadedApk")
//        mLoadedApkField.isAccessible = true
//        val mLoadedApk = mLoadedApkField.get(getApp())
//        val mActivityThreadField = mLoadedApk.javaClass.getDeclaredField("mActivityThread")
//        mActivityThreadField.isAccessible = true
//        val mActivityThread = mActivityThreadField.get(mLoadedApk)
//        val mActivitiesField = mActivityThread.javaClass.getDeclaredField("mActivities")
//        mActivitiesField.isAccessible = true
//        val mActivities = mActivitiesField.get(mActivityThread) as Map<*, *>
//        for (value in mActivities.values) {
//            val activityField = value!!.javaClass.getDeclaredField("activity")
//            activityField.isAccessible = true
//            list.add(activityField.get(value) as Activity)
//        }
//    } catch (e: Throwable) { }
//    return list
//}

/**
 * 将startActivity和startActivityForResult合并，在[callback]回调，某些机型在后台需要权限才能打开activity，对于这种情况（只针对不传AppCompatActivity）在[error]中回调
 *
 */
fun Intent.startActivity(
    ctx: Context = getApp(),
    callback: ((resultCode: Int, data: Intent?) -> Unit)? = null
) {
    if (callback == null) {
        if (ctx !is Activity) addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ctx.startActivity(this)
        return
    }
    RequestUtils.startActivityForResult(ctx, this, callback)
}

/**
 * 请求权限
 *
 */
fun String.permissionRequest(
    ctx: Context = getApp(),
    grantedCallback: ((permissionsGranted: ArrayList<String>) -> Unit)? = null,
    deniedCallback: ((permissionsDenied: ArrayList<String>, permissionsDeniedForever: ArrayList<String>, permissionsUndefined: ArrayList<String>) -> Unit)? = null,
    simpleGrantedCallback: (() -> Unit)? = null,
    simpleDeniedCallback: (() -> Unit)? = null
) {
    PermissionUtils.requestPermission(
        this,
        ctx = ctx,
        grantedCallback = grantedCallback,
        deniedCallback = deniedCallback,
        simpleGrantedCallback = simpleGrantedCallback,
        simpleDeniedCallback = simpleDeniedCallback
    )
}

/**
 * 请求权限
 *
 */
fun Array<String>.permissionRequest(
    ctx: Context = getApp(),
    grantedCallback: ((permissionsGranted: ArrayList<String>) -> Unit)? = null,
    deniedCallback: ((permissionsDenied: ArrayList<String>, permissionsDeniedForever: ArrayList<String>, permissionsUndefined: ArrayList<String>) -> Unit)? = null,
    simpleGrantedCallback: (() -> Unit)? = null,
    simpleDeniedCallback: (() -> Unit)? = null
) {
    PermissionUtils.requestPermission(
        permissions = *this,
        ctx = ctx,
        grantedCallback = grantedCallback,
        deniedCallback = deniedCallback,
        simpleGrantedCallback = simpleGrantedCallback,
        simpleDeniedCallback = simpleDeniedCallback
    )
}

/**
 * 请求权限
 *
 */
fun ArrayList<String>.permissionRequest(
    ctx: Context = getApp(),
    grantedCallback: ((permissionsGranted: ArrayList<String>) -> Unit)? = null,
    deniedCallback: ((permissionsDenied: ArrayList<String>, permissionsDeniedForever: ArrayList<String>, permissionsUndefined: ArrayList<String>) -> Unit)? = null,
    simpleGrantedCallback: (() -> Unit)? = null,
    simpleDeniedCallback: (() -> Unit)? = null
) {
    PermissionUtils.requestPermission(
        permissions = *(this.toArray(arrayOfNulls<String>(this.size)) as Array<out String>),
        ctx = ctx,
        grantedCallback = grantedCallback,
        deniedCallback = deniedCallback,
        simpleGrantedCallback = simpleGrantedCallback,
        simpleDeniedCallback = simpleDeniedCallback
    )
}

/**
 * 获取泛型类型
 *
 */
fun <T> Any.getGenericClass(
    index: Int
): Class<T>? {
    return try {
        (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[index] as Class<T>
    } catch (e: Throwable) {
        null
    }
}

/**
 * 获取颜色资源
 *
 */
fun Int.resToColor(): Int = getApp().resources.getColor(this)

/**
 * 获取图片资源
 *
 */
fun Int.resToDrawable(): Drawable = getApp().resources.getDrawable(this)

/**
 * 获取尺寸资源
 *
 */
fun Int.resToDimension(): Float = getApp().resources.getDimension(this)

/**
 * 获取字符串资源
 *
 */
fun Int.resToString(): String = getApp().resources.getString(this)

/**
 * View黑白化
 * 使整个activity黑白化，activity.window.decorView.gray()
 *
 */
fun View.gray() = setLayerType(View.LAYER_TYPE_HARDWARE, Paint().apply {
    colorFilter = ColorMatrixColorFilter(ColorMatrix().apply {
        setSaturation(0f)
    })
})

/**
 * 要想和BViewModel，BHolder等使用注解生成Dialog，请在这个类中调用该方法初始化
 *
 */
fun Any.initDialog(act: AppCompatActivity) {
    val dialogSwitchMap = HashMap<Class<out BDialog<out BViewModel<*>>>, ObservableBoolean>()
    val dialogDataMap = HashMap<Class<out BDialog<out BViewModel<*>>>, Any>()
    val dialogCancelableMap = HashMap<Class<out BDialog<out BViewModel<*>>>, ObservableBoolean>()
    javaClass.declaredFields.forEach { field ->
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

/**
 * 要想和BViewModel，BHolder等使用注解生成PopupWindows，请在这个类中调用该方法初始化
 *
 */
fun Any.initPopup(act: AppCompatActivity) {
    val popupSwitchMap = HashMap<Class<out BPopup<out BPopupViewModel>>, ObservableBoolean>()
    val popupDataMap = HashMap<Class<out BPopup<out BPopupViewModel>>, BPopupBean>()
    val popupCancelableMap = HashMap<Class<out BPopup<out BPopupViewModel>>, ObservableBoolean>()
    javaClass.declaredFields.forEach { field ->
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

/**
 * 调用三方应用打开文件
 * 如果是安装应用，请加权限：<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
 *
 */
fun File.openByOtherApp(ctx: Context = getApp(), callback: ((Int, Intent?) -> Unit)? = null) {
    if (!isFile) return
    val mimeType = absolutePath.toMimeType()
    if (TextUtils.isEmpty(mimeType)) return
    Intent(Intent.ACTION_VIEW).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            setDataAndType(FileProvider.getUriForFile(ctx.applicationContext, ctx.applicationContext.packageName + ".shouzhong.fileprovider", this@openByOtherApp), mimeType)
        } else {
            setDataAndType(Uri.fromFile(this@openByOtherApp), mimeType)
        }
    }.startActivity(ctx, callback)
}

/**
 * 获取MIME类型
 *
 * @return 格式为：xxx/xxx，如：application/vnd.android.package-archive
 */
fun String.toMimeType(): String? {
    if (TextUtils.isEmpty(this)) return null
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(this))
}
