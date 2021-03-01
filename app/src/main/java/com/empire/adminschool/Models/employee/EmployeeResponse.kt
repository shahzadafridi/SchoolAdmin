package com.empire.adminschool.Models.employee

data class EmployeeResponse(
    val status: Int,
    val message: String,
    val classes: List<Classes>
)
