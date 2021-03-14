package com.empire.adminschool.ViewModels

import android.app.Activity
import android.app.PendingIntent
import android.content.*
import android.os.Build
import android.telephony.SmsManager
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.empire.adminschool.Data.Repositories.MainRepository
import com.empire.adminschool.Data.Remote.RetrofitConstant
import com.empire.adminschool.Interfaces.EmployeeInterface
import com.empire.adminschool.Models.Employee
import com.empire.adminschool.Interfaces.StudentInterface
import com.empire.adminschool.Models.Student
import com.empire.adminschool.Util.AppPermissions

class MainViewModel : ViewModel() {

    val TAG = "MainViewModel"
    val SENT = "SMS_SENT"
    val DELIVERED = "SMS_DELIVERED"
    lateinit var repository: MainRepository

    fun injectRepository(activity: FragmentActivity) {
        repository = MainRepository(RetrofitConstant.getEndPoints(), activity)
    }

    fun getClasses(id: String, itrface: StudentInterface) {
        repository.onClasses(id, itrface)
    }

    fun getStudents(cls: String, session: String, itrface: StudentInterface) {
        repository.onStudents(cls, session, itrface)
    }

    fun getEmployeeTypes(employeeInterface: EmployeeInterface) {
        employeeInterface.onGetEmployeeTypes(arrayListOf("Current", "Ex"))
    }

    fun getCurrentEmployees(id: String, itrface: EmployeeInterface) {
        repository.onCurrentEmployees(id, itrface)
    }

    fun getExEmployees(id: String, itrface: EmployeeInterface) {
        repository.onExEmployees(id, itrface)
    }

    fun addStudentSmsHistory(school: String, student_name: String, mobile: String, message: String, format: String){
        repository.onStudentSmsHistory(school,student_name,mobile,message,format)
    }

    fun addEmployeeSmsHistory(school: String, student_name: String, mobile: String, message: String, format: String){
        repository.onStudentSmsHistory(school,student_name,mobile,message,format)
    }

    fun getSIMProvider(simType: Int, activity: Activity): SubscriptionInfo? {
        var sim: SubscriptionInfo? = null

        if (AppPermissions.checkReadPhoneStatePermission(activity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                val localSubscriptionManager = SubscriptionManager.from(activity)
                if (localSubscriptionManager.activeSubscriptionInfoCount > 1) {
                    val localList: List<*> = localSubscriptionManager.activeSubscriptionInfoList

                    when (simType) {
                        1 -> sim = localList[0] as SubscriptionInfo
                        2 -> sim = localList[1] as SubscriptionInfo
                    }
                }
            } else {
                sim = null
            }
        } else {
            Log.e(TAG, "Permission not granted.")
        }

        return sim
    }

    fun sendDirectSMS(activity: Activity, sim: SubscriptionInfo?, smsText: String, student: Student?, employee: Employee?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            var phone = ""
            var name = ""
            if (student != null){
                phone = student.mobile
                name = student.name
            }else{
                phone = employee!!.mobile
                name = employee!!.name
            }
            var sentIntent = Intent(SENT)
            sentIntent.putExtra("name", name)
            sentIntent.putExtra("mobile", phone)
            var delieverIntent = Intent(DELIVERED)
            var sentPI = PendingIntent.getBroadcast(activity, 0, Intent(SENT), 0)
            var deliveredPI = PendingIntent.getBroadcast(activity, 0, Intent(DELIVERED), 0)

            if (sim != null) {
                Log.e(TAG, "Message sent from selected sim.")
                SmsManager.getSmsManagerForSubscriptionId(sim!!.subscriptionId).sendTextMessage(phone, null, smsText, sentPI, deliveredPI)
            } else {
                Log.e(TAG, "Message sent from default sim.")
                SmsManager.getDefault().sendTextMessage(phone, null, smsText, sentPI, deliveredPI)
            }
        }
    }
}