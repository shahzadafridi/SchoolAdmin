package com.empire.adminschool.ViewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.empire.adminschool.Data.Repositories.MainRepository
import com.empire.adminschool.Data.Remote.RetrofitConstant
import com.empire.adminschool.Data.Repositories.AttendenceRepository
import com.empire.adminschool.Util.Utility

class LoginViewModel() : ViewModel() {

    lateinit var repository: MainRepository
    var context: Context? = null

    fun injectRepository(context: Context) {
        this.context = context
    }

    fun onUserLogin(username: String, password: String) {
        RetrofitConstant.BASE_URL = Utility.provideSharedPreferences(context!!).getString("base_url",RetrofitConstant.BASE_URL)!!
        repository = MainRepository(RetrofitConstant.getEndPoints(), context!!)
        repository.onUserLogin(username,password)
    }
}