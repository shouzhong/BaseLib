package com.shouzhong.base.permission

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.shouzhong.base.util.getApp
import com.shouzhong.base.util.hashCode
import com.shouzhong.base.util.startActivity
import com.shouzhong.bridge.ActivityStack
import com.shouzhong.bridge.EventBusUtils
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class PermissionUtils private constructor(){
    companion object {
        fun getPermissions(): List<String> {
            val list = ArrayList<String>()
            try {
                val permissions = getApp()?.packageManager?.getPackageInfo(getApp()?.packageName, PackageManager.GET_PERMISSIONS)?.requestedPermissions
                for (permission in permissions!!) {
                    list.add(permission)
                }
            } catch (e: Throwable) {}
            return list
        }

        fun isGranted(vararg permissions: String): Boolean {
            for (permission in permissions) {
                if (!isGranted(permission)) return false
            }
            return true
        }

        private fun isGranted(permission: String): Boolean {
            return Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                    || PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(getApp()!!, permission)
        }

        fun requestPermission(
            vararg permissions: String,
            grantedCallback: ((permissionsGranted: List<String>) -> Unit)? = null,
            deniedCallback: ((permissionsDenied: List<String>, permissionsDeniedForever: List<String>, permissionsUndefined: ArrayList<String>) -> Unit)? = null,
            simpleGrantedCallback: (() -> Unit)? = null,
            simpleDeniedCallback: (() -> Unit)? = null,
            errorCallback: ((errorMessage: String) -> Unit)? = null
        ) {
            PermissionUtils().apply {
                this.type = 0
                this.permissions = permissions
                this.grantedCallback = grantedCallback
                this.deniedCallback = deniedCallback
                this.simpleGrantedCallback = simpleGrantedCallback
                this.simpleDeniedCallback = simpleDeniedCallback
                this.errorCallback = errorCallback
            }.request()
        }

        fun isGrantedWriteSettings(): Boolean  = Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.System.canWrite(getApp()!!)

        fun requestWriteSettings(
            grantedCallback: (() -> Unit)? = null,
            deniedCallback: (() -> Unit)? = null,
            errorCallback: ((errorMessage: String) -> Unit)? = null
        ) {
            if (isGrantedWriteSettings()) {
                grantedCallback?.invoke()
                return
            }
            PermissionUtils().apply {
                this.type = 1
                this.simpleGrantedCallback = grantedCallback
                this.simpleDeniedCallback = deniedCallback
                this.errorCallback = errorCallback
            }.request()
        }

        fun isGrantedOverlay(): Boolean = Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(getApp()!!)

        fun requestOverlay(
            grantedCallback: (() -> Unit)? = null,
            deniedCallback: (() -> Unit)? = null,
            errorCallback: ((errorMessage: String) -> Unit)? = null
        ) {
            if (isGrantedOverlay()) {
                grantedCallback?.invoke()
                return
            }
            PermissionUtils().apply {
                this.type = 2
                this.simpleGrantedCallback = grantedCallback
                this.simpleDeniedCallback = deniedCallback
                this.errorCallback = errorCallback
            }.request()
        }

        fun isGrantedNotification(): Boolean = NotificationManagerCompat.from(getApp()!!).areNotificationsEnabled()

        fun requestNotification(
            grantedCallback: (() -> Unit)? = null,
            deniedCallback: (() -> Unit)? = null,
            errorCallback: ((String) -> Unit)? = null
        ) {
            if (isGrantedNotification()) {
                grantedCallback?.invoke()
                return
            }
            PermissionUtils().apply {
                this.type = 3
                this.simpleGrantedCallback = grantedCallback
                this.simpleDeniedCallback = deniedCallback
                this.errorCallback = errorCallback
            }.request()
        }

        fun launchAppDetailsSettings() {
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:${getApp()!!.packageName}")
            }.startActivity()
        }
    }

    private var flag: Boolean = false
    private val uniqueId = hashCode(this)
    private var type: Int = 0
    private var permissions: Array<out String>? = null
    private var grantedCallback: ((permissionsGranted: List<String>) -> Unit)? = null
    private var deniedCallback: ((permissionsDenied: List<String>, permissionsDeniedForever: List<String>, permissionsUndefined: ArrayList<String>) -> Unit)? = null
    private var simpleGrantedCallback: (() -> Unit)? = null
    private var simpleDeniedCallback: (() -> Unit)? = null
    private var errorCallback: ((errorMessage: String) -> Unit)? = null

    private fun request() {
        if (type == 0 && permissions?.isNotEmpty() != true) {
            errorCallback?.invoke("permission is null")
            return
        }
        EventBusUtils.register(this)
        try {
            Intent(getApp(), PermissionActivity::class.java).apply {
                putExtra("type", this@PermissionUtils.type)
                putExtra("unique_id", this@PermissionUtils.uniqueId)
                putExtra("data", this@PermissionUtils.permissions)
            }.startActivity()
        } catch (e: Throwable) { }
        Handler(Looper.getMainLooper()).postDelayed({
            if (flag || ActivityStack.contains(PermissionActivity::class.java)) return@postDelayed
            EventBusUtils.unregister(this@PermissionUtils)
            errorCallback?.invoke("launcher failure")
        }, 500)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun a(data: PermissionBean) {
        if (data.uniqueId != uniqueId || data.type != type) return
        flag = true
        EventBusUtils.unregister(this)
        if (type == 0) {
            grantedCallback?.invoke(data.permissionsGranted)
            deniedCallback?.invoke(data.permissionsDenied, data.permissionsDeniedForever, data.permissionsUndefined)
            if (data.permissionsDenied.isEmpty() && data.permissionsDeniedForever.isEmpty() && data.permissionsUndefined.isEmpty()) {
                simpleGrantedCallback?.invoke()
            } else {
                simpleDeniedCallback?.invoke()
            }
            return
        }
        if (type == 1 || type == 2 || type == 3) {
            if (data.isGranted) simpleGrantedCallback?.invoke()
            else simpleDeniedCallback?.invoke()
            return
        }
    }
}