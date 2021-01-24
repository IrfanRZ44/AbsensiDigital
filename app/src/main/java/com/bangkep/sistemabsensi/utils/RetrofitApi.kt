package com.bangkep.sistemabsensi.utils

import com.bangkep.sistemabsensi.model.*
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

    @Headers("Accept:application/json")
    @POST(Constant.reffHariKerja)
    fun getHariKerja(
        @Body input: Map<String, String>,
        @Header("Content-Type") contentType: String
    ): Call<ModelHariKerja>

    @Headers("Accept:application/json")
    @POST(Constant.reffAbsensiByJenis)
    fun getAbsensiByJenis(
        @Body input: Map<String, String>,
        @Header("Content-Type") contentType: String
    ): Call<ModelAbsensi>

    @Headers("Accept:application/json")
    @POST(Constant.reffAbsensi)
    fun getAbsensi(
        @Body input: Map<String, String>,
        @Header("Content-Type") contentType: String
    ): Call<ModelListAbsensi>

    @Headers("Accept:application/json")
    @POST(Constant.reffCreateAbsensi)
    fun createAbsensi(
        @Body input: Map<String, String>,
        @Header("Content-Type") contentType: String
    ): Call<ModelResultAbsen>

    companion object {
        const val baseUrl = Constant.reffBaseUrl
    }
}