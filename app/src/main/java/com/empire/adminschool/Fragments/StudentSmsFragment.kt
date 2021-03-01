package com.empire.adminschool.Fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.empire.adminschool.Adapters.ClassesAdapter
import com.empire.adminschool.Adapters.StudendsAdapter
import com.empire.adminschool.StudentInterface
import com.empire.adminschool.Models.Classes
import com.empire.adminschool.Models.Student
import com.empire.adminschool.MyApplication
import com.empire.adminschool.R
import com.empire.adminschool.Util.Utility
import com.empire.adminschool.ViewModels.MainViewModel

class StudentSmsFragment : Fragment(), View.OnClickListener, StudentInterface {

    val TAG = "StudentSmsFragment"
    private lateinit var viewModel: MainViewModel
    var classes: List<Classes> = arrayListOf()
    var students: List<Student> = arrayListOf()
    var adapter: ClassesAdapter? = null
    var classesSpinner: Spinner? = null
    var studentAdapter: StudendsAdapter? = null
    var studentSpinner: Spinner? = null
    var school: TextView? = null
    var session: TextView? = null
    var message: EditText? = null
    var sendButton: Button? = null
    var simType = 1
    var progressBar: ProgressBar? = null

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
        school = view.findViewById(R.id.send_sms_school_tv)
        session = view.findViewById(R.id.send_sms_session_tv)
        message = view.findViewById(R.id.send_sms_message_et)
        sendButton = view.findViewById(R.id.send_sms_btn)
        sendButton!!.setOnClickListener(this)

        var mSchool = MyApplication.loginResponse!!.school
        school!!.text = mSchool.name
        session!!.text = mSchool.current_session

        progressBar!!.visibility = View.VISIBLE
        viewModel.getClasses("1", this)
    }

    override fun onGetClasses(classes: List<Classes>) {
        if (classes.size > 0) {
            this.classes = classes
            adapter!!.setClassesList(classes)
        }
    }

    override fun onGetStudents(students: List<Student>) {
        this.students = students
        studentAdapter!!.setStudentsList(students)
        progressBar!!.visibility = View.GONE
    }

    override fun onError(error: String) {
        Log.e(TAG, error)
    }

    fun validation(): Boolean {

        var isValid = true

        if (TextUtils.isEmpty(school!!.text.toString())){
            isValid = false
        }

        if (TextUtils.isEmpty(session!!.text.toString())){
            isValid = false
        }

        if (TextUtils.isEmpty(message!!.text.toString())){
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
                sim1.setOnClickListener {
                    simType = 1
                }
                sim2.setOnClickListener {
                    simType = 2
                }

                dialog.show()
            }

            R.id.send_sms_btn -> {
                if (validation()){
                    progressBar!!.visibility = View.VISIBLE
                    var phones: MutableList<String> = arrayListOf()
                    for (stu in students)
                        phones.add(stu.mobile)
                    viewModel.sendDirectSMS(
                        requireActivity(), viewModel.getSIMProvider(simType, requireActivity()), message!!.text.toString(), phones
                    )
                }
            }
        }
    }
}