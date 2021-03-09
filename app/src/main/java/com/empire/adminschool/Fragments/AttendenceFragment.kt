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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.empire.adminschool.R
import com.empire.adminschool.Util.AppPermissions
import com.empire.adminschool.Util.FragmentStack
import com.empire.adminschool.Util.Utility
import com.empire.adminschool.ViewModels.MainViewModel

class AttendenceFragment : Fragment() {

    val TAG = "AttendenceFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_attendence, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.atten_frag_back).setOnClickListener {
            requireActivity().onBackPressed()
        }

        view.findViewById<ImageView>(R.id.atten_frag_in_iv).setOnClickListener {
            if (AppPermissions.checkCameraPermission(requireActivity())){
                var fragment = AttendenceInOutFragment()
                var bundle = Bundle()
                bundle.putString("type","in")
                fragment.arguments = bundle
                FragmentStack.replaceFragmentToContainer(R.id.main_container,requireActivity().supportFragmentManager,
                        fragment,"AttendenceInOutFragment")
            }else{
                Toast.makeText(requireContext(),"Permission required to access camera for scanning QR Code",Toast.LENGTH_LONG).show()
            }
        }

        view.findViewById<ImageView>(R.id.atten_frag_out_iv).setOnClickListener {
            if (AppPermissions.checkCameraPermission(requireActivity())) {
                var fragment = AttendenceInOutFragment()
                var bundle = Bundle()
                bundle.putString("type", "out")
                fragment.arguments = bundle
                FragmentStack.replaceFragmentToContainer(R.id.main_container, requireActivity().supportFragmentManager,
                        fragment, "AttendenceInOutFragment")
            }else{
                Toast.makeText(requireContext(),"Permission required to access camera for scanning QR Code",Toast.LENGTH_LONG).show()
            }
        }

    }

}