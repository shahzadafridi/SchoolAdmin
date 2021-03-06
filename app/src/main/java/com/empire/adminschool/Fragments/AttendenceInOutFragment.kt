package com.empire.adminschool.Fragments

import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.Dialog
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
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
import com.google.gson.Gson
import kotlinx.coroutines.*


class AttendenceInOutFragment : Fragment() {

    val TAG = "AttendenceFragment"
    private lateinit var viewModel: AttendenceViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var codeScanner: CodeScanner
    var attenChangeCamera: ImageView? = null
    var attenDialog: Dialog? = null
    var title: TextView? = null
    var studentName: TextView? = null
    var progressBar: ProgressBar? = null
    var greenTick: ImageView? = null
    var objectAnimator1: ObjectAnimator? = null
    private var type = "in"
    var IS_SCAN_ALLOW = true

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
        attenDialog = Utility.onCreateDialog(requireContext(), R.layout.attendence_dialog, false)
        title = attenDialog!!.findViewById(R.id.attend_dialog_title)
        studentName = attenDialog!!.findViewById(R.id.atten_dialog_name)
        progressBar = attenDialog!!.findViewById(R.id.atten_dialog_prog)
        greenTick = attenDialog!!.findViewById(R.id.atten_dialog_green_tick)
        objectAnimator1 = ObjectAnimator.ofInt(progressBar, "progress", 100)
        objectAnimator1!!.setDuration(2000)
        objectAnimator1!!.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                greenTick!!.visibility = View.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed({
                    attenDialog!!.dismiss()
                }, 3000)
            }

            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}

        })

        var defaultSim = Utility.provideSharedPreferences(requireContext()).getInt("sim_type", 1)
        var sim = mainViewModel.getSIMProvider(defaultSim, requireActivity())
        viewModel.attendenceInLiveData.observe(viewLifecycleOwner, {
            IS_SCAN_ALLOW = true
            if (it != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    val mediaPlayer: MediaPlayer = MediaPlayer.create(context, R.raw.attendence_sound)
                    mediaPlayer.start()
                }
                attenDialog!!.show()
                studentName!!.text = it.narration
                objectAnimator1!!.start()
                Log.e(TAG, Gson().toJson(it))
                if (it.sms) {
                    mainViewModel.sendDirectSMS(requireActivity(), sim, it.sms_text, Student("", "", "", "", it.mobile, "", false), null)
                }
            } else {
                Toast.makeText(context, "Attendence failed", Toast.LENGTH_LONG).show()
            }
        })
        viewModel.attendenceOutLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    val mediaPlayer: MediaPlayer = MediaPlayer.create(context, R.raw.attendence_sound)
                    mediaPlayer.start()
                }
                attenDialog!!.show()
                studentName!!.text = it.narration
                objectAnimator1!!.start()
                Log.e(TAG, Gson().toJson(it))
                if (it.sms) {
                    mainViewModel.sendDirectSMS(requireActivity(), sim, it.sms_text, Student("", "", "", "", it.mobile, "", false), null)
                }
            } else {
                Toast.makeText(context, "Attendence failed", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        type = requireArguments().getString("type", "in")
        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
        attenChangeCamera = view.findViewById(R.id.atten_change_camera)
        codeScanner = CodeScanner(requireContext(), scannerView)

        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.CONTINUOUS // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runBlocking {
                requireActivity().runOnUiThread {
                    if (type.contentEquals("in"))
                        viewModel.attendenceIn(it.text, MyApplication.loginResponse!!.school.id)
                    else
                        viewModel.attendenceOut(it.text, MyApplication.loginResponse!!.school.id)
                }
                delay(6000L)
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            requireActivity().runOnUiThread {
                Toast.makeText(requireContext(), "Camera initialization error: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

        attenChangeCamera!!.setOnClickListener {
            if (codeScanner.camera == 1){
                codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
            }else{
                codeScanner.camera = CodeScanner.CAMERA_FRONT // or CAMERA_FRONT or specific camera id
            }
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