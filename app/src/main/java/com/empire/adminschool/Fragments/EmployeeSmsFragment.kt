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
import com.empire.adminschool.Adapters.EmployeesAdapter
import com.empire.adminschool.Adapters.StudendsAdapter
import com.empire.adminschool.Adapters.TypeStringAdapter
import com.empire.adminschool.EmployeeInterface
import com.empire.adminschool.StudentInterface
import com.empire.adminschool.Models.Classes
import com.empire.adminschool.Models.Employee
import com.empire.adminschool.Models.Student
import com.empire.adminschool.MyApplication
import com.empire.adminschool.R
import com.empire.adminschool.Util.Utility
import com.empire.adminschool.ViewModels.MainViewModel

class EmployeeSmsFragment : Fragment(), View.OnClickListener, EmployeeInterface {

    val TAG = "EmployeeSmsFragment"
    private lateinit var viewModel: MainViewModel
    var type: List<String> = arrayListOf()
    var typeAdapter: TypeStringAdapter? = null
    var typeSpinner: Spinner? = null
    var employees: List<Employee> = arrayListOf()
    var employeeAdapter: EmployeesAdapter? = null
    var employeeSpinner: Spinner? = null
    var school: TextView? = null
    var message: EditText? = null
    var sendButton: Button? = null
    var simType = 1
    var progressBar: ProgressBar? = null

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
        school = view.findViewById(R.id.employee_school_tv)
        message = view.findViewById(R.id.employee_message_et)
        sendButton = view.findViewById(R.id.employee_btn)
        sendButton!!.setOnClickListener(this)

        var mSchool = MyApplication.loginResponse!!.school
        school!!.text = mSchool.name

        progressBar!!.visibility = View.VISIBLE

        viewModel.getEmployeeTypes(this)
    }

    override fun onGetEmployees(employees: List<Employee>) {
        if (employees.size > 0) {
            this.employees = employees
            employeeAdapter!!.setEmployeeList(employees)
        }
    }

    override fun onGetEmployeeTypes(types: List<String>) {
        if (types.size > 0) {
            this.type = types
            typeAdapter!!.setTypeStringList(types)
            viewModel.getCurrentEmployees(MyApplication.loginResponse!!.school.id,this)
        }
    }

    override fun onError(error: String) {
        Log.e(TAG, error)
    }

    fun validation(): Boolean {

        var isValid = true

        if (TextUtils.isEmpty(school!!.text.toString())){
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
                    for (emp in employees)
                        phones.add(emp.mobile)
//                    viewModel.sendDirectSMS(
//                        requireActivity(), viewModel.getSIMProvider(simType, requireActivity()), message!!.text.toString(), employees
//                    )
                }
            }
        }
    }
}