package com.shouzhong.base.permission

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.shouzhong.base.util.startActivity
import com.shouzhong.bridge.EventBusUtils

class PermissionActivity : AppCompatActivity() {
    val data: PermissionBean = PermissionBean()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设定一像素的activity
        window.setGravity(Gravity.START or Gravity.TOP)
        window.attributes = window.attributes.apply {
            x = 0
            y = 0
            height = 1
            width = 1
        }
        data.uniqueId = intent.getIntExtra("unique_id", 0)
        data.type = intent.getIntExtra("type", 0)
        if (data.type == 0) {
            val temp = intent.getStringArrayExtra("data")
            val permissions = PermissionUtils.getPermissions()
            for (permission in temp) {
                if (!permissions.contains(permission)) {
                    data.permissionsUndefined.add(permission)
                    continue
                }
                if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                    data.permissionsGranted.add(permission)
                    continue
                }
                data.permissionsRequest.add(permission)
            }
            if (data.permissionsRequest.isEmpty()) {
                EventBusUtils.post(data)
                finish()
                return
            }
            val request = arrayOfNulls<String>(data.permissionsRequest.size)
            for (i in request.indices) {
                request[i] = data.permissionsRequest[i]
            }
            ActivityCompat.requestPermissions(this, request, 0)
            return
        }
        try {
            Intent(
                when (data.type) {
                    1 -> Settings.ACTION_MANAGE_WRITE_SETTINGS
                    2 -> Settings.ACTION_MANAGE_OVERLAY_PERMISSION
                    3 -> Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    else -> null
                }
            ).apply {
                data = Uri.parse("package:$packageName")
            }.startActivity(this) { _, _ ->
                data.isGranted = when (data.type) {
                    1 -> PermissionUtils.isGrantedWriteSettings()
                    2 -> PermissionUtils.isGrantedOverlay()
                    3 -> PermissionUtils.isGrantedNotification()
                    else -> false
                }
                EventBusUtils.post(data)
                finish()
            }
        } catch (e: Throwable) {
            EventBusUtils.post(data)
            finish()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != 0) return
        for (i in permissions.indices) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                data.permissionsGranted.add(permissions[i])
                continue
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                data.permissionsDenied.add(permissions[i])
                continue
            }
            data.permissionsDeniedForever.add(permissions[i])
        }
        EventBusUtils.post(data)
        finish()
    }
}