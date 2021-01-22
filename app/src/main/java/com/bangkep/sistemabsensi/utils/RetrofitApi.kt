package com.bangkep.sistemabsensi.utils

import com.bangkep.sistemabsensi.model.ModelResult
import com.bangkep.sistemabsensi.model.ModelUser
import retrofit2.Call
import retrofit2.http.*


/**
 * Created by IrfanRZ on 02/08/2019.
 */
interface RetrofitApi {

    @Headers("Accept:application/json")
    @POST(Constant.reffLoginUser)
    fun loginUser(
        @Body input: Map<String, String>,
        @Header("Content-Type") contentType: String
    ): Call<ModelUser>

    @Headers("Accept:application/json")
    @POST(Constant.reffDeleteToken)
    fun deleteToken(
        @Body input: Map<String, String>,
        @Header("Content-Type") contentType: String
    ): Call<ModelResult>

    companion object {
        const val baseUrl = Constant.reffBaseUrl
    }
}