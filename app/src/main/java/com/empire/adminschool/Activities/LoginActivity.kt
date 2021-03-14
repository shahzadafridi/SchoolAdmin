package com.empire.adminschool.Activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.empire.adminschool.Data.Remote.RetrofitConstant
import com.empire.adminschool.R
import com.empire.adminschool.Util.Utility
import com.empire.adminschool.Util.Utility.isValidUrl
import com.empire.adminschool.ViewModels.LoginViewModel

class LoginActivity : AppCompatActivity() {

    val TAG = "LoginActivity"
    private lateinit var viewModel: LoginViewModel
    var etEmail: EditText? = null
    var etPass: EditText? =  null
    var btLogin: Button? = null
    var progressBar: ProgressBar? = null
    var settingIcon: ImageView? = null
    var settingDialog: Dialog? = null
    var baseUrl: EditText? = null
    var rGroup: RadioGroup? = null
    var save: Button? = null
    var type: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        viewModel.injectRepository(this)
        settingIcon = findViewById(R.id.login_setting)
        progressBar = findViewById(R.id.login_progress)
        etEmail = findViewById(R.id.login_email_et)
        etPass = findViewById(R.id.login_pass_et)
        btLogin = findViewById(R.id.login_btn)
        settingDialog = Utility.onCreateDialog(this,R.layout.setting_dialog_layout,false)
        baseUrl = settingDialog!!.findViewById(R.id.app_setting_base_url)
        rGroup = settingDialog!!.findViewById(R.id.app_setting_rg)
        save = settingDialog!!.findViewById(R.id.app_setting_save)
        var base_url = Utility.provideSharedPreferences(this).getString("base_url",null)
        base_url?.let {
            baseUrl!!.setText(it)
            RetrofitConstant.BASE_URL = it
        }
        etEmail!!.setText("admin")
        etPass!!.setText("@admin#599")
        btLogin!!.setOnClickListener {
            if (validation()){
                progressBar!!.visibility = View.VISIBLE
                viewModel.onUserLogin(etEmail!!.text.toString(),etPass!!.text.toString())
            }
        }
        settingIcon!!.setOnClickListener {
            settingDialog!!.show()
        }
        rGroup!!.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.app_setting_rb1 -> {
                    type = 1
                }
                R.id.app_setting_rb2 -> {
                    type = 2
                }
            }
        }
        save!!.setOnClickListener {
            if (!TextUtils.isEmpty(baseUrl!!.text.toString())){
                if (!baseUrl!!.text.toString().isValidUrl()) {
                    Toast.makeText(this,"Enter valid url",Toast.LENGTH_LONG).show()
                }else{
                    Utility.provideSharedPreferences(this).edit()
                            .putString("base_url",baseUrl!!.text.toString())
                            .putInt("sim_type",type)
                            .apply()
                    RetrofitConstant.BASE_URL = baseUrl!!.text.toString()
                    settingDialog!!.dismiss()
                }
            }else{
                Utility.provideSharedPreferences(this).edit()
                        .putInt("sim_type",type)
                        .apply()
                settingDialog!!.dismiss()
            }
        }
        settingDialog!!.findViewById<ImageView>(R.id.app_setting_cancel).setOnClickListener {
            settingDialog!!.dismiss()
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

    override fun onStart() {
        super.onStart()
    }

}