package com.shouzhong.base.permission

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PermissionBean(
    var isGranted: Boolean = false,
    val permissionsRequest: ArrayList<String> = arrayListOf(),
    val permissionsGranted: ArrayList<String> = arrayListOf(),
    val permissionsDenied: ArrayList<String> = arrayListOf(),
    val permissionsDeniedForever: ArrayList<String> = arrayListOf(),
    val permissionsUndefined: ArrayList<String> = arrayListOf()
) : Parcelable