package com.bangkep.sistemabsensi.ui.operator.detailPegawai

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.base.BaseViewModel
import com.bangkep.sistemabsensi.model.ModelResultAbsen
import com.bangkep.sistemabsensi.model.ModelUser
import com.bangkep.sistemabsensi.utils.Constant
import com.bangkep.sistemabsensi.utils.RetrofitUtils
import com.bangkep.sistemabsensi.utils.onClickFoto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap

class DetailPegawaiViewModel(
    private val navController: NavController,
    private val activity: Activity?
) : BaseViewModel() {
    val dataUser = MutableLiveData<ModelUser>()
    val dataNipNama = MutableLiveData<String>()
    val idHari = MutableLiveData<String>()
    val jenisAbsen = MutableLiveData<String>()

    fun clickFoto(){
        onClickFoto(dataUser.value?.foto?:"", navController)
    }

    fun onClickWA(){
        try {
            isShowLoading.value = true
            val text = "Permisi, hari ini Anda belum melakukan absensi harian pegawai"

            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, text)
            sendIntent.type = "text/plain"
            isShowLoading.value = false
            activity?.startActivity(sendIntent)
        }catch (e: ActivityNotFoundException){
            message.value = e.message
        }
    }

    fun onClickAlpa(){
        val idHari = idHari.value
        val nip = dataUser.value?.username

        if (!idHari.isNullOrEmpty() && !nip.isNullOrEmpty()){
            validateData(Constant.jenisAlpa, idHari, nip)
        }
        else{
            message.value = "Error, terjadi kesalahan database"
        }
    }

    fun onClickCuti(){
        val idHari = idHari.value
        val nip = dataUser.value?.username

        if (!idHari.isNullOrEmpty() && !nip.isNullOrEmpty()){
            validateData(Constant.jenisCuti, idHari, nip)
        }
        else{
            message.value = "Error, terjadi kesalahan database"
        }
    }

    fun onClickSakit(){
        val idHari = idHari.value
        val nip = dataUser.value?.username

        if (!idHari.isNullOrEmpty() && !nip.isNullOrEmpty()){
            validateData(Constant.jenisSakit, idHari, nip)
        }
        else{
            message.value = "Error, terjadi kesalahan database"
        }
    }

    fun onClickIzin(){
        val idHari = idHari.value
        val nip = dataUser.value?.username

        if (!idHari.isNullOrEmpty() && !nip.isNullOrEmpty()){
            validateData(Constant.jenisIzin, idHari, nip)
        }
        else{
            message.value = "Error, terjadi kesalahan database"
        }
    }

    fun onClickTLD(){
        val idHari = idHari.value
        val nip = dataUser.value?.username

        if (!idHari.isNullOrEmpty() && !nip.isNullOrEmpty()){
            validateData(Constant.jenisTLD, idHari, nip)
        }
        else{
            message.value = "Error, terjadi kesalahan database"
        }
    }

    fun onClickTDD(){
        val idHari = idHari.value
        val nip = dataUser.value?.username

        if (!idHari.isNullOrEmpty() && !nip.isNullOrEmpty()){
            validateData(Constant.jenisTDD, idHari, nip)
        }
        else{
            message.value = "Error, terjadi kesalahan database"
        }
    }

    private fun validateData(jenis: String, idHari: String, username: String){
        @SuppressLint("SimpleDateFormat")
        val dateCreated = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constant.dateFormat2))
        } else {
            SimpleDateFormat(Constant.dateFormat2).format(Date())
        }

        val body = HashMap<String, String>()
        body[Constant.reffNip] = username
        body[Constant.reffIdHari] = idHari
        body["latitude"] = "0"
        body["longitude"] = "0"
        body["status"] = "1"
        body["jenis"] = jenis
        body["date_created"] = dateCreated
        body["img"] = Constant.defaultTempFoto

        sendData(body)
    }

    private fun sendData(body: HashMap<String, String>){
        isShowLoading.value = true

        RetrofitUtils.createAbsensi(body,
            object : Callback<ModelResultAbsen> {
                override fun onResponse(
                    call: Call<ModelResultAbsen>,
                    response: Response<ModelResultAbsen>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.response == Constant.reffSuccess){
                        navController.navigate(R.id.navBeranda)
                        message.value = "Berhasil absensi"
                    }
                    else{
                        message.value = result?.response
                    }
                }

                override fun onFailure(
                    call: Call<ModelResultAbsen>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    message.value = t.message
                }
            })
    }
}