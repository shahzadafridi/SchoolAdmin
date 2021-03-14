package com.empire.adminschool.Models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SentSMS(
        val count: Int,
        val status: String,
        val name: String
): Parcelable