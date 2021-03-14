package com.empire.adminschool.Fragments

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.empire.adminschool.MyApplication
import com.empire.adminschool.R
import com.empire.adminschool.Util.FragmentStack
import com.empire.adminschool.Util.Utility
import com.empire.adminschool.ViewModels.MainViewModel
import com.google.gson.Gson


class MainFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    var ivBack: ImageView? = null
    var ivSMS: ImageView? = null
    var ivAttendence: ImageView? = null
    var title: TextView? = null
    var logout: ImageView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var root = inflater.inflate(R.layout.fragment_main, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.injectRepository(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logout = view.findViewById(R.id.main_logout)
        title = view.findViewById(R.id.main_title)
        ivBack = view.findViewById(R.id.main_back)
        ivSMS = view.findViewById(R.id.main_sms_iv)
        ivAttendence = view.findViewById(R.id.main_attend_iv)
        ivBack!!.setOnClickListener(this)
        ivSMS!!.setOnClickListener(this)
        ivAttendence!!.setOnClickListener(this)
        logout!!.setOnClickListener(this)

        var mSchool = MyApplication.loginResponse!!.school
        title!!.text = mSchool.name
    }

    override fun onClick(p0: View?) {

        when(p0!!.id){

            R.id.main_sms_iv -> {
                FragmentStack.replaceFragmentToContainer(R.id.main_container, requireActivity().supportFragmentManager,
                        SMSFragment(), "SMSFragment")
            }

            R.id.main_attend_iv -> {
                FragmentStack.replaceFragmentToContainer(R.id.main_container, requireActivity().supportFragmentManager,
                        AttendenceFragment(), "AttendenceFragment")
            }

            R.id.main_logout -> {
                var builder = AlertDialog.Builder(requireContext())
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes") { dialog, which ->
                            Utility.provideSharedPreferences(requireContext()).edit().
                            putBoolean("isLogin",false).
                            putString("login_response", null).
                            apply()
                            Handler(Looper.myLooper()!!).
                            postDelayed({Utility.startLoginActivity(requireContext())},2000)
                        }
                        .setNegativeButton("No") { dialog, which -> }
                builder.show()
            }

            R.id.main_back -> {
                requireActivity().onBackPressed()
            }
        }
    }
}