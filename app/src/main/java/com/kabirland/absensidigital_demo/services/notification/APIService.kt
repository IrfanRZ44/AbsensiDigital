package com.kabirland.absensidigital_demo.services.notification

import com.kabirland.absensidigital_demo.services.notification.model.MyResponse
import com.kabirland.absensidigital_demo.services.notification.model.Sender
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIService {
//    @Headers("Content-Type:application/json", "Authorization:key=AAAA-sRpp9g:APA91bEfx3P8cnXPMsS-vt_X9meWzcXferoEwj2nMcOWYQaB7xy04w1xEjl3XlTjZGQjaABtEVDgYABHtG9Bv9zycj4KQ3jnSpbBDgKGJs6bRAfj7vK41N8SIfZ1h0QNFSMhHLS2ZQGu")
    @Headers("Content-Type:application/json", "Authorization:key=AAAAZDF42jM:APA91bHAlpdQ58oJje8N9597Hz5Ud-xzdqU2c-uuZh-w7FoJQJIroCMoSJBj8q5YgxGu3mUen2XQXXg4JKI2wC1BT6weQC15TSECi-c0MIeETY5uID62uDcdcYr0pU7wgqju9VmvElip")
    @POST("fcm/send")
    fun sendNotification(@Body body: Sender?): Call<MyResponse?>?
}