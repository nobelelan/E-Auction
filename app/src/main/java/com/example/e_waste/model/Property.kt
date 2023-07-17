package com.example.e_waste.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Property(
    val name: String? = "",
    val type: String? = "",
    val url: String? = "",
    val price: String? = "",
    val currentBid: String? = "",
    val topBidder: String? = "",
    val totalBids: String? = "0",
    val description: String? = ""
): Parcelable
