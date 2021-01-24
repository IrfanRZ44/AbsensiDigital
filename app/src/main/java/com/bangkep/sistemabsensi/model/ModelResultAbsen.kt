package com.bangkep.sistemabsensi.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelResultAbsen(
    var id_absensi: String = "",
    var response: String = ""
    ) : Parcelable