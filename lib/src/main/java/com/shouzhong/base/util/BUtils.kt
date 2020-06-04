package com.shouzhong.base.util

import android.annotation.SuppressLint
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
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.shouzhong.base.annotation.*
import com.shouzhong.base.dlg.BDialog
import com.shouzhong.base.dlg.BViewModel
import com.shouzhong.base.permission.PermissionUtils
import com.shouzhong.base.popup.BPopup
import com.shouzhong.base.popup.BPopupBean
import com.shouzhong.base.popup.PopupFragment
import com.shouzhong.base.request.RequestUtils
import com.shouzhong.bridge.Bridge
import java.io.File
import java.lang.reflect.ParameterizedType

class BUtils {
    companion object {
        fun init(application: Application) {
            app = application
            Bridge.init(app)
        }
    }
}

// ****************************************************************************************************
// 跨进程的activity栈和eventbus，请关注：https://github.com/shouzhong/Bridge
// 屏幕适配，请关注：https://github.com/shouzhong/ScreenHelper
// 扫码、身份证、银行卡等，请关注：https://github.com/shouzhong/Scanner
// ****************************************************************************************************

private var app: Application? = null
private var mainHandler: Handler? = null
private var toast: Toast? = null

fun getApp(): Application {
    if (app != null) return app!!
    @SuppressLint("PrivateApi")
    app = Class.forName("android.app.ActivityThread").let {
        it.getDeclaredMethod("getApplication").invoke(it.getDeclaredMethod("currentActivityThread").invoke(null))
    } as Application
    return app!!
}

/**
 * 要想和BViewModel，BHolder等使用注解生成Dialog，请在这个类中调用该方法初始化
 *
 */
fun Any.initDialog(act: AppCompatActivity, lifecycleOwner: LifecycleOwner) {
    val dialogSwitchMap = HashMap<Class<out BDialog<out BViewModel<*>>>,  MutableLiveData<Boolean>>()
    val dialogDataMap = HashMap<Class<out BDialog<out BViewModel<*>>>, Any>()
    val dialogCancelableMap = HashMap<Class<out BDialog<out BViewModel<*>>>,  MutableLiveData<Boolean>>()
    javaClass.declaredFields.forEach { field ->
        field.getAnnotation(DialogSwitch::class.java)?.also {
            field.isAccessible = true
            dialogSwitchMap[it.value.java] = field.get(this) as  MutableLiveData<Boolean>
        }
        field.getAnnotation(DialogData::class.java)?.also {
            field.isAccessible = true
            dialogDataMap[it.value.java] = field.get(this)
        }
        field.getAnnotation(DialogCancelable::class.java)?.also {
            field.isAccessible = true
            dialogCancelableMap[it.value.java] = field.get(this) as  MutableLiveData<Boolean>
        }
    }
    for((k, v) in dialogSwitchMap) {
        v.observe(lifecycleOwner, Observer {
            val tag = "${k.name}${System.identityHashCode(dialogDataMap[k])}"
            if (it) {
                if (act.supportFragmentManager.findFragmentByTag(tag) != null) return@Observer
                val dialog = k.newInstance()
                dialog.showSwitch = v
                dialog.data = dialogDataMap[k]
                dialog.isCancelable = dialogCancelableMap[k]?.value ?: true
                dialog.show(act.supportFragmentManager, tag)
            } else {
                (act.supportFragmentManager.findFragmentByTag(tag) as BDialog<out BViewModel<*>>?)?.dismiss()
            }
        })
    }
    for ((k, v) in dialogCancelableMap) {
        v.observe(lifecycleOwner, Observer {
            val tag = "${k.name}${System.identityHashCode(dialogDataMap[k])}"
            (act.supportFragmentManager.findFragmentByTag(tag) as BDialog<out BViewModel<*>>?)?.isCancelable = it
        })
    }
}

typealias BPopupViewModel = com.shouzhong.base.popup.BViewModel<out BPopupBean>

/**
 * 要想和BViewModel，BHolder等使用注解生成PopupWindows，请在这个类中调用该方法初始化
 *
 */
