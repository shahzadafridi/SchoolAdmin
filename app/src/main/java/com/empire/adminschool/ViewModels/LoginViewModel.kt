package com.empire.adminschool.ViewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.brikmas.balochtransport.Data.Network.RetrofitConstant
import com.empire.adminschool.Data.MainRepository

class LoginViewModel() : ViewModel() {

    lateinit var repository: MainRepository

    fun injectRepository(context: Context) {
        repository = MainRepository(RetrofitConstant.getEndPoints(),context)
    }

    fun onUserLogin(username: String, password: String) {
        repository.onUserLogin(username,password)
    }
}