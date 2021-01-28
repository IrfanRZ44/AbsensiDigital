package com.bangkep.sistemabsensi.utils

import com.bangkep.sistemabsensi.model.*
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitUtils{
    private val retrofit = Retrofit.Builder()
        .baseUrl(RetrofitApi.baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val api = retrofit.create(RetrofitApi::class.java)

    fun loginUser(input: Map<String, String>, callback: Callback<ModelUser>){
        val call = api.loginUser(input, Constant.contentType)
        call.enqueue(callback)
    }

    fun getDataPegawai(input: Map<String, String>, callback: Callback<ModelUser>){
        val call = api.getDataPegawai(input, Constant.contentType)
        call.enqueue(callback)
    }

    fun deleteToken(input: Map<String, String>, callback: Callback<ModelResult>){
        val call = api.deleteToken(input, Constant.contentType)
        call.enqueue(callback)
    }

    fun getHariKerja(input: Map<String, String>, callback: Callback<ModelHariKerja>){
        val call = api.getHariKerja(input, Constant.contentType)
        call.enqueue(callback)
    }

    fun getAbsensiByJenis(input: Map<String, String>, callback: Callback<ModelAbsensi>){
        val call = api.getAbsensiByJenis(input, Constant.contentType)
        call.enqueue(callback)
    }

    fun getAbsensi(input: Map<String, String>, callback: Callback<ModelListAbsensi>){
        val call = api.getAbsensi(input, Constant.contentType)
        call.enqueue(callback)
    }

    fun getAbsensiByDate(input: Map<String, String>, callback: Callback<ModelListAbsensi>){
        val call = api.getAbsensiByDate(input, Constant.contentType)
        call.enqueue(callback)
    }

    fun getRiwayat(input: Map<String, String>, callback: Callback<ModelListAbsensi>){
        val call = api.getRiwayat(input, Constant.contentType)
        call.enqueue(callback)
    }

    fun createAbsensi(input: Map<String, String>, callback: Callback<ModelResultAbsen>){
        val call = api.createAbsensi(input, Constant.contentType)
        call.enqueue(callback)
    }

    fun updateAbsensi(input: Map<String, String>, callback: Callback<ModelResultAbsen>){
        val call = api.updateAbsensi(input, Constant.contentType)
        call.enqueue(callback)
    }

    fun updateAbsensiStatus(input: Map<String, String>, callback: Callback<ModelResult>){
        val call = api.updateAbsensiStatus(input, Constant.contentType)
        call.enqueue(callback)
    }

    fun updateProfil(input: Map<String, String>, callback: Callback<ModelResult>){
        val call = api.updateProfil(input, Constant.contentType)
        call.enqueue(callback)
    }
}