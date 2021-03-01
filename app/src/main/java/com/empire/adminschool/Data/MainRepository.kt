package com.empire.adminschool.Data

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.brikmas.balochtransport.Data.Network.EndPoints
import com.empire.adminschool.Activities.MainActivity
import com.empire.adminschool.EmployeeInterface
import com.empire.adminschool.StudentInterface
import com.empire.adminschool.Models.ClassesResponse
import com.empire.adminschool.Models.Employee
import com.empire.adminschool.Models.LoginResponse
import com.empire.adminschool.Models.StudentResponse
import com.empire.adminschool.Models.employee.EmployeeResponse
import com.empire.adminschool.Util.Utility
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response

class MainRepository(
    val apiEndpoints: EndPoints,
    val context: Context
) {

    val TAG = "MainRepository"

    fun onUserLogin(email: String, password: String) {
        apiEndpoints.userLogin(email, password).enqueue(object : retrofit2.Callback<LoginResponse> {

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                var res: LoginResponse? = response.body()
                if (res != null) {
                    if(res.status == 200){
                        Log.e(TAG,"Login successfully.")
                        Utility.provideSharedPreferences(context).edit().
                        putBoolean("isLogin",true).
                        putString("login_response", Gson().toJson(res)).
                        apply()
                        Toast.makeText(context,"Login successfully",Toast.LENGTH_SHORT).show()
                        context.startActivity(Intent(context, MainActivity::class.java))
                    }
                } else {
                    Log.e(TAG, "response body null")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(TAG,t.message.toString())
            }
        })
    }

    fun onClasses(schoolId: String, itrface: StudentInterface) {
        apiEndpoints.getClasses(schoolId).enqueue(object : retrofit2.Callback<ClassesResponse> {

            override fun onResponse(call: Call<ClassesResponse>, response: Response<ClassesResponse>) {
                var res: ClassesResponse? = response.body()
                if (res != null) {
                    if(res.status == 200){
                        itrface.onGetClasses(res.classes)
                    }
                } else {
                    itrface.onError("response body null.")
                }
            }

            override fun onFailure(call: Call<ClassesResponse>, t: Throwable) {
                itrface.onError(t.message.toString())
            }
        })
    }

    fun onStudents(cls: String, session: String, itrface: StudentInterface) {
        apiEndpoints.getStudents(cls,session).enqueue(object : retrofit2.Callback<StudentResponse> {

            override fun onResponse(call: Call<StudentResponse>, response: Response<StudentResponse>) {
                var res: StudentResponse? = response.body()
                if (res != null) {
                    if(res.status == 200){
                        itrface.onGetStudents(res.studetns)
                    }
                } else {
                    itrface.onError("response body null.")
                }
            }

            override fun onFailure(call: Call<StudentResponse>, t: Throwable) {
                itrface.onError(t.message.toString())
            }
        })
    }

    fun onCurrentEmployees(id: String, itrface: EmployeeInterface) {
        apiEndpoints.getCurrentEmpolyees(id).enqueue(object : retrofit2.Callback<EmployeeResponse> {

            override fun onResponse(call: Call<EmployeeResponse>, response: Response<EmployeeResponse>) {
                var res: EmployeeResponse? = response.body()
                if (res != null) {
                    if(res.status == 200){
                        var emps: MutableList<Employee> = arrayListOf()
                        for (cls in res.classes)
                            emps.add(Employee(cls.id,cls.name,cls.father,cls.mobile))
                        itrface.onGetEmployees(emps)
                    }
                } else {
                    itrface.onError("response body null.")
                }
            }

            override fun onFailure(call: Call<EmployeeResponse>, t: Throwable) {
                itrface.onError(t.message.toString())
            }
        })
    }

    fun onExEmployees(id: String, itrface: EmployeeInterface) {
        apiEndpoints.getExEmpolyees(id).enqueue(object : retrofit2.Callback<EmployeeResponse> {

            override fun onResponse(call: Call<EmployeeResponse>, response: Response<EmployeeResponse>) {
                var res: EmployeeResponse? = response.body()
                if (res != null) {
                    if(res.status == 200){
                        var emps: MutableList<Employee> = arrayListOf()
                        for (cls in res.classes)
                            emps.add(Employee(cls.id,cls.name,cls.father,cls.mobile))
                        itrface.onGetEmployees(emps)
                    }
                } else {
                    itrface.onError("response body null.")
                }
            }

            override fun onFailure(call: Call<EmployeeResponse>, t: Throwable) {
                itrface.onError(t.message.toString())
            }
        })
    }

}