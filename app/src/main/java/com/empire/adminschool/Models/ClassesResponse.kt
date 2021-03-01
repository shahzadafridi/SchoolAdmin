package com.empire.adminschool.Models

data class ClassesResponse(
        val status: Int,
        val message: String,
        val classes: List<Classes>
)
