package com.empire.adminschool.Fragments

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.empire.adminschool.R
import com.empire.adminschool.Util.FragmentStack
import com.empire.adminschool.Util.Utility
import com.empire.adminschool.ViewModels.MainViewModel


class SMSFragment : Fragment() {

    val TAG = "SMSFragment"
    val DELAY = 2000
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_s_m_s, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.injectRepository(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.sms_frag_emp_iv).setOnClickListener {
            FragmentStack.replaceFragmentToContainer(R.id.main_container,requireActivity().supportFragmentManager,
                EmployeeSmsFragment(),"EmployeeSmsFragment")
        }

        view.findViewById<ImageView>(R.id.sms_frag_student_iv).setOnClickListener {
            FragmentStack.replaceFragmentToContainer(R.id.main_container,requireActivity().supportFragmentManager,
                StudentSmsFragment(),"StudentSmsFragment")
        }

        view.findViewById<ImageView>(R.id.sms_frag_back).setOnClickListener {
            requireActivity().onBackPressed()
        }

    }

}