package com.shouzhong.base.permission

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.shouzhong.base.util.getApp

class PermissionActivity : AppCompatActivity() {
    var uniqueId: Int = 0
    var type: Int = 0
    val permissionsRequest = ArrayList<String>()
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
        uniqueId = intent.getIntExtra("unique_id", 0)
        type = intent.getIntExtra("type", 0)
        if (type == 0) {
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
                permissionsRequest.add(permission)
            }
            if (permissionsRequest.isEmpty()) {
                data.isGranted = data.permissionsDenied.isEmpty() && data.permissionsDeniedForever.isEmpty() && data.permissionsUndefined.isEmpty()
                sendResult()
                return
            }
            ActivityCompat.requestPermissions(
                this,
                permissionsRequest.toArray(arrayOfNulls<String>(permissionsRequest.size)),
                uniqueId and 0xffff
            )
            return
        }
        startActivityForResult(
            Intent().apply {
                action = when (this@PermissionActivity.type) {
                    1 -> Settings.ACTION_MANAGE_WRITE_SETTINGS
                    2 -> Settings.ACTION_MANAGE_OVERLAY_PERMISSION
                    3 -> Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    else -> null
                }
                data = Uri.parse("package:$packageName")
            },
            uniqueId and 0xffff
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == (uniqueId and 0xffff)) {
            this.data.isGranted = when (this.type) {
                1 -> PermissionUtils.isGrantedWriteSettings()
                2 -> PermissionUtils.isGrantedOverlay()
                3 -> PermissionUtils.isGrantedNotification()
                else -> false
            }
            sendResult()
            return
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == (uniqueId and 0xffff)) {
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
            data.isGranted = data.permissionsDenied.isEmpty() && data.permissionsDeniedForever.isEmpty() && data.permissionsUndefined.isEmpty()
            sendResult()
            return
        }
    }

    private fun sendResult() {
        getApp().sendBroadcast(
            Intent().apply {
                action = "${getApp().packageName}.shouzhong.receiver.action.REQUEST_PERMISSION_$uniqueId"
                putExtra("data", this@PermissionActivity.data)
            }
        )
        finish()
    }
}