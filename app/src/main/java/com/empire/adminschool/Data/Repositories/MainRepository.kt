package com.empire.adminschool.Data.Repositories

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.empire.adminschool.Data.Remote.EndPoints
import com.empire.adminschool.Interfaces.EmployeeInterface
import com.empire.adminschool.Interfaces.StudentInterface
import com.empire.adminschool.Models.*
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
                        Utility.startMainActivity(context)
                    }else if (res.status == 401){
                        Toast.makeText(context,"No permission to user app or service not available",Toast.LENGTH_SHORT).show()
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
                        if(res.students != null)
                            itrface.onGetStudents(res.students)
                        else
                            itrface.onError("No students found.")
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
                            emps.add(Employee(cls.id,cls.name,cls.father,cls.mobile,"",false))
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
                            emps.add(Employee(cls.id,cls.name,cls.father,cls.mobile,"",false))
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

    fun onStudentSmsHistory(school: String, student_name: String, mobile: String, message: String, format: String) {
        apiEndpoints.addStudentSmsHistory(school,student_name,mobile,message,format).enqueue(object : retrofit2.Callback<SMSHistoryResponse> {

            override fun onResponse(call: Call<SMSHistoryResponse>, response: Response<SMSHistoryResponse>) {
                var res: SMSHistoryResponse? = response.body()
                if (res != null) {
                    if(res.status == 200){
                        Log.e(TAG,res.message)
                    }
                } else {
                    Log.e(TAG,"response body null.")
                }
            }

            override fun onFailure(call: Call<SMSHistoryResponse>, t: Throwable) {
                Log.e(TAG,t.message!!)
            }
        })
    }

    fun onEmpSmsHistory(school: String, employee_name: String, mobile: String, message: String, format: String) {
        apiEndpoints.addEmployeeSmsHistory(school,employee_name,mobile,message,format).enqueue(object : retrofit2.Callback<SMSHistoryResponse> {

            override fun onResponse(call: Call<SMSHistoryResponse>, response: Response<SMSHistoryResponse>) {
                var res: SMSHistoryResponse? = response.body()
                if (res != null) {
                    if(res.status == 200){
                        Log.e(TAG,res.message)
                    }
                } else {
                    Log.e(TAG,"response body null.")
                }
            }

            override fun onFailure(call: Call<SMSHistoryResponse>, t: Throwable) {
                Log.e(TAG,t.message!!)
            }
        })
    }

}