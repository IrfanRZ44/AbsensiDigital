package com.kabirland.absensidigital_demo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelResult(
    var response: String = ""
    ) : Parcelable