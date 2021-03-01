package com.empire.adminschool.Fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.empire.adminschool.R
import com.empire.adminschool.Util.FragmentStack
import com.empire.adminschool.ViewModels.MainViewModel

class MainFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    var ivBack: ImageView? = null
    var ivSMS: ImageView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var root = inflater.inflate(R.layout.main_fragment, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.injectRepository(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivBack = view.findViewById(R.id.main_back)
        ivSMS = view.findViewById(R.id.main_sms_iv)
        ivBack!!.setOnClickListener(this)
        ivSMS!!.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {

        when(p0!!.id){

            R.id.main_sms_iv -> {
                FragmentStack.replaceFragmentToContainer(R.id.main_container,requireActivity().supportFragmentManager,
                    SMSFragment(),"SMSFragment")
            }

            R.id.main_attend_iv -> {

            }

            R.id.main_back -> {
                requireActivity().onBackPressed()
            }
        }
    }
}