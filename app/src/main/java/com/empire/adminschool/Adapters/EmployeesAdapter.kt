package com.empire.adminschool.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.empire.adminschool.Models.Classes
import com.empire.adminschool.Models.Employee
import com.empire.adminschool.Models.Student
import com.empire.adminschool.R

class EmployeesAdapter(context: Context) : BaseAdapter() {

    var mCtx: Context
    var list: List<Employee> = arrayListOf()

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

    fun setEmployeeList(list: List<Employee>){
        this.list = list
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var holder: MyViewHolder
        var retView: View

        if(convertView == null){
            retView = LayoutInflater.from(mCtx).inflate(R.layout.row_classes_layout, null)
            holder = MyViewHolder()
            holder.title = retView.findViewById(R.id.classes_title) as TextView?
            retView.tag = holder
        } else {
            holder = convertView.tag as MyViewHolder
            retView = convertView
        }

        holder.title!!.text = list.get(position).name
        return retView
    }


    inner class MyViewHolder{
        var title: TextView? = null
    }
}