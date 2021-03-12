package com.empire.adminschool.ViewModels

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.brikmas.balochtransport.Data.Network.RetrofitConstant
import com.empire.adminschool.Data.AttendenceRepository
import com.empire.adminschool.Data.MainRepository
import com.empire.adminschool.Interfaces.StudentInterface
import com.empire.adminschool.Models.attendence.AttendenceInResponse
import com.empire.adminschool.Models.attendence.AttendenceOutResponse

class AttendenceViewModel : ViewModel() {

    lateinit var repository: AttendenceRepository
    var attendenceInLiveData = MutableLiveData<AttendenceInResponse?>()
    var attendenceOutLiveData = MutableLiveData<AttendenceOutResponse?>()

    fun injectRepository(activity: FragmentActivity) {
        repository = AttendenceRepository(RetrofitConstant.getEndPoints(), activity)
    }

    fun attendenceIn(schoolId: String, qrCode: String) {
         repository.attendenceIn(schoolId,qrCode, attendenceInLiveData)
    }

    fun attendenceOut(schoolId: String, qrCode: String) {
        repository.attendenceOut(schoolId,qrCode,attendenceOutLiveData)
    }
}