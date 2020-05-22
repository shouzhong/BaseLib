package com.shouzhong.base.permission

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.provider.Settings
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.shouzhong.base.util.getApp
import com.shouzhong.base.util.startActivity

/**
 * 权限管理
 *
 */
class PermissionUtils private constructor(){
    companion object {
        /**
         * 获取注册的所有权限
         *
         */
        fun getPermissions(): List<String> {
            val list = arrayListOf<String>()
            try {
                val permissions = getApp().packageManager.getPackageInfo(getApp().packageName, PackageManager.GET_PERMISSIONS)?.requestedPermissions ?: return list
                for (permission in permissions) {
                    list.add(permission)
                }
            } catch (e: Throwable) {}
            return list
        }

        /**
         * 是否有权限
         *
         */
        fun isGranted(vararg permissions: String): Boolean {
            for (permission in permissions) {
                if (!isGranted(permission)) return false
            }
            return true
        }

        /**
         * 是否有权限
         *
         */
        private fun isGranted(permission: String): Boolean {
            return Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                    || PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(getApp(), permission)
        }

        /**
         * 申请权限
         * [permissions]所有申请的权限
         * [grantedCallback]授予权限回调
         * [deniedCallback]拒绝权限回调
         * [simpleGrantedCallback]授予权限简单回调
         * [simpleDeniedCallback]拒绝权限简单回调
         *
         */
        fun requestPermission(
            vararg permissions: String,
            ctx: Context? = null,
            grantedCallback: ((permissionsGranted: ArrayList<String>) -> Unit)? = null,
            deniedCallback: ((permissionsDenied: ArrayList<String>, permissionsDeniedForever: ArrayList<String>, permissionsUndefined: ArrayList<String>) -> Unit)? = null,
            simpleGrantedCallback: (() -> Unit)? = null,
            simpleDeniedCallback: (() -> Unit)? = null,
            error: ((errorMessage: String) -> Unit)? = null
        ) {
            PermissionUtils().apply {
                this.type = 0
                this.permissions = permissions
                this.grantedCallback = grantedCallback
                this.deniedCallback = deniedCallback
                this.simpleGrantedCallback = simpleGrantedCallback
                this.simpleDeniedCallback = simpleDeniedCallback
                this.error = error
            }.request(ctx)
        }

        /**
         * 是否有修改系统权限
         *
         */
        fun isGrantedWriteSettings(): Boolean  = Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.System.canWrite(getApp())

        /**
         * 申请修改系统权限
         *
         */
        fun requestWriteSettings(
            ctx: Context? = null,
            grantedCallback: (() -> Unit)? = null,
            deniedCallback: (() -> Unit)? = null,
            error: ((errorMessage: String) -> Unit)? = null
        ) {
            if (isGrantedWriteSettings()) {
                grantedCallback?.invoke()
                return
            }
            PermissionUtils().apply {
                this.type = 1
                this.simpleGrantedCallback = grantedCallback
                this.simpleDeniedCallback = deniedCallback
                this.error = error
            }.request(ctx)
        }

        /**
         * 是否有悬浮窗权限
         *
         */
        fun isGrantedOverlay(): Boolean = Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(getApp())

        /**
         * 申请悬浮窗权限
         *
         */
        fun requestOverlay(
            ctx: Context? = null,
            grantedCallback: (() -> Unit)? = null,
            deniedCallback: (() -> Unit)? = null,
            error: ((errorMessage: String) -> Unit)? = null
        ) {
            if (isGrantedOverlay()) {
                grantedCallback?.invoke()
                return
            }
            PermissionUtils().apply {
                this.type = 2
                this.simpleGrantedCallback = grantedCallback
                this.simpleDeniedCallback = deniedCallback
                this.error = error
            }.request(ctx)
        }

        /**
         * 判断通知是否可用
         *
         */
        fun isGrantedNotification(): Boolean = NotificationManagerCompat.from(getApp()).areNotificationsEnabled()

        /**
         * 申请通知权限
         *
         */
        fun requestNotification(
            ctx: Context? = null,
            grantedCallback: (() -> Unit)? = null,
            deniedCallback: (() -> Unit)? = null,
            error: ((errorMessage: String) -> Unit)? = null
        ) {
            if (isGrantedNotification()) {
                grantedCallback?.invoke()
                return
            }
            PermissionUtils().apply {
                this.type = 3
                this.simpleGrantedCallback = grantedCallback
                this.simpleDeniedCallback = deniedCallback
                this.error = error
            }.request(ctx)
        }

        /**
         * 设置通知栏是否可见
         *
         */
        @RequiresPermission(Manifest.permission.EXPAND_STATUS_BAR)
        fun setNotificationBarVisibility(isVisible: Boolean) {
            try {
                val methodName = when {
                    isVisible && (Build.VERSION.SDK_INT <= 16) -> "expand"
                    isVisible && (Build.VERSION.SDK_INT > 16) -> "expandNotificationsPanel"
                    !isVisible && (Build.VERSION.SDK_INT <= 16) -> "collapse"
                    else -> "collapsePanels"
                }
                @SuppressLint("WrongConstant")
                val service = getApp().getSystemService("statusbar")
                val clsStatusBarManager = Class.forName("android.app.StatusBarManager")
                clsStatusBarManager.getMethod(methodName).invoke(service)
            } catch (e: Throwable) {}
        }

        /**
         * 打开应用具体设置
         *
         */
        fun launchAppDetailsSettings() {
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:${getApp().packageName}")
            }.startActivity()
        }
    }

    internal val uniqueId = System.identityHashCode(this)
    internal var flag = false
    internal var type: Int = 0
    internal var permissions: Array<out String>? = null
    internal var grantedCallback: ((permissionsGranted: ArrayList<String>) -> Unit)? = null
    internal var deniedCallback: ((permissionsDenied: ArrayList<String>, permissionsDeniedForever: ArrayList<String>, permissionsUndefined: ArrayList<String>) -> Unit)? = null
    internal var simpleGrantedCallback: (() -> Unit)? = null
    internal var simpleDeniedCallback: (() -> Unit)? = null
    private var error: ((errorMessage: String) -> Unit)? = null

    private fun request(context: Context?) {
        if (type == 0 && permissions?.isNotEmpty() != true) {
            error?.invoke("permissions is empty")
            return
        }
        val ctx = context ?: getApp()
        if (ctx is AppCompatActivity) {
            getFragment(ctx).put(this)
            return
        }
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.getBooleanExtra("flag", false) == true) {
                    flag = true
                    return
                }
                getApp().unregisterReceiver(this)
                val data = intent?.getParcelableExtra<PermissionBean>("data") ?: return
                if (type == 0) {
                    grantedCallback?.invoke(data.permissionsGranted)
                    deniedCallback?.invoke(data.permissionsDenied, data.permissionsDeniedForever, data.permissionsUndefined)
                }
                if (data.isGranted) simpleGrantedCallback?.invoke()
                else simpleDeniedCallback?.invoke()
            }
        }
        getApp().registerReceiver(
            receiver,
            IntentFilter("${getApp().packageName}.shouzhong.receiver.action.REQUEST_PERMISSION_$uniqueId")
        )
        ctx.startActivity(
            Intent(ctx, PermissionActivity::class.java).apply {
                if (ctx !is Activity) addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra("type", this@PermissionUtils.type)
                putExtra("unique_id", this@PermissionUtils.uniqueId)
                putExtra("data", this@PermissionUtils.permissions)
            }
        )
        Handler().postDelayed({
            if (flag) return@postDelayed
            getApp().unregisterReceiver(receiver)
            error?.invoke("launcher failure")
        }, 500)
    }

    private fun getFragment(act: AppCompatActivity): PermissionFragment {
        val manager = act.supportFragmentManager
        var frgm: PermissionFragment? = manager.findFragmentByTag(PermissionFragment.TAG) as PermissionFragment?
        if (frgm == null) {
            frgm = PermissionFragment()
            manager.beginTransaction().add(frgm, PermissionFragment.TAG).commitAllowingStateLoss()
            manager.executePendingTransactions()
        }
        return frgm
    }
}