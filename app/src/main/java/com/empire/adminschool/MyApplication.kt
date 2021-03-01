package com.empire.adminschool

import android.app.Application
import com.empire.adminschool.Models.LoginResponse

class MyApplication: Application() {

    companion object{
        var loginResponse: LoginResponse? = null
    }
}