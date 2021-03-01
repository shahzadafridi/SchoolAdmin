package com.empire.adminschool.ViewModels

import android.app.Activity
import android.app.PendingIntent
import android.content.*
import android.os.Build
import android.telephony.SmsManager
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brikmas.balochtransport.Data.Network.RetrofitConstant
import com.empire.adminschool.Data.MainRepository
import com.empire.adminschool.EmployeeInterface
import com.empire.adminschool.StudentInterface
import com.empire.adminschool.Models.Student
import com.empire.adminschool.Util.AppPermissions
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    val TAG = "MainViewModel"
    val SENT = "SMS_SENT"
    val DELIVERED = "SMS_DELIVERED"
    lateinit var repository: MainRepository
    lateinit var sentPI: PendingIntent
    lateinit var deliveredPI: PendingIntent
    private var repeatableJob: Job? = null

    fun injectRepository(activity: FragmentActivity) {
        repository = MainRepository(RetrofitConstant.getEndPoints(),activity)
        sentPI = PendingIntent.getBroadcast(activity, 0, Intent(SENT), 0)
        deliveredPI = PendingIntent.getBroadcast(activity, 0, Intent(DELIVERED), 0)
    }

    fun getClasses(id: String, itrface: StudentInterface){
        repository.onClasses(id,itrface)
    }

    fun getStudents(cls: String, session: String, itrface: StudentInterface){
        repository.onStudents(cls,session,itrface)
    }

    fun getEmployeeTypes(employeeInterface: EmployeeInterface){
        employeeInterface.onGetEmployeeTypes(arrayListOf("Current","Ex"))
    }

    fun getCurrentEmployees(id: String, itrface: EmployeeInterface){
        repository.onCurrentEmployees(id,itrface)
    }

    fun getExEmployees(id: String, itrface: EmployeeInterface){
        repository.onExEmployees(id,itrface)
    }

    fun registerSMSBR(activity: Activity){
        // SEND BroadcastReceiver
        val sendSMSBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(arg0: Context?, arg1: Intent?) {
                when (resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        Log.e("test","SEND_REMINDER_SMS_APP_SUCCESS")
                    }
                    SmsManager.RESULT_ERROR_GENERIC_FAILURE -> {
                        Log.e("test","SEND_REMINDER_SMS_APP_FAILED")
                    }
                    SmsManager.RESULT_ERROR_NO_SERVICE -> {
                        Log.e("test","SEND_REMINDER_SMS_APP_FAILED")
                    }
                    SmsManager.RESULT_ERROR_NULL_PDU -> {
                        Log.e("test","SEND_REMINDER_SMS_APP_FAILED")
                    }
                    SmsManager.RESULT_ERROR_RADIO_OFF -> {
                        Log.e("test","SEND_REMINDER_SMS_APP_FAILED")
                    }
                }
            }
        }

        // DELIVERY BroadcastReceiver
        val deliverSMSBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(arg0: Context?, arg1: Intent?) {
                when (resultCode) {
                    AppCompatActivity.RESULT_OK -> Toast.makeText(activity, "Sms delivered", Toast.LENGTH_SHORT).show()
                    AppCompatActivity.RESULT_CANCELED -> Toast.makeText(activity, "Sms not delivered", Toast.LENGTH_SHORT).show()
                }
            }
        }

        activity.registerReceiver(sendSMSBroadcastReceiver, IntentFilter(SENT))
        activity.registerReceiver(deliverSMSBroadcastReceiver, IntentFilter(DELIVERED))
    }

    fun getSIMProvider(simType: Int,activity: Activity): SubscriptionInfo? {
        var sim: SubscriptionInfo? = null

        if (AppPermissions.checkReadPhoneStatePermission(activity)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                val localSubscriptionManager = SubscriptionManager.from(activity)
                if (localSubscriptionManager.activeSubscriptionInfoCount > 1) {
                    val localList: List<*> = localSubscriptionManager.activeSubscriptionInfoList

                    when(simType){
                        1 -> sim = localList[0] as SubscriptionInfo
                        2 -> sim = localList[1] as SubscriptionInfo
                    }
                }
            } else {
                sim = null
            }
        }else{
            Log.e(TAG,"Permission not granted.")
        }

        return sim
    }

    fun sendDirectSMS(activity: Activity, sim: SubscriptionInfo?, smsText: String, phoneNos: List<String>){
        //SendSMS From SIM
        if (AppPermissions.checkReadPhoneStatePermission(activity)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                repeatableJob = viewModelScope.launch {
                    while (isActive) {
                        for (phone in phoneNos){
                            if (sim != null)
                                Log.e(TAG,"Message sent from selected sim.")
//                                SmsManager.getSmsManagerForSubscriptionId(sim.subscriptionId).sendTextMessage(phone, null, smsText, sentPI, deliveredPI)
                            else
                                Log.e(TAG,"Message sent from default sim.")
//                                SmsManager.getDefault().sendTextMessage(phone, null, smsText, sentPI, deliveredPI)
                            delay(5_000)
                        }
                        cancelSendDriectSMS()
                    }
                }
                repeatableJob?.start()
            }
        }
    }

    fun cancelSendDriectSMS(){
        repeatableJob?.cancel()
    }

}