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
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.empire.adminschool.Adapters.ClassesAdapter
import com.empire.adminschool.Adapters.StudendsAdapter
import com.empire.adminschool.Interfaces.StudentInterface
import com.empire.adminschool.Models.Classes
import com.empire.adminschool.Models.SentSMS
import com.empire.adminschool.Models.Student
import com.empire.adminschool.MyApplication
import com.empire.adminschool.R
import com.empire.adminschool.Util.AppPermissions
import com.empire.adminschool.Util.Utility
import com.empire.adminschool.ViewModels.MainViewModel
import java.util.*


class StudentSmsFragment : Fragment(), View.OnClickListener, StudentInterface {

    val TAG = "StudentSmsFragment"
    val SENT = "SMS_SENT"
    val DELIVERED = "SMS_DELIVERED"
    private lateinit var viewModel: MainViewModel
    var classes: List<Classes> = arrayListOf()
    var students: MutableList<Student> = arrayListOf()
    var selectedStudents: MutableList<Student> = arrayListOf()
    var adapter: ClassesAdapter? = null
    var classesSpinner: Spinner? = null
    var studentAdapter: StudendsAdapter? = null
    var studentSpinner: Spinner? = null
    var message: EditText? = null
    var sendButton: Button? = null
    var simType = 1
    var selectedSimTv: TextView? = null
    var progressBar: ProgressBar? = null
    var sendSMSDialog: Dialog? = null
    var name: TextView? = null
    var messageStatus: TextView? = null
    var dialgoProgressBar: ProgressBar? = null
    var minimum: TextView? = null
    var max: TextView? = null
    var selectedStudentsSize = 0
    var smsLiveData = MutableLiveData<SentSMS>()
    var counter = 0
    var status: String = ""
    var greenTickLL: LinearLayout? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var root = inflater.inflate(R.layout.fragment_student_sms, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.injectRepository(requireActivity())
        viewModel.getClasses(MyApplication.loginResponse!!.school.id, this)
        registerSMSBR(requireActivity())

        smsLiveData.observe(viewLifecycleOwner, {
            Log.e(TAG, "smsLiveData:" + it.status)
            it?.let {
                minimum!!.text = it.count.toString()
                dialgoProgressBar!!.progress = it.count
                messageStatus!!.text = it.status + " to " + it.name
                if (counter == dialgoProgressBar!!.max) {
                    greenTickLL!!.visibility = View.VISIBLE
                    Handler(Looper.getMainLooper()).postDelayed({
                        sendSMSDialog!!.dismiss()
                    }, 4000)
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = view.findViewById(R.id.send_sms_progessBar)
        studentSpinner = view.findViewById(R.id.send_sms_spinner_stdent)
        studentAdapter = StudendsAdapter(requireActivity())
        studentSpinner!!.adapter = studentAdapter
        classesSpinner = view.findViewById(R.id.send_sms_spinner)
        adapter = ClassesAdapter(requireActivity())
        classesSpinner!!.adapter = adapter
        message = view.findViewById(R.id.send_sms_message_et)
        sendButton = view.findViewById(R.id.send_sms_btn)
        sendButton!!.setOnClickListener(this)
        selectedSimTv = view.findViewById(R.id.send_sms_select_sim_tv)
        view.findViewById<LinearLayout>(R.id.send_sms_select_sim_ll).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.send_sms_back).setOnClickListener(this)
        selectedSimTv!!.text = "SIM 1"
        progressBar!!.visibility = View.VISIBLE

        classesSpinner!!.onItemSelectedListener =  object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                progressBar!!.visibility = View.VISIBLE
                viewModel.getStudents(
                        classes.get(position).id,
                        MyApplication.loginResponse!!.school.current_session,
                        this@StudentSmsFragment
                )
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        view.findViewById<RadioGroup>(R.id.send_sms_rg)!!.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.send_sms_rg_english -> {
                    message!!.setText("Enter your message...")
                    message!!.gravity = Gravity.LEFT or Gravity.TOP
                }
                R.id.send_sms_rg_urdu -> {
                    message!!.setText("اپنا پیغام درج کریں")
                    message!!.gravity = Gravity.RIGHT or Gravity.TOP
                }
            }
        }

        //Send SMS Dialog.
        sendSMSDialog = Utility.onCreateDialog(requireContext(), R.layout.progress_dialog_layout, false)
        name = sendSMSDialog!!.findViewById<TextView>(R.id.progress_dialog_name)
        messageStatus = sendSMSDialog!!.findViewById<TextView>(R.id.progress_dialog_message)
        dialgoProgressBar = sendSMSDialog!!.findViewById<ProgressBar>(R.id.progress_dialog_pbar)
        minimum = sendSMSDialog!!.findViewById<TextView>(R.id.progress_dialog_min)
        max = sendSMSDialog!!.findViewById<TextView>(R.id.progress_dialog_max)
        greenTickLL = sendSMSDialog!!.findViewById(R.id.progress_dialog_green_tick_ll)
    }

    override fun onGetClasses(classes: List<Classes>) {
        if (classes.size > 0) {
            this.classes = classes
            adapter!!.setClassesList(classes)
            viewModel.getStudents(
                    classes.get(0).id,
                    MyApplication.loginResponse!!.school.current_session,
                    this
            )
        }
    }

    override fun onGetStudents(list: List<Student>) {
        students.clear()
        students = list.toMutableList()
        students.add(0, Student("", "Name", "", "", "Mobile", "", false))
        students.add(1, Student("", "Shahzad Afridi", "", "", "+923339218035", "", false))
        students.add(2, Student("", "Hizbullah", "", "", "+923451926814", "", false))
        studentAdapter!!.setStudentsList(students)
        progressBar!!.visibility = View.GONE
    }

    override fun onError(error: String) {
        Log.e(TAG, error)
    }

    fun validation(): Boolean {

        var isValid = true

        if (TextUtils.isEmpty(message!!.text.toString())){
            Toast.makeText(requireContext(), "Enter message", Toast.LENGTH_LONG).show()
            isValid = false
        }

        return isValid
    }

    override fun onClick(p0: View?) {

        when (p0!!.id) {

            R.id.send_sms_select_sim_ll -> {
                var dialog =
                        Utility.onCreateDialog(requireContext(), R.layout.sim_select_layout, true)
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

            R.id.send_sms_btn -> {
                if (!AppPermissions.checkReadPhoneStatePermission(requireActivity()) || !AppPermissions.checkSendSmsPermission(requireActivity())) {
                    return
                }

                if (validation()) {
                    selectedStudentsSize = studentAdapter!!.getSelectedStudents().size
                    selectedStudents = studentAdapter!!.getSelectedStudents().toMutableList()
                    if (selectedStudents.get(0).mobile.contentEquals("Mobile")){
                        selectedStudents.removeAt(0)
                    }
                    max!!.text = selectedStudentsSize.toString()
                    dialgoProgressBar!!.progress = 0
                    dialgoProgressBar!!.max = selectedStudentsSize
                    if (selectedStudentsSize > 0) {
                        sendSMSDialog!!.show()
                        viewModel.sendDirectSMS(
                                requireActivity(), viewModel.getSIMProvider(simType, requireActivity()),
                                message!!.text.toString(), selectedStudents.get(0), null
                        )
                    } else {
                        Toast.makeText(requireContext(), "Please select student to send sms", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            R.id.send_sms_back -> {
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
                        status = "Message sent successfully"
                        if (selectedStudents.size > 0) {
                            smsLiveData.value = SentSMS(counter, status, selectedStudents[0].name)
                            selectedStudents.removeAt(0)
                            if (selectedStudents.size > 0)
                                viewModel.sendDirectSMS(
                                        requireActivity(), viewModel.getSIMProvider(simType, requireActivity()),
                                        message!!.text.toString(), selectedStudents.get(0), null
                                )
                        }

                        Log.e("test", "SEND_REMINDER_SMS_APP_SUCCESS ")
                    }
                    SmsManager.RESULT_ERROR_GENERIC_FAILURE -> {
                        status = "Message sent failed"
                        smsLiveData.value = SentSMS(counter, status, "")
                        Log.e("test", "SEND_REMINDER_SMS_APP_FAILED")
                    }
                    SmsManager.RESULT_ERROR_NO_SERVICE -> {
                        status = "Message sent failed"
                        smsLiveData.value = SentSMS(counter, status, "")
                        Log.e("test", "SEND_REMINDER_SMS_APP_FAILED")
                    }
                    SmsManager.RESULT_ERROR_NULL_PDU -> {
                        status = "Message sent failed"
                        smsLiveData.value = SentSMS(counter, status, "")
                        Log.e("test", "SEND_REMINDER_SMS_APP_FAILED")
                    }
                    SmsManager.RESULT_ERROR_RADIO_OFF -> {
                        status = "Message sent failed"
                        smsLiveData.value = SentSMS(counter, status, "")
                        Log.e("test", "SEND_REMINDER_SMS_APP_FAILED")
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