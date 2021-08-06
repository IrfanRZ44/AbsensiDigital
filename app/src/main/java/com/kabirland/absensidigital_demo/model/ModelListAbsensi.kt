package com.kabirland.absensidigital_demo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelListAbsensi(
    var list_absen: ArrayList<ModelAbsensi>,
    var response: String = ""
    ) : Parcelable