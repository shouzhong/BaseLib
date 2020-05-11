package com.shouzhong.base.demo.act

import android.Manifest
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.databinding.ObservableField
import com.shouzhong.base.act.BActivity
import com.shouzhong.base.act.BViewModel
import com.shouzhong.base.demo.R
import com.shouzhong.base.permission.PermissionUtils

class PermissionActivity : BActivity<PermissionViewModel>(R.layout.act_permission)

class PermissionViewModel : BViewModel() {
    val text = ObservableField<CharSequence>()

    fun onClickPermission(v: View) {
        text.set("")
        PermissionUtils
            .requestPermission(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                grantedCallback = { permissionsGranted ->
                    text.set(StringBuffer().apply {
                        append(text.get())
                        append("grantedCallback:[")
                        for (i in permissionsGranted.indices) {
                            append(permissionsGranted[i])
                            if (i < permissionsGranted.size - 1) append(",")
                        }
                        append("]\n\n")
                    })
                },
                deniedCallback = { permissionsDenied, permissionsDeniedForever, permissionsUndefined ->
                    text.set(StringBuffer().apply {
                        append(text.get())
                        append("deniedCallback:[")
                        for (i in permissionsDenied.indices) {
                            append(permissionsDenied[i])
                            if (i < permissionsDenied.size - 1) append(",")
                        }
                        append("]\n\npermissionsDeniedForever:[")
                        for (i in permissionsDeniedForever.indices) {
                            append(permissionsDeniedForever[i])
                            if (i < permissionsDeniedForever.size - 1) append(",")
                        }
                        append("]\n\npermissionsUndefined:[")
                        for (i in permissionsUndefined.indices) {
                            append(permissionsUndefined[i])
                            if (i < permissionsUndefined.size - 1) append(",")
                        }
                        append("]\n\n")
                    })
                },
                simpleGrantedCallback = {
                    text.set(StringBuffer().apply {
                        append(text.get())
                        append("simpleGrantedCallback")
                    })
                },
                simpleDeniedCallback = {
                    text.set(StringBuffer().apply {
                        append(text.get())
                        append("simpleDeniedCallback\n\n")
                    })
                },
                errorCallback = { errorMessage ->
                    text.set(StringBuffer().apply {
                        append(text.get())
                        append("errorCallback:$errorMessage")
                        append("\n\n")
                    })
                }
            )
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun onClickWriteSettings(v: View) {
        text.set("")
        PermissionUtils
            .requestWriteSettings({
                text.set("success")
            }, {
                text.set("failure")
            }, {
                text.set(it)
            })
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun onClickOverlay(v: View) {
        text.set("")
        PermissionUtils
            .requestOverlay({
                text.set("success")
            }, {
                text.set("failure")
            }, {
                text.set(it)
            })
    }

    fun onClickAppDetail(v: View) = PermissionUtils.launchAppDetailsSettings()
}