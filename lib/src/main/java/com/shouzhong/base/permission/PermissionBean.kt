package com.shouzhong.base.permission

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PermissionBean(
    var isGranted: Boolean = false,
    val permissionsGranted: ArrayList<String> = ArrayList(),
    val permissionsDenied: ArrayList<String> = ArrayList(),
    val permissionsDeniedForever: ArrayList<String> = ArrayList(),
    val permissionsUndefined: ArrayList<String> = ArrayList()
) : Parcelable