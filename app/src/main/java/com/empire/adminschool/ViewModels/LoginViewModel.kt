package com.empire.adminschool.ViewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.empire.adminschool.Data.Repositories.MainRepository
import com.empire.adminschool.Data.Remote.RetrofitConstant

class LoginViewModel() : ViewModel() {

    lateinit var repository: MainRepository

    fun injectRepository(context: Context) {
        repository = MainRepository(RetrofitConstant.getEndPoints(),context)
    }

    fun onUserLogin(username: String, password: String) {
        repository.onUserLogin(username,password)
    }
}