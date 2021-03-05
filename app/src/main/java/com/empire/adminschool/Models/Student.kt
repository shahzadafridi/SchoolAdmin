package com.empire.adminschool.Models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Student(
        val id: String,
        val name: String,
        val father: String,
        val guardian: String,
        val mobile: String,
        val photo: String,
        var isCheckBox: Boolean = false
): Parcelable