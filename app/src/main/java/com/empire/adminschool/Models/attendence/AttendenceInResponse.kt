package com.empire.adminschool.Models.attendence

data class AttendenceInResponse(
        val format: String,
        val message: String,
        val mobile: String,
        val narration: String,
        val sms: Boolean,
        val sms_text: String,
        val status: Int,
        val student_name: String,
        val type: String
)