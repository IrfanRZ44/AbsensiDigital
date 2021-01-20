package com.bangkep.sistemabsensi.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelUser(
    var idUser: String = "",
    var username: String = "",
    var nama: String = "",
    var email: String = "",
    var level: String = "",
    var foto: String = "",
    var phone: String = "",
    var idDinas: String = "",
    var token: String = "",
    var response: String = ""
    ) : Parcelable