package com.empire.adminschool.ViewModels

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.empire.adminschool.Data.Repositories.AttendenceRepository
import com.empire.adminschool.Data.Remote.RetrofitConstant
import com.empire.adminschool.Models.attendence.AttendenceInResponse
import com.empire.adminschool.Models.attendence.AttendenceOutResponse
import com.empire.adminschool.Util.Constants
import com.empire.adminschool.Util.Utility

class AttendenceViewModel : ViewModel() {

    lateinit var repository: AttendenceRepository
    var attendenceInLiveData = MutableLiveData<AttendenceInResponse?>()
    var attendenceOutLiveData = MutableLiveData<AttendenceOutResponse?>()
    var activity: FragmentActivity? = null

    fun injectRepository(activity: FragmentActivity) {
        this.activity = activity
    }

    fun attendenceIn(schoolId: String, qrCode: String) {
         RetrofitConstant.BASE_URL = Utility.provideSharedPreferences(activity!!).getString("base_url",RetrofitConstant.BASE_URL)!!
         repository = AttendenceRepository(RetrofitConstant.getEndPoints(), activity!!)
         repository.attendenceIn(schoolId,qrCode, attendenceInLiveData)
    }

    fun attendenceOut(schoolId: String, qrCode: String) {
        RetrofitConstant.BASE_URL = Utility.provideSharedPreferences(activity!!).getString("base_url",RetrofitConstant.BASE_URL)!!
        repository = AttendenceRepository(RetrofitConstant.getEndPoints(), activity!!)
        repository.attendenceOut(schoolId,qrCode,attendenceOutLiveData)
    }
}