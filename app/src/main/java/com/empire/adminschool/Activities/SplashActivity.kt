package com.empire.adminschool.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.empire.adminschool.R
import com.empire.adminschool.Util.Utility

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var isLogin = Utility.provideSharedPreferences(this).getBoolean("isLogin",false)
        Handler(Looper.getMainLooper()).postDelayed({
            if (isLogin){
                Utility.startMainActivity(this)
            }else{
                Utility.startLoginActivity(this)
            }
        },2000)
    }
}