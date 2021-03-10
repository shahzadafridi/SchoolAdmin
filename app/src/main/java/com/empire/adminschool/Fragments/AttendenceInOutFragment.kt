package com.empire.adminschool.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.empire.adminschool.Models.Student
import com.empire.adminschool.MyApplication
import com.empire.adminschool.R
import com.empire.adminschool.Util.Utility
import com.empire.adminschool.ViewModels.AttendenceViewModel
import com.empire.adminschool.ViewModels.MainViewModel

class AttendenceInOutFragment : Fragment() {

    val TAG = "AttendenceFragment"
    private lateinit var viewModel: AttendenceViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var codeScanner: CodeScanner
    private var type = "in"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var root = inflater.inflate(R.layout.fragment_attendence_inout, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AttendenceViewModel::class.java)
        viewModel.injectRepository(requireActivity())
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mainViewModel.injectRepository(requireActivity())
        var defaultSim = Utility.provideSharedPreferences(requireContext()).getInt("sim_type",1)
        var sim = mainViewModel.getSIMProvider(defaultSim,requireActivity())
        viewModel.attendenceInLiveData.observe(viewLifecycleOwner,{
            if (it != null){
                Toast.makeText(context,it.narration,Toast.LENGTH_LONG).show()
                if (it.sms){
                    mainViewModel.sendDirectSMS(requireActivity(),sim,it.sms_text, Student("","","","",it.mobile,"",false),null)
                }
            }else{
                Toast.makeText(context,"Attendence failed",Toast.LENGTH_LONG).show()
            }
        })
        viewModel.attendenceOutLiveData.observe(viewLifecycleOwner,{
            if (it != null){
                Toast.makeText(context,it.narration,Toast.LENGTH_LONG).show()
                if (it.sms){
                    mainViewModel.sendDirectSMS(requireActivity(),sim,it.sms_text, Student("","","","",it.mobile,"",false),null)
                }
            }else{
                Toast.makeText(context,"Attendence failed",Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        type = requireArguments().getString("type","in")
        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)

        codeScanner = CodeScanner(requireContext(), scannerView)

        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            requireActivity().runOnUiThread {
                if (type.contentEquals("in"))
                    viewModel.attendenceIn(it.text,MyApplication.loginResponse!!.school.id)
                else
                    viewModel.attendenceOut(it.text,MyApplication.loginResponse!!.school.id)
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            requireActivity().runOnUiThread {
                Toast.makeText(requireContext(), "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}