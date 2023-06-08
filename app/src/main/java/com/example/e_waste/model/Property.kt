package com.example.e_waste.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Property(
    val name: String? = "",
    val type: String? = "",
    val url: String? = "",
    val price: String? = "",
    val description: String? = ""
): Parcelable
