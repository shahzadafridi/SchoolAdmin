package com.empire.adminschool.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import com.empire.adminschool.R
import com.empire.adminschool.ViewModels.LoginViewModel

class LoginActivity : AppCompatActivity() {

    val TAG = "LoginActivity"
    private lateinit var viewModel: LoginViewModel
    var etEmail: EditText? = null
    var etPass: EditText? =  null
    var btLogin: Button? = null
    var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        viewModel.injectRepository(this)
        progressBar = findViewById(R.id.login_progress)
        etEmail = findViewById(R.id.login_email_et)
        etPass = findViewById(R.id.login_pass_et)
        btLogin = findViewById(R.id.login_btn)
        btLogin!!.setOnClickListener {
            if (validation()){
                progressBar!!.visibility = View.VISIBLE
                viewModel.onUserLogin(etEmail!!.text.toString(),etPass!!.text.toString())
            }
        }
    }

    fun validation(): Boolean{

        var isValid = true

        if (TextUtils.isEmpty(etEmail!!.text.toString())){
            isValid = false
            etEmail!!.setError("Enter valid username")
        }

        if (TextUtils.isEmpty(etPass!!.text.toString())){
            isValid = false
            etPass!!.setError("Enter valid password")
        }

        return isValid
    }


}