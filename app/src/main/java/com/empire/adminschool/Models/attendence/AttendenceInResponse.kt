package com.empire.adminschool.Models.attendence

data class AttendenceInResponse(
        val message: String,
        val narration: String,
        val sms: Boolean,
        val sms_text: String,
        val formate: String,
        val mobile: String,
        val type: String,
        val status: Int
)