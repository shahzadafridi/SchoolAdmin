package com.empire.adminschool

import android.app.Application
import android.content.SharedPreferences
import com.empire.adminschool.Models.LoginResponse
import com.empire.adminschool.Util.Utility

class MyApplication: Application() {

    companion object{
        var loginResponse: LoginResponse? = null
        var sharedPreferences: SharedPreferences? = null
    }

    override fun onCreate() {
        super.onCreate()
    }
}