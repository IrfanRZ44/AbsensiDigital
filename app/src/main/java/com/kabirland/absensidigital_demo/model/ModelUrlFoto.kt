package com.kabirland.absensidigital_demo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelUrlFoto(
    var foto: String = "",
    var response: String = ""
    ) : Parcelable