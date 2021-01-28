package com.bangkep.sistemabsensi.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelListSudahAbsensi(
    var list_absen: ArrayList<ModelSudahAbsensi>,
    var response: String = ""
    ) : Parcelable