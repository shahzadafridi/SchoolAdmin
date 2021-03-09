package com.empire.adminschool.Data

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.brikmas.balochtransport.Data.Network.EndPoints
import com.empire.adminschool.Activities.MainActivity
import com.empire.adminschool.Interfaces.EmployeeInterface
import com.empire.adminschool.Interfaces.StudentInterface
import com.empire.adminschool.Models.ClassesResponse
import com.empire.adminschool.Models.Employee
import com.empire.adminschool.Models.LoginResponse
import com.empire.adminschool.Models.StudentResponse
import com.empire.adminschool.Models.attendence.AttendenceInResponse
import com.empire.adminschool.Models.attendence.AttendenceOutResponse
import com.empire.adminschool.Models.employee.EmployeeResponse
import com.empire.adminschool.Util.Utility
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response

class AttendenceRepository(
    val apiEndpoints: EndPoints,
    val context: Context
) {

    val TAG = "MainRepository"

    fun attendenceIn(qrCode: String, schooldId: String, liveData: MutableLiveData<AttendenceInResponse?>) {
        apiEndpoints.attendanceIn(qrCode, schooldId).enqueue(object : retrofit2.Callback<AttendenceInResponse> {

            override fun onResponse(call: Call<AttendenceInResponse>, response: Response<AttendenceInResponse>) {
                var res: AttendenceInResponse? = response.body()
                if (res != null) {
                    if(res.status == 200){
                        Log.e(TAG,"attendence in successfully.")
                        liveData.value = res
                    }
                } else {
                    Log.e(TAG, "response body null")
                    liveData.value = null
                }
            }

            override fun onFailure(call: Call<AttendenceInResponse>, t: Throwable) {
                Log.e(TAG,t.message.toString())
                liveData.value = null
            }
        })
    }

    fun attendenceOut(qrCode: String, schooldId: String, liveData: MutableLiveData<AttendenceOutResponse?>) {
        apiEndpoints.attendanceOut(qrCode, schooldId).enqueue(object : retrofit2.Callback<AttendenceOutResponse> {

            override fun onResponse(call: Call<AttendenceOutResponse>, response: Response<AttendenceOutResponse>) {
                var res: AttendenceOutResponse? = response.body()
                if (res != null) {
                    if(res.status == 200){
                        Log.e(TAG,"attendence out successfully.")
                    }
                } else {
                    Log.e(TAG, "response body null")
                }
            }

            override fun onFailure(call: Call<AttendenceOutResponse>, t: Throwable) {
                Log.e(TAG,t.message.toString())
            }
        })
    }

}