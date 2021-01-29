@file:Suppress("DEPRECATION")

package com.bangkep.sistemabsensi.utils

import com.bangkep.sistemabsensi.services.notification.APIService
import com.bangkep.sistemabsensi.services.notification.Common
import com.bangkep.sistemabsensi.services.notification.model.MyResponse
import com.bangkep.sistemabsensi.services.notification.model.Sender
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("DEPRECATION")
object FirebaseUtils {
    fun getUserToken(onCompleteListener: OnCompleteListener<InstanceIdResult>) {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(onCompleteListener)
    }

    fun sendNotif(sender: Sender) {
        val mService: APIService = Common.fCMClient
        mService.sendNotification(sender)
            ?.enqueue(object : Callback<MyResponse?> {
                override fun onResponse(
                    call: Call<MyResponse?>?,
                    response: Response<MyResponse?>?
                ) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            showLog("Notify Succes")
                        } else {
                            showLog("Notify Failed")
                        }
                    } else {
                        showLog("Null Response")
                    }
                }

                override fun onFailure(call: Call<MyResponse?>?, t: Throwable) {
                    showLog(t.message)
                }
            })
    }
}