package com.bangkep.sistemabsensi.utils

import com.bangkep.sistemabsensi.model.ModelResult
import com.bangkep.sistemabsensi.model.ModelUser
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitUtils{
    private val retrofit = Retrofit.Builder()
        .baseUrl(RetrofitApi.baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val api = retrofit.create(RetrofitApi::class.java)

    fun createUser(input: Map<String, String>, callback: Callback<ModelUser>){
        val call = api.loginUser(input, Constant.contentType)
        call.enqueue(callback)
    }

    fun deleteToken(input: Map<String, String>, callback: Callback<ModelResult>){
        val call = api.deleteToken(input, Constant.contentType)
        call.enqueue(callback)
    }


}