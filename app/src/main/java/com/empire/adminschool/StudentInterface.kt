package com.empire.adminschool

import com.empire.adminschool.Models.Classes
import com.empire.adminschool.Models.Student

interface StudentInterface {
    fun onGetClasses(classes: List<Classes>)
    fun onGetStudents(students: List<Student>)
    fun onError(error: String)
}