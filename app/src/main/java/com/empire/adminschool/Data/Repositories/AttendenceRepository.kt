package com.empire.adminschool.Data.Repositories

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.empire.adminschool.Data.Remote.EndPoints
import com.empire.adminschool.Models.attendence.AttendenceInResponse
import com.empire.adminschool.Models.attendence.AttendenceOutResponse
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
                    }else if (res.status == 400){
                        Toast.makeText(context,"The server could not understand the request due to invalid syntax.", Toast.LENGTH_SHORT).show()
                    }else if (res.status == 401){
                        Toast.makeText(context,"The client must authenticate itself to get the requested response", Toast.LENGTH_SHORT).show()
                    }else if (res.status == 403){
                        Toast.makeText(context,"The client does not have access rights to the content.", Toast.LENGTH_SHORT).show()
                    }else if (res.status == 503){
                        Toast.makeText(context,"The server is not ready to handle the request.", Toast.LENGTH_SHORT).show()
                    }else if (res.status == 404){
                        Toast.makeText(context,"The server can not find the requested resource.", Toast.LENGTH_SHORT).show()
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
                        liveData.value = res
                    }else if (res.status == 400){
                        Toast.makeText(context,"The server could not understand the request due to invalid syntax.",Toast.LENGTH_SHORT).show()
                    }else if (res.status == 401){
                        Toast.makeText(context,"The client must authenticate itself to get the requested response",Toast.LENGTH_SHORT).show()
                    }else if (res.status == 403){
                        Toast.makeText(context,"The client does not have access rights to the content.",Toast.LENGTH_SHORT).show()
                    }else if (res.status == 503){
                        Toast.makeText(context,"The server is not ready to handle the request.",Toast.LENGTH_SHORT).show()
                    }else if (res.status == 404){
                        Toast.makeText(context,"The server can not find the requested resource.",Toast.LENGTH_SHORT).show()
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