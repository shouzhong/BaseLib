package com.shouzhong.base.demo.act

import android.Manifest
import android.view.View
import androidx.databinding.ObservableField
import com.shouzhong.base.act.BActivity
import com.shouzhong.base.act.BViewModel
import com.shouzhong.base.demo.R
import com.shouzhong.base.permission.PermissionUtils
import com.shouzhong.base.util.permissionRequest

class PermissionActivity : BActivity<PermissionViewModel>(R.layout.act_permission)

class PermissionViewModel : BViewModel() {
    val text = ObservableField<CharSequence>()

    fun onClickPermission(v: View) {
        text.set("")
        arrayListOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ).permissionRequest(
            ctx = getActivity(),
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
            }
        )

//        Manifest.permission.CAMERA.permissionRequest(
//            ctx = getActivity(),
//            grantedCallback = { permissionsGranted ->
//                text.set(StringBuffer().apply {
//                    append(text.get())
//                    append("grantedCallback:\n[")
//                    for (i in permissionsGranted.indices) {
//                        append(permissionsGranted[i])
//                        if (i < permissionsGranted.size - 1) append(", ")
//                    }
//                    append("]\n\n")
//                })
//            },
//            deniedCallback = { permissionsDenied, permissionsDeniedForever, permissionsUndefined ->
//                text.set(StringBuffer().apply {
//                    append(text.get())
//                    append("deniedCallback:\npermissionsDenied:\n[")
//                    for (i in permissionsDenied.indices) {
//                        append(permissionsDenied[i])
//                        if (i < permissionsDenied.size - 1) append(", ")
//                    }
//                    append("]\npermissionsDeniedForever:\n[")
//                    for (i in permissionsDeniedForever.indices) {
//                        append(permissionsDeniedForever[i])
//                        if (i < permissionsDeniedForever.size - 1) append(", ")
//                    }
//                    append("]\npermissionsUndefined:\n[")
//                    for (i in permissionsUndefined.indices) {
//                        append(permissionsUndefined[i])
//                        if (i < permissionsUndefined.size - 1) append(", ")
//                    }
//                    append("]\n\n")
//                })
//            },
//            simpleGrantedCallback = {
//                text.set(StringBuffer().apply {
//                    append(text.get())
//                    append("simpleGrantedCallback\n\n")
//                })
//            },
//            simpleDeniedCallback = {
//                text.set(StringBuffer().apply {
//                    append(text.get())
//                    append("simpleDeniedCallback\n\n")
//                })
//            }
//        )

//        arrayOf(
//            Manifest.permission.CAMERA,
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.ACCESS_BACKGROUND_LOCATION
//        ).permissionRequest(
//            ctx = getActivity(),
//            grantedCallback = { permissionsGranted ->
//                text.set(StringBuffer().apply {
//                    append(text.get())
//                    append("grantedCallback:\n[")
//                    for (i in permissionsGranted.indices) {
//                        append(permissionsGranted[i])
//                        if (i < permissionsGranted.size - 1) append(", ")
//                    }
//                    append("]\n\n")
//                })
//            },
//            deniedCallback = { permissionsDenied, permissionsDeniedForever, permissionsUndefined ->
//                text.set(StringBuffer().apply {
//                    append(text.get())
//                    append("deniedCallback:\npermissionsDenied:\n[")
//                    for (i in permissionsDenied.indices) {
//                        append(permissionsDenied[i])
//                        if (i < permissionsDenied.size - 1) append(", ")
//                    }
//                    append("]\npermissionsDeniedForever:\n[")
//                    for (i in permissionsDeniedForever.indices) {
//                        append(permissionsDeniedForever[i])
//                        if (i < permissionsDeniedForever.size - 1) append(", ")
//                    }
//                    append("]\npermissionsUndefined:\n[")
//                    for (i in permissionsUndefined.indices) {
//                        append(permissionsUndefined[i])
//                        if (i < permissionsUndefined.size - 1) append(", ")
//                    }
//                    append("]\n\n")
//                })
//            },
//            simpleGrantedCallback = {
//                text.set(StringBuffer().apply {
//                    append(text.get())
//                    append("simpleGrantedCallback\n\n")
//                })
//            },
//            simpleDeniedCallback = {
//                text.set(StringBuffer().apply {
//                    append(text.get())
//                    append("simpleDeniedCallback\n\n")
//                })
//            }
//        )

//        PermissionUtils.requestPermission(
//            Manifest.permission.CAMERA,
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
//            ctx = getActivity(),
//            grantedCallback = { permissionsGranted ->
//                text.set(StringBuffer().apply {
//                    append(text.get())
//                    append("grantedCallback:\n[")
//                    for (i in permissionsGranted.indices) {
//                        append(permissionsGranted[i])
//                        if (i < permissionsGranted.size - 1) append(", ")
//                    }
//                    append("]\n\n")
//                })
//            },
//            deniedCallback = { permissionsDenied, permissionsDeniedForever, permissionsUndefined ->
//                text.set(StringBuffer().apply {
//                    append(text.get())
//                    append("deniedCallback:\npermissionsDenied:\n[")
//                    for (i in permissionsDenied.indices) {
//                        append(permissionsDenied[i])
//                        if (i < permissionsDenied.size - 1) append(", ")
//                    }
//                    append("]\npermissionsDeniedForever:\n[")
//                    for (i in permissionsDeniedForever.indices) {
//                        append(permissionsDeniedForever[i])
//                        if (i < permissionsDeniedForever.size - 1) append(", ")
//                    }
//                    append("]\npermissionsUndefined:\n[")
//                    for (i in permissionsUndefined.indices) {
//                        append(permissionsUndefined[i])
//                        if (i < permissionsUndefined.size - 1) append(", ")
//                    }
//                    append("]\n\n")
//                })
//            },
//            simpleGrantedCallback = {
//                text.set(StringBuffer().apply {
//                    append(text.get())
//                    append("simpleGrantedCallback\n\n")
//                })
//            },
//            simpleDeniedCallback = {
//                text.set(StringBuffer().apply {
//                    append(text.get())
//                    append("simpleDeniedCallback\n\n")
//                })
//            }
//        )
    }

    fun onClickWriteSettings(v: View) {
        text.set("")
        PermissionUtils.requestWriteSettings(
            ctx = getActivity(),
            grantedCallback = {
                text.set("success")
            },
            deniedCallback = {
                text.set("failure")
            }
        )
    }

    fun onClickOverlay(v: View) {
        text.set("")
        PermissionUtils.requestOverlay(
            ctx = getActivity(),
            grantedCallback = {
                text.set("success")
            },
            deniedCallback = {
                text.set("failure")
            }
        )
    }

    fun onClickNotification(v: View) {
        text.set("")
        PermissionUtils.requestNotification(
            ctx = getActivity(),
            grantedCallback = {
                text.set("success")
            },
            deniedCallback = {
                text.set("failure")
            }
        )
    }

    fun onClickAppDetail(v: View) = PermissionUtils.launchAppDetailsSettings()
}