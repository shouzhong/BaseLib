package com.shouzhong.base.permission

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.shouzhong.base.util.getApp
import com.shouzhong.base.util.hashCode
import com.shouzhong.base.util.startActivity

class PermissionUtils private constructor(){
    companion object {
        fun getPermissions(): List<String> {
            val list = ArrayList<String>()
            try {
                val permissions = getApp().packageManager.getPackageInfo(getApp().packageName, PackageManager.GET_PERMISSIONS)?.requestedPermissions ?: return list
                for (permission in permissions) {
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
                    || PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(getApp(), permission)
        }

        fun requestPermission(
            vararg permissions: String,
            ctx: Context? = null,
            grantedCallback: ((permissionsGranted: List<String>) -> Unit)? = null,
            deniedCallback: ((permissionsDenied: List<String>, permissionsDeniedForever: List<String>, permissionsUndefined: ArrayList<String>) -> Unit)? = null,
            simpleGrantedCallback: (() -> Unit)? = null,
            simpleDeniedCallback: (() -> Unit)? = null
        ) {
            PermissionUtils().apply {
                this.type = 0
                this.permissions = permissions
                this.grantedCallback = grantedCallback
                this.deniedCallback = deniedCallback
                this.simpleGrantedCallback = simpleGrantedCallback
                this.simpleDeniedCallback = simpleDeniedCallback
            }.request(ctx)
        }

        fun isGrantedWriteSettings(): Boolean  = Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.System.canWrite(getApp())

        fun requestWriteSettings(
            ctx: Context? = null,
            grantedCallback: (() -> Unit)? = null,
            deniedCallback: (() -> Unit)? = null
        ) {
            if (isGrantedWriteSettings()) {
                grantedCallback?.invoke()
                return
            }
            PermissionUtils().apply {
                this.type = 1
                this.simpleGrantedCallback = grantedCallback
                this.simpleDeniedCallback = deniedCallback
            }.request(ctx)
        }

        fun isGrantedOverlay(): Boolean = Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(getApp())

        fun requestOverlay(
            ctx: Context? = null,
            grantedCallback: (() -> Unit)? = null,
            deniedCallback: (() -> Unit)? = null
        ) {
            if (isGrantedOverlay()) {
                grantedCallback?.invoke()
                return
            }
            PermissionUtils().apply {
                this.type = 2
                this.simpleGrantedCallback = grantedCallback
                this.simpleDeniedCallback = deniedCallback
            }.request(ctx)
        }

        fun isGrantedNotification(): Boolean = NotificationManagerCompat.from(getApp()).areNotificationsEnabled()

        fun requestNotification(
            ctx: Context? = null,
            grantedCallback: (() -> Unit)? = null,
            deniedCallback: (() -> Unit)? = null
        ) {
            if (isGrantedNotification()) {
                grantedCallback?.invoke()
                return
            }
            PermissionUtils().apply {
                this.type = 3
                this.simpleGrantedCallback = grantedCallback
                this.simpleDeniedCallback = deniedCallback
            }.request(ctx)
        }

        fun launchAppDetailsSettings() {
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:${getApp().packageName}")
            }.startActivity()
        }
    }

    private val uniqueId = hashCode(this)
    private var type: Int = 0
    private var permissions: Array<out String>? = null
    private var grantedCallback: ((permissionsGranted: List<String>) -> Unit)? = null
    private var deniedCallback: ((permissionsDenied: List<String>, permissionsDeniedForever: List<String>, permissionsUndefined: ArrayList<String>) -> Unit)? = null
    private var simpleGrantedCallback: (() -> Unit)? = null
    private var simpleDeniedCallback: (() -> Unit)? = null

    private fun request(ctx: Context?) {
        require(type != 0 || permissions?.isNotEmpty() == true) { "permission is null" }
        getApp().registerReceiver(
            object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    getApp().unregisterReceiver(this)
                    val data = intent?.getParcelableExtra<PermissionBean>("data") ?: return
                    if (type == 0) {
                        grantedCallback?.invoke(data.permissionsGranted)
                        deniedCallback?.invoke(data.permissionsDenied, data.permissionsDeniedForever, data.permissionsUndefined)
                    }
                    if (data.isGranted) simpleGrantedCallback?.invoke()
                    else simpleDeniedCallback?.invoke()
                }
            },
            IntentFilter("${getApp().packageName}.shouzhong.receiver.action.REQUEST_PERMISSION_$uniqueId")
        )
        (ctx ?: getApp()).startActivity(
            Intent(ctx ?: getApp(), PermissionActivity::class.java).apply {
                if (ctx == null || ctx !is Activity) addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra("type", this@PermissionUtils.type)
                putExtra("unique_id", this@PermissionUtils.uniqueId)
                putExtra("data", this@PermissionUtils.permissions)
            }
        )
    }
}