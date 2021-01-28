package com.bangkep.sistemabsensi.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelListBelumAbsensi(
    var list_absen: ArrayList<ModelUser>,
    var response: String = ""
    ) : Parcelable