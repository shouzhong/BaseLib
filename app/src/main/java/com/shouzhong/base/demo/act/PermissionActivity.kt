package com.shouzhong.base.demo.act

import android.Manifest
import android.view.View
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
        PermissionUtils.requestPermission(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            grantedCallback = { permissionsGranted ->
                text.set(StringBuffer().apply {
                    append(text.get())
                    append("grantedCallback:\n[")
                    for (i in permissionsGranted.indices) {
                        append(permissionsGranted[i])
                        if (i < permissionsGranted.size - 1) append(", ")
                    }
                    append("]\n\n")
                })
            },
            deniedCallback = { permissionsDenied, permissionsDeniedForever, permissionsUndefined ->
                text.set(StringBuffer().apply {
                    append(text.get())
                    append("deniedCallback:\npermissionsDenied:\n[")
                    for (i in permissionsDenied.indices) {
                        append(permissionsDenied[i])
                        if (i < permissionsDenied.size - 1) append(", ")
                    }
                    append("]\npermissionsDeniedForever:\n[")
                    for (i in permissionsDeniedForever.indices) {
                        append(permissionsDeniedForever[i])
                        if (i < permissionsDeniedForever.size - 1) append(", ")
                    }
                    append("]\npermissionsUndefined:\n[")
                    for (i in permissionsUndefined.indices) {
                        append(permissionsUndefined[i])
                        if (i < permissionsUndefined.size - 1) append(", ")
                    }
                    append("]\n\n")
                })
            },
            simpleGrantedCallback = {
                text.set(StringBuffer().apply {
                    append(text.get())
                    append("simpleGrantedCallback\n\n")
                })
            },
            simpleDeniedCallback = {
                text.set(StringBuffer().apply {
                    append(text.get())
                    append("simpleDeniedCallback\n\n")
                })
            },
            errorCallback = {
                text.set(StringBuffer().apply {
                    append(text.get())
                    append("errorCallback:$it")
                    append("\n\n")
                })
            }
        )
    }

    fun onClickWriteSettings(v: View) {
        text.set("")
        PermissionUtils.requestWriteSettings(
            grantedCallback = {
                text.set("success")
            },
            deniedCallback = {
                text.set("failure")
            },
            errorCallback = {
                text.set(it)
            }
        )
    }

    fun onClickOverlay(v: View) {
        text.set("")
        PermissionUtils.requestOverlay(
            grantedCallback = {
                text.set("success")
            },
            deniedCallback = {
                text.set("failure")
            },
            errorCallback = {
                text.set(it)
            }
        )
    }

    fun onClickNotification(v: View) {
        text.set("")
        PermissionUtils.requestNotification(
            grantedCallback = {
                text.set("success")
            },
            deniedCallback = {
                text.set("failure")
            },
            errorCallback = {
                text.set(it)
            }
        )
    }

    fun onClickAppDetail(v: View) = PermissionUtils.launchAppDetailsSettings()
}