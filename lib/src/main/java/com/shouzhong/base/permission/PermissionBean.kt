package com.shouzhong.base.permission

data class PermissionBean(
    var uniqueId: Int = 0,
    var type: Int = 0,
    var isGranted: Boolean = false,
    val permissionsRequest: ArrayList<String> = ArrayList(),
    val permissionsGranted: ArrayList<String> = ArrayList(),
    val permissionsDenied: ArrayList<String> = ArrayList(),
    val permissionsDeniedForever: ArrayList<String> = ArrayList(),
    val permissionsUndefined: ArrayList<String> = ArrayList()
)