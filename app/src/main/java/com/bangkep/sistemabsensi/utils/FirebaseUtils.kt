@file:Suppress("DEPRECATION")

package com.bangkep.sistemabsensi.utils

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult

@Suppress("DEPRECATION")
object FirebaseUtils {
    fun getUserToken(onCompleteListener: OnCompleteListener<InstanceIdResult>) {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(onCompleteListener)
    }
}