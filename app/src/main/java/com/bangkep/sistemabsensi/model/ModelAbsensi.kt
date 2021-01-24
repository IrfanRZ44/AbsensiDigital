package com.bangkep.sistemabsensi.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelAbsensi(
    var id_absensi: String = "",
    var nip: String = "",
    var id_hari: String = "",
    var img: String = "",
    var latitude: String = "",
    var longitude: String = "",
    var jenis: String = "",
    var status: String = "",
    var date_created: String = "",
    var response: String = ""
    ) : Parcelable