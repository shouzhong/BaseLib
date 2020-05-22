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
                    else -> Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                }
                data = Uri.parse("package:${getApp().packageName}")
            },
            code
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (this.cache.indexOfKey(requestCode) < 0) return
        val result = when(this.cache[requestCode].type) {
            1 -> PermissionUtils.isGrantedWriteSettings()
            2 -> PermissionUtils.isGrantedOverlay()
            3 -> PermissionUtils.isGrantedNotification()
            else -> false
        }
        if (result) this.cache[requestCode].simpleGrantedCallback?.invoke()
        else this.cache[requestCode].simpleDeniedCallback?.invoke()
        this.cache.remove(requestCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (this.cache.indexOfKey(requestCode) < 0) return
        for (i in permissions.indices) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                data[requestCode].permissionsGranted.add(permissions[i])
                continue
            }
            if (shouldShowRequestPermissionRationale(permissions[i])) {
                data[requestCode].permissionsDenied.add(permissions[i])
                continue
            }
            data[requestCode].permissionsDeniedForever.add(permissions[i])
        }
        a(requestCode)
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