package com.empire.adminschool.Models

data class StudentResponse(
        val status: Int,
        val message: String,
        val studetns: List<Student>
)