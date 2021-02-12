package com.bangkep.sistemabsensi.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelResultInfoApps(
    var message: String = "",
    var version_apps: String = "",
    var link: String = "",
    var response: String = ""
    ) : Parcelable