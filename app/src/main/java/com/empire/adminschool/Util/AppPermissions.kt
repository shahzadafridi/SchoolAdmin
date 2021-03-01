package com.empire.adminschool.Util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

object AppPermissions {

    val READ_PHONE_STATE_PERMISSION = 111

    fun checkReadPhoneStatePermission(activity: Activity): Boolean{
        val isGranted = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
        if (!isGranted){
            requestReadPhoneStatePermission(activity)
        }
        return isGranted
    }

    fun requestReadPhoneStatePermission(activity: Activity){
        val permissions = arrayOf<String>(Manifest.permission.READ_PHONE_STATE)
        ActivityCompat.requestPermissions(activity,permissions, READ_PHONE_STATE_PERMISSION)
    }
}