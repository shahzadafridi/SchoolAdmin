package com.empire.adminschool.Fragments

import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telephony.SmsManager
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.empire.adminschool.Adapters.EmployeesAdapter
import com.empire.adminschool.Adapters.TypeStringAdapter
import com.empire.adminschool.Interfaces.EmployeeInterface
import com.empire.adminschool.Models.Employee
import com.empire.adminschool.Models.SentSMS
import com.empire.adminschool.MyApplication
import com.empire.adminschool.R
import com.empire.adminschool.Util.Utility
import com.empire.adminschool.ViewModels.MainViewModel

class EmployeeSmsFragment : Fragment(), View.OnClickListener, EmployeeInterface {

    val TAG = "EmployeeSmsFragment"
    val SENT = "SMS_SENT"
    val DELIVERED = "SMS_DELIVERED"
    private lateinit var viewModel: MainViewModel
    var type: List<String> = arrayListOf()
    var typeAdapter: TypeStringAdapter? = null
    var typeSpinner: Spinner? = null
    var employees: MutableList<Employee> = arrayListOf()
    var selectedEmployees: MutableList<Employee> = arrayListOf()
    var employeeAdapter: EmployeesAdapter? = null
    var employeeSpinner: Spinner? = null
    var message: EditText? = null
    var selectedSimTv: TextView? = null
    var sendButton: Button? = null
    var simType = 1
    var progressBar: ProgressBar? = null
    var smsLiveData = MutableLiveData<SentSMS>()
    var counter = 0
    var selectedEmployeeSize = 0
    var status: String = ""
    var sendSMSDialog: Dialog? = null
    var name: TextView? = null
    var messageStatus: TextView? = null
    var dialgoProgressBar: ProgressBar? = null
    var minimum: TextView? = null
    var max: TextView? = null
    var greenTickLL: LinearLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var root = inflater.inflate(R.layout.fragment_employee_sms, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.injectRepository(requireActivity())
        viewModel.getEmployeeTypes(this)
        registerSMSBR(requireActivity())
        smsLiveData.observe(viewLifecycleOwner,{
            Log.e(TAG,"smsLiveData:" + it.status)
            it?.let {
                minimum!!.text = it.count.toString()
                dialgoProgressBar!!.progress = it.count
                messageStatus!!.text = it.status+ " to " + it.name
                if (counter == dialgoProgressBar!!.max){
                    greenTickLL!!.visibility = View.VISIBLE
                    Handler(Looper.getMainLooper()).postDelayed({
                        sendSMSDialog!!.dismiss()
                    },4000)
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = view.findViewById(R.id.employee_progessBar)
        typeSpinner = view.findViewById(R.id.employee_spinner_type)
        typeAdapter = TypeStringAdapter(requireActivity())
        typeSpinner!!.adapter = typeAdapter
        employeeSpinner = view.findViewById(R.id.employee_spinner_emp)
        employeeAdapter = EmployeesAdapter(requireActivity())
        employeeSpinner!!.adapter = employeeAdapter
        message = view.findViewById(R.id.employee_message_et)
        selectedSimTv = view.findViewById(R.id.emp_send_sms_select_sim_tv)
        view.findViewById<LinearLayout>(R.id.employee_select_sim_ll).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.employee_sms_back).setOnClickListener(this)
        selectedSimTv!!.text = "SIM 1"
        sendButton = view.findViewById(R.id.employee_send_btn)
        sendButton!!.setOnClickListener(this)
        progressBar!!.visibility = View.VISIBLE
        typeSpinner!!.onItemSelectedListener =  object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                progressBar!!.visibility = View.VISIBLE
                if (position == 0){
                    viewModel.getCurrentEmployees(MyApplication.loginResponse!!.school.id,this@EmployeeSmsFragment)
                }else{
                    viewModel.getExEmployees(MyApplication.loginResponse!!.school.id,this@EmployeeSmsFragment)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        //Send SMS Dialog.
        sendSMSDialog = Utility.onCreateDialog(requireContext(),R.layout.progress_dialog_layout,false)
        name = sendSMSDialog!!.findViewById<TextView>(R.id.progress_dialog_name)
        messageStatus = sendSMSDialog!!.findViewById<TextView>(R.id.progress_dialog_message)
        dialgoProgressBar = sendSMSDialog!!.findViewById<ProgressBar>(R.id.progress_dialog_pbar)
        minimum = sendSMSDialog!!.findViewById<TextView>(R.id.progress_dialog_min)
        max = sendSMSDialog!!.findViewById<TextView>(R.id.progress_dialog_max)
        greenTickLL = sendSMSDialog!!.findViewById(R.id.progress_dialog_green_tick_ll)
    }

    override fun onGetEmployees(list: List<Employee>) {
        this.employees.clear()
        if (list.size > 0) {
            this.employees = list.toMutableList()
            employees.add(0, Employee("", "Name", "", "", "Mobile", false))
            employees.add(1, Employee("", "Shahzad Afridi", "", "+923339218035", "", false))
            employees.add(2, Employee("", "Hizbullah", "", "+923451926814", "", false))
            employeeAdapter!!.setEmployeeList(employees.toMutableList())
            progressBar!!.visibility = View.GONE
        }
    }

    override fun onGetEmployeeTypes(types: List<String>) {
        if (types.size > 0) {
            this.type = types
            typeAdapter!!.setTypeStringList(types)
            if (typeSpinner!!.selectedItemPosition == 0){
                viewModel.getCurrentEmployees(MyApplication.loginResponse!!.school.id,this)
            }else{
                viewModel.getExEmployees(MyApplication.loginResponse!!.school.id,this)
            }
        }
    }

    override fun onError(error: String) {
        Log.e(TAG, error)
    }

    fun validation(): Boolean {

        var isValid = true

        if (TextUtils.isEmpty(message!!.text.toString())){
            isValid = false
        }

        return isValid
    }

    override fun onClick(p0: View?) {

        when (p0!!.id) {

            R.id.employee_select_sim_ll -> {
                var dialog = Utility.onCreateDialog(requireContext(), R.layout.sim_select_layout, true)
                var sim1 = dialog!!.findViewById<LinearLayout>(R.id.sim_select_sim1)
                var sim2 = dialog.findViewById<LinearLayout>(R.id.sim_select_sim2)
                var cancelBtn = dialog.findViewById<Button>(R.id.sim_dialog_cancel)
                sim1.setOnClickListener {
                    simType = 1
                    selectedSimTv!!.text = "SIM $simType"
                    dialog.dismiss()
                }
                sim2.setOnClickListener {
                    simType = 2
                    selectedSimTv!!.text = "SIM $simType"
                    dialog.dismiss()
                }
                cancelBtn.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.show()
            }

            R.id.employee_send_btn -> {
                if (validation()){
                    selectedEmployeeSize = employeeAdapter!!.getSelectedEmployee().size
                    selectedEmployees = employeeAdapter!!.getSelectedEmployee().toMutableList()
                    if (selectedEmployees.get(0).mobile.contentEquals("Mobile")){
                        selectedEmployees.removeAt(0)
                    }
                    max!!.text = selectedEmployeeSize.toString()
                    dialgoProgressBar!!.progress = 0
                    dialgoProgressBar!!.max = selectedEmployeeSize
                    if (selectedEmployeeSize > 0){
                        sendSMSDialog!!.show()
                        viewModel.sendDirectSMS(
                                requireActivity(), viewModel.getSIMProvider(simType, requireActivity()),
                                message!!.text.toString(), null, selectedEmployees.get(0)
                        )
                    }else{
                        Toast.makeText(requireContext(),"Please select employee to send sms",Toast.LENGTH_SHORT).show()
                    }
                }
            }

            R.id.employee_sms_back -> {
                requireActivity().onBackPressed()
            }
        }
    }

    fun registerSMSBR(activity: Activity){

        // SEND BroadcastReceiver
        val sendSMSBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(arg0: Context?, arg1: Intent?) {
                when (resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        counter = counter + 1
                        status = "Sent successfully"
                        if (selectedEmployees.size > 0){
                            smsLiveData.value = SentSMS(counter,status,selectedEmployees[0].name)
                            selectedEmployees.removeAt(0)
                            if (selectedEmployees.size > 0)
                                viewModel.sendDirectSMS(
                                        requireActivity(), viewModel.getSIMProvider(simType, requireActivity()),
                                        message!!.text.toString(),null, selectedEmployees.get(0)
                                )
                        }else{
                            greenTickLL!!.visibility = View.VISIBLE
                            Handler(Looper.getMainLooper()).postDelayed({
                                sendSMSDialog!!.dismiss()
                            },2000)
                        }

                        Log.e("test","SEND_REMINDER_SMS_APP_SUCCESS ")
                    }
                    SmsManager.RESULT_ERROR_GENERIC_FAILURE -> {
                        status = "Message sent failed"
                        smsLiveData.value = SentSMS(counter,status,"")
                        Log.e("test","SEND_REMINDER_SMS_APP_FAILED")
                    }
                    SmsManager.RESULT_ERROR_NO_SERVICE -> {
                        status = "Message sent failed"
                        smsLiveData.value = SentSMS(counter,status,"")
                        Log.e("test","SEND_REMINDER_SMS_APP_FAILED")
                    }
                    SmsManager.RESULT_ERROR_NULL_PDU -> {
                        status = "Message sent failed"
                        smsLiveData.value = SentSMS(counter,status,"")
                        Log.e("test","SEND_REMINDER_SMS_APP_FAILED")
                    }
                    SmsManager.RESULT_ERROR_RADIO_OFF -> {
                        status = "Message sent failed"
                        smsLiveData.value = SentSMS(counter,status,"")
                        Log.e("test","SEND_REMINDER_SMS_APP_FAILED")
                    }
                }
            }
        }

        // DELIVERY BroadcastReceiver
        val deliverSMSBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(arg0: Context?, arg1: Intent?) {
                when (resultCode) {
                    AppCompatActivity.RESULT_OK -> Toast.makeText(activity, "Message delivered", Toast.LENGTH_SHORT).show()
                    AppCompatActivity.RESULT_CANCELED -> Toast.makeText(activity, "Message not delivered", Toast.LENGTH_SHORT).show()
                }
            }
        }

        activity.registerReceiver(sendSMSBroadcastReceiver, IntentFilter(SENT))
        activity.registerReceiver(deliverSMSBroadcastReceiver, IntentFilter(DELIVERED))
    }
}