package com.empire.adminschool.Interfaces

import com.empire.adminschool.Models.Employee

interface EmployeeInterface {
    fun onGetEmployees(classes: List<Employee>)
    fun onGetEmployeeTypes(types: List<String>)
    fun onError(error: String)
}