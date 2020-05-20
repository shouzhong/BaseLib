package com.shouzhong.base.permission

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.SparseArray
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.shouzhong.base.util.getApp

class PermissionFragment : Fragment() {
    companion object {
        val TAG = PermissionFragment::class.java.name
    }

    private val cache = SparseArray<PermissionUtils>()
    private val data = SparseArray<PermissionBean>()

    internal fun put(p: PermissionUtils) {
        val code = p.uniqueId and 0xffff
        cache.put(code, p)
        if (p.type == 0) {
            val bean = PermissionBean()
            data.put(code, bean)
            val permissions = PermissionUtils.getPermissions()
            for (permission in p.permissions!!) {
                if (!permissions.contains(permission)) {
                    bean.permissionsUndefined.add(permission)
                    continue
                }
                if (ContextCompat.checkSelfPermission(getApp(), permission) == PackageManager.PERMISSION_GRANTED) {
                    bean.permissionsGranted.add(permission)
                    continue
                }
                bean.permissionsRequest.add(permission)
            }
            if (bean.permissionsRequest.isEmpty()) {
                a(code)
                return
            }
            requestPermissions(
                bean.permissionsRequest.toArray(arrayOfNulls<String>(bean.permissionsRequest.size)),
                code
            )
            return
        }
        startActivityForResult(
            Intent().apply {
                action = when (p.type) {
                    1 -> Settings.ACTION_MANAGE_WRITE_SETTINGS
                    2 -> Settings.ACTION_MANAGE_OVERLAY_PERMISSION
                    3 -> Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    else -> null
                }
                data = Uri.parse("package:${getApp().packageName}")
            },
            code
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val code = requestCode and 0xffff
        if (this.cache.indexOfKey(code) < 0) return
        val result = when(this.cache[code].type) {
            1 -> PermissionUtils.isGrantedWriteSettings()
            2 -> PermissionUtils.isGrantedOverlay()
            3 -> PermissionUtils.isGrantedNotification()
            else -> false
        }
        if (result) this.cache[code].simpleGrantedCallback?.invoke()
        else this.cache[code].simpleDeniedCallback?.invoke()
        this.cache.remove(code)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val code = requestCode and 0xffff
        if (this.cache.indexOfKey(code) < 0) return
        for (i in permissions.indices) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                data[code].permissionsGranted.add(permissions[i])
                continue
            }
            if (shouldShowRequestPermissionRationale(permissions[i])) {
                data[code].permissionsDenied.add(permissions[i])
                continue
            }
            data[code].permissionsDeniedForever.add(permissions[i])
        }
        a(code)
    }

    private fun a(code: Int) {
        val p = cache[code]
        val b = data[code]
        b.isGranted = b.permissionsDenied.isEmpty() && b.permissionsDeniedForever.isEmpty() && b.permissionsUndefined.isEmpty()
        p.grantedCallback?.invoke(b.permissionsGranted)
        p.deniedCallback?.invoke(b.permissionsDenied, b.permissionsDeniedForever, b.permissionsUndefined)
        if (data[code].isGranted) p.simpleGrantedCallback?.invoke()
        else p.simpleDeniedCallback?.invoke()
        cache.remove(code)
        data.remove(code)
    }
}