fun Any.initPopup(act: AppCompatActivity, lifecycleOwner: LifecycleOwner) {
    val popupSwitchMap = HashMap<Class<out BPopup<out BPopupViewModel>>, MutableLiveData<Boolean>>()
    val popupDataMap = HashMap<Class<out BPopup<out BPopupViewModel>>, BPopupBean>()
    val popupCancelableMap = HashMap<Class<out BPopup<out BPopupViewModel>>, MutableLiveData<Boolean>>()
    javaClass.declaredFields.forEach { field ->
        field.getAnnotation(PopupSwitch::class.java)?.also {
            field.isAccessible = true
            popupSwitchMap[it.value.java] = field.get(this) as MutableLiveData<Boolean>
        }
        field.getAnnotation(PopupData::class.java)?.also {
            field.isAccessible = true
            popupDataMap[it.value.java] = field.get(this) as BPopupBean
        }
        field.getAnnotation(PopupCancelable::class.java)?.also {
            field.isAccessible = true
            popupCancelableMap[it.value.java] = field.get(this) as MutableLiveData<Boolean>
        }
    }
    for((k, v) in popupSwitchMap) {
        v.observe(lifecycleOwner, Observer {
            val tag = "${k.name}${System.identityHashCode(popupDataMap[k])}"
            if (it) {
                if (act.supportFragmentManager.findFragmentByTag(tag) != null) return@Observer
                val popup = k.newInstance()
                popup.showSwitch = v
                popup.data = popupDataMap[k]
                popup.isCancelable = popupCancelableMap[k]?.value ?: true
                when(popup.data?.showStyle) {
                    PopupFragment.SHOW_STYLE_DROP_DOWN -> popup.showAsDropDown(act.supportFragmentManager, tag,
                        popup.data!!.relatedView!!, popup.data!!.gravity, popup.data!!.x, popup.data!!.y)
                    PopupFragment.SHOW_STYLE_LOCATION -> popup.showAtLocation(act.supportFragmentManager, tag,
                        popup.data!!.relatedView!!, popup.data!!.gravity, popup.data!!.x, popup.data!!.y)
                    PopupFragment.SHOW_STYLE_TOP -> popup.showOnTop(act.supportFragmentManager, tag,
                        popup.data!!.relatedView!!, popup.data!!.gravity, popup.data!!.x, popup.data!!.y)
                    PopupFragment.SHOW_STYLE_LEFT -> popup.showOnLeft(act.supportFragmentManager, tag,
                        popup.data!!.relatedView!!, popup.data!!.gravity, popup.data!!.x, popup.data!!.y)
                    PopupFragment.SHOW_STYLE_RIGHT -> popup.showOnRight(act.supportFragmentManager, tag,
                        popup.data!!.relatedView!!, popup.data!!.gravity, popup.data!!.x, popup.data!!.y)
                    PopupFragment.SHOW_STYLE_BOTTOM -> popup.showOnBottom(act.supportFragmentManager, tag,
                        popup.data!!.relatedView!!, popup.data!!.gravity, popup.data!!.x, popup.data!!.y)
                    else -> throw IllegalArgumentException("show style does not exit")
                }
            } else {
                (act.supportFragmentManager.findFragmentByTag(tag) as BPopup<out BPopupViewModel>?)?.dismiss()
            }
        })
    }
    for ((k, v) in popupCancelableMap) {
        v.observe(lifecycleOwner, Observer {
            val tag = "${k.name}${System.identityHashCode(popupDataMap[k])}"
            (act.supportFragmentManager.findFragmentByTag(tag) as BPopup<out BPopupViewModel>?)?.isCancelable = it
        })
    }
}

/**
 * 获取泛型类型
 *
 */
fun <T> Any.getGenericClass(index: Int): Class<T>? {
    return try {
        (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[index] as Class<T>
    } catch (e: Throwable) {
        null
    }
}

/**
 * 将startActivity和startActivityForResult合并，在[callback]回调
 * 某些机型在后台需要权限才能打开activity，对于这种情况（只针对不传AppCompatActivity）请使用RequestUtils.startActivityForResult，无法打开时将在[error]中回调
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
 * 调用三方应用打开文件
 * 如果是安装应用，请加权限：<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
 *
 */
fun File.openByOtherApp(
    ctx: Context = getApp(),
    callback: ((Int, Intent?) -> Unit)? = null
) {
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

/**
 * 获取主线程 Handler
 *
 */
fun getMainHandler(): Handler {
    if (mainHandler != null) return mainHandler!!
    mainHandler = Handler(Looper.getMainLooper())
    return mainHandler!!
}

/**
 * 运行在主线程
 *
 */
fun runOnUiThread(
    delayMillis: Long = 0,
    r: () -> Unit
) {
    val l = if (delayMillis < 0) 0 else delayMillis
    if (l == 0L && Looper.getMainLooper() == Looper.myLooper()) {
        r.invoke()
        return
    }
    getMainHandler().postDelayed(r, l)
}

fun toastShort(resId: Int) = toastShort(resId.resToString())

fun toastLong(resId: Int) = toastLong(resId.resToString())

/**
 * 短时吐司
 *
 */
fun toastShort(s: CharSequence?) = toast(Toast.LENGTH_SHORT, s)

/**
 * 长时吐司
 *
 */
fun toastLong(s: CharSequence?) = toast(Toast.LENGTH_LONG, s)

private fun toast(
    duration: Int,
    s: CharSequence?
) {
    if (TextUtils.isEmpty(s)) return
    runOnUiThread {
        if (toast == null) {
            toast = Toast.makeText(getApp(), s, duration)
        } else {
            toast?.duration = duration
            toast?.setText(s)
        }
        toast?.show()
    }
}
