package com.empire.adminschool.Activities

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.brikmas.balochtransport.Data.Network.RetrofitConstant
import com.empire.adminschool.Fragments.MainFragment
import com.empire.adminschool.Models.LoginResponse
import com.empire.adminschool.MyApplication
import com.empire.adminschool.R
import com.empire.adminschool.Util.AppPermissions
import com.empire.adminschool.Util.FragmentStack
import com.empire.adminschool.Util.Utility
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"
    var btBack: ImageView? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var baseUrl = Utility.provideSharedPreferences(this).getString("base_url",null)
        baseUrl?.let {
            RetrofitConstant.BASE_URL = it
        }

        var loginResponse = Utility.provideSharedPreferences(this).getString("login_response",null)
        loginResponse?.let {
            var res = Gson().fromJson(loginResponse,LoginResponse::class.java)
            MyApplication.loginResponse = res
            Log.e(TAG,loginResponse)
        }

        FragmentStack.replaceFragmentToContainer(R.id.main_container,supportFragmentManager,
            MainFragment(),"MainFragment")
    }
}