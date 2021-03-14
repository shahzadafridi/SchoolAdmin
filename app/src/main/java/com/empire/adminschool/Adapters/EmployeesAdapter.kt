package com.empire.adminschool.Adapters

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.empire.adminschool.Models.Classes
import com.empire.adminschool.Models.Employee
import com.empire.adminschool.Models.Student
import com.empire.adminschool.R
import com.squareup.picasso.Picasso

class EmployeesAdapter(context: Context) : BaseAdapter() {

    var mCtx: Context
    var list: MutableList<Employee> = arrayListOf()

    init {
        mCtx = context
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return list.size
    }

    fun setEmployeeList(list: MutableList<Employee>){
        this.list.clear()
        this.list = list
        notifyDataSetChanged()
    }

    fun getSelectedEmployee(): List<Employee>{
        var selectedEmployeeList = arrayListOf<Employee>()
        for (s in list){
            if (s.isCheckBox)
                selectedEmployeeList.add(s)
        }
        return selectedEmployeeList
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var holder: MyViewHolder
        var retView: View
        var item = list.get(position)

        if(convertView == null){
            retView = LayoutInflater.from(mCtx).inflate(R.layout.row_students_layout, null)
            holder = MyViewHolder()
            holder.name = retView.findViewById(R.id.students_name) as TextView
            holder.mobile = retView.findViewById(R.id.students_mobile) as TextView
            holder.image = retView.findViewById(R.id.students_image) as ImageView
            holder.checkBox = retView.findViewById(R.id.students_chkbox) as CheckBox
            retView.tag = holder
        } else {
            holder = convertView.tag as MyViewHolder
            retView = convertView
        }

        holder.checkBox!!.isChecked = item.isCheckBox

        if (position == 0){
            retView.setBackgroundColor(ContextCompat.getColor(mCtx,R.color.app_color_green))
            holder.checkBox!!.setOnCheckedChangeListener { compoundButton, b ->
                if (compoundButton.isPressed){
                    if (b){
                        setCheckBoxesActive(true)
                    }else{
                        setCheckBoxesActive(false)
                    }
                }
            }
            holder.name!!.text = "Name"
            holder.mobile!!.text = "Mobile"
        }else{
            retView.setBackgroundColor(ContextCompat.getColor(mCtx,R.color.white))
            holder.name!!.text = item.name
            holder.mobile!!.text = item.mobile
            if (!TextUtils.isEmpty(item.photo))
                Picasso.get().load(item.photo).into(holder.image)
            holder.checkBox!!.setOnCheckedChangeListener { compoundButton, b ->
                if (compoundButton.isPressed){
                    list.get(position).isCheckBox = b
                }
            }
        }

        return retView
    }

    fun setCheckBoxesActive(isActive: Boolean){
        if (isActive){
            for (s in list){
                s.isCheckBox = true
            }
        }else{
            for (s in list){
                s.isCheckBox = false
            }
        }
        notifyDataSetChanged()
    }


    inner class MyViewHolder{
        var name: TextView? = null
        var mobile: TextView? = null
        var image: ImageView? = null
        var checkBox: CheckBox? = null
    }
}