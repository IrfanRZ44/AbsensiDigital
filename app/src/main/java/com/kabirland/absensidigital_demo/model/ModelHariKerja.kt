package com.kabirland.absensidigital_demo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelHariKerja(
    var id_hari: String = "",
    var hari: String = "",
    var masuk_kerja: String = "",
    var pulang_kerja: String = "",
    var masuk_apel: String = "",
    var pulang_apel: String = "",
    var keterangan: String = "",
    var response: String = ""
    ) : Parcelable