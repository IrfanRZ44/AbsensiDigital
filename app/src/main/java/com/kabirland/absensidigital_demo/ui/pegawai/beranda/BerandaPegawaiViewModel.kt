package com.kabirland.absensidigital_demo.ui.pegawai.beranda

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.kabirland.absensidigital_demo.R
import com.kabirland.absensidigital_demo.base.BaseViewModel
import com.kabirland.absensidigital_demo.model.ModelAbsensi
import com.kabirland.absensidigital_demo.model.ModelHariKerja
import com.kabirland.absensidigital_demo.model.ModelListAbsensi
import com.kabirland.absensidigital_demo.ui.pegawai.absensi.AbsensiFragment
import com.kabirland.absensidigital_demo.utils.Constant
import com.kabirland.absensidigital_demo.utils.DataSave
import com.kabirland.absensidigital_demo.utils.RetrofitUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap

@SuppressLint("StaticFieldLeak")
class BerandaPegawaiViewModel(
    private val navController: NavController,
    private val btnApel: AppCompatTextView,
    private val savedData: DataSave,
    private val activity: Activity?,
    private val checkPermissionStorage: () -> Unit
) : BaseViewModel() {
    val isDatang = MutableLiveData<Boolean>()
    val isPulang = MutableLiveData<Boolean>()
    val isApel = MutableLiveData<Boolean>()
    val keterangan = MutableLiveData<String>()
    val tglKeterangan = MutableLiveData<String>()
    var idHari : String? = ""
    var idAbsensi : String? = ""
    var urlFoto : String? = ""
    var fotoDatang : String = Constant.fotoDatang
    var fotoPulang : String = Constant.fotoPulang
    var fotoApel : String = Constant.fotoApel
    private var request = 0

    fun getHariKerja(){
        isShowLoading.value = true

        @SuppressLint("SimpleDateFormat")
        val tglSkrng = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constant.dateFormat1))
        } else {
            SimpleDateFormat(Constant.dateFormat1).format(Date())
        }

        val body = HashMap<String, String>()
        body["tglSekarang"] = tglSkrng

        RetrofitUtils.getHariKerja(body,
            object : Callback<ModelHariKerja> {
                override fun onResponse(
                    call: Call<ModelHariKerja>,
                    response: Response<ModelHariKerja>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.response == Constant.reffSuccess){
                        val nip = savedData.getDataUser()?.username
                        val id = result.id_hari
                        idHari = result.id_hari
                        keterangan.value = result.keterangan

                        if (!nip.isNullOrEmpty() && id.isNotEmpty()){
                            val dataSend = HashMap<String, String>()
                            dataSend[Constant.reffNip] = nip
                            dataSend[Constant.reffIdHari] = id

                            if (result.masuk_apel != "00:00:00"){
                                getAbsensiApel(dataSend, result.masuk_apel, result.pulang_apel)
                            }
                            getListAbsensi(dataSend, result)
                        }
                        else{
                            message.value = "Terjadi kesalahan penyimpanan, mohon login ulang"
                            isDatang.value = false
                            isPulang.value = false
                            btnApel.visibility = View.GONE
                        }
                    }
                    else{
                        keterangan.value = "Sekarang bukan hari kerja"
                        if (result?.response == Constant.tanggalTidakTersedia){
                            message.value = "Sekarang bukan hari kerja"
                        }
                        else{
                            message.value = result?.response
                        }
                        isDatang.value = false
                        isPulang.value = false
                        btnApel.visibility = View.GONE
                    }
                }

                override fun onFailure(
                    call: Call<ModelHariKerja>,
                    t: Throwable
                ) {
                    keterangan.value = "Sekarang bukan hari kerja"
                    isShowLoading.value = false
                    message.value = t.message
                    isDatang.value = false
                    isPulang.value = false
                    btnApel.visibility = View.GONE
                }
            })
    }

    fun getListAbsensi(body : HashMap<String, String>, dataHari: ModelHariKerja){
        isShowLoading.value = true
        @SuppressLint("SimpleDateFormat")
        val timeNow = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constant.timeFormat))
        } else {
            SimpleDateFormat(Constant.timeFormat).format(Date())
        }

        RetrofitUtils.getAbsensi(body, object : Callback<ModelListAbsensi> {
                override fun onResponse(
                    call: Call<ModelListAbsensi>,
                    response: Response<ModelListAbsensi>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.response == Constant.reffSuccess){
                        val listAbsen = result.list_absen
                        var izin = false
                        var masuk = false
                        var pulang = false

                        for(i in listAbsen.indices){
                            val dataAbsen = listAbsen[i]
                            if (dataAbsen.jenis == Constant.jenisAlpa){
                                if (!izin){
                                    izin = true
                                }
                            }
                            else if (dataAbsen.jenis == Constant.jenisCuti){
                                if (!izin){
                                    izin = true
                                }
                            }
                            else if (dataAbsen.jenis == Constant.jenisIzin){
                                if (!izin){
                                    izin = true
                                }
                            }
                            else if (dataAbsen.jenis == Constant.jenisSakit){
                                if (!izin){
                                    izin = true
                                }
                            }
                            else if (dataAbsen.jenis == Constant.jenisTLD){
                                if (!izin){
                                    izin = true
                                }
                            }
                            else if (dataAbsen.jenis == Constant.jenisTDD){
                                if (!izin){
                                    izin = true
                                }
                            }
                            else if (dataAbsen.jenis == Constant.jenisMasuk){
                                if (!izin){
                                    if (!masuk){
                                        if (dataAbsen.status == "2"){
                                            idAbsensi = dataAbsen.id_absensi
                                            urlFoto = dataAbsen.img
                                            masuk = false
                                        }
                                        else{
                                            masuk = true
                                        }
                                    }
                                }
                            }
                            else if (dataAbsen.jenis == Constant.jenisPulang){
                                if (!izin){
                                    if (!pulang){
                                        if (dataAbsen.status == "2"){
                                            idAbsensi = dataAbsen.id_absensi
                                            urlFoto = dataAbsen.img
                                            pulang = false
                                        }
                                        else{
                                            pulang = true
                                        }
                                    }
                                }
                            }
                        }

                        if (izin){
                            isDatang.value = false
                            isPulang.value = false
                        }
                        else if (!izin && !masuk && !pulang){
                            if (comparingTimesAfter(dataHari.masuk_kerja, timeNow)){
                                isDatang.value = true
                                isPulang.value = false
                            }
                            else{
                                isDatang.value = false
                                isPulang.value = false
                            }
                        }
                        else if (!izin && masuk && !pulang){
                            isDatang.value = false
                            isPulang.value = true
                        }
                        else if (!izin && masuk && pulang){
                            isDatang.value = false
                            isPulang.value = false
                        }
                        else{
                            isDatang.value = true
                            isPulang.value = false
                        }
                    }
                    else{
                        isDatang.value = true
                        isPulang.value = false
                    }
                }

                override fun onFailure(
                    call: Call<ModelListAbsensi>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    isDatang.value = true
                    isPulang.value = false
                }
            })
    }

    fun getAbsensiApel(body : HashMap<String, String>, timeStart: String, timeEnd: String){
        isShowLoading.value = true
        body["jenis"] = Constant.jenisApel
        @SuppressLint("SimpleDateFormat")
        val timeNow = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constant.timeFormat))
        } else {
            SimpleDateFormat(Constant.timeFormat).format(Date())
        }

        RetrofitUtils.getAbsensiByJenis(body,
            object : Callback<ModelAbsensi> {
                override fun onResponse(
                    call: Call<ModelAbsensi>,
                    response: Response<ModelAbsensi>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.response == Constant.reffSuccess){
                        if (result.status == "2"){
                            idAbsensi = result.id_absensi
                            urlFoto = result.img

                            isApel.value = true
                        }
                        else{
                            isApel.value = false
                        }
                    }
                    else{
                        isApel.value = comparingTimes(timeStart, timeEnd, timeNow)
                    }
                }

                override fun onFailure(
                    call: Call<ModelAbsensi>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    isApel.value = comparingTimes(timeStart, timeEnd, timeNow)
                }
            })
    }

    @SuppressLint("SimpleDateFormat")
    private fun comparingTimesAfter(waktuMulai: String, waktuDipilih: String) : Boolean{
        try {
            val time1 = SimpleDateFormat(Constant.timeFormat).parse(waktuMulai)
            val d = SimpleDateFormat(Constant.timeFormat).parse(waktuDipilih)

            val calendar1 = Calendar.getInstance()
            val calendar3 = Calendar.getInstance()

            return if (time1 != null && d != null){
                calendar1.time = time1
                calendar1.add(Calendar.DATE, 1)
                calendar3.time = d
                calendar3.add(Calendar.DATE, 1)

                val x = calendar3.time
                x.after(calendar1.time)
            } else{
                false
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            return false
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun comparingTimes(waktuMulai: String, waktuSelesai: String, waktuDipilih: String) : Boolean{
        try {
            val time1 = SimpleDateFormat(Constant.timeFormat).parse(waktuMulai)
            val time2 = SimpleDateFormat(Constant.timeFormat).parse(waktuSelesai)
            val d = SimpleDateFormat(Constant.timeFormat).parse(waktuDipilih)

            val calendar1 = Calendar.getInstance()
            val calendar2 = Calendar.getInstance()
            val calendar3 = Calendar.getInstance()

            return if (time1 != null && time2 != null && d != null){
                calendar1.time = time1
                calendar1.add(Calendar.DATE, 1)
                calendar2.time = time2
                calendar2.add(Calendar.DATE, 1)
                calendar3.time = d
                calendar3.add(Calendar.DATE, 1)

                val x = calendar3.time
                x.after(calendar1.time) && x.before(calendar2.time)
            } else{
                false
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            return false
        }
    }

    fun onClickDatang(){
        if (!idHari.isNullOrEmpty()){
            if (checkGPS()){
                request = 1
                checkPermissionStorage()
            }
        }
        else{
            message.value = "Error, hari ini tidak tersedia untuk melakukan absensi"
        }
    }

    fun onClickPulang(){
        if (!idHari.isNullOrEmpty()){
            if (checkGPS()){
                request = 2
                checkPermissionStorage()
            }
        }
        else{
            message.value = "Error, hari ini tidak tersedia untuk melakukan absensi"
        }
    }

    fun onClickApel(){
        if (!idHari.isNullOrEmpty()){
            if (checkGPS()){
                request = 3
                checkPermissionStorage()
            }
        }
        else{
            message.value = "Error, hari ini tidak tersedia untuk melakukan absensi apel"
        }
    }

    fun navigateRequest(){
        val bundle = Bundle()
        val fragmentTujuan = AbsensiFragment()
        bundle.putString(Constant.reffIdHari, idHari)
        bundle.putString(Constant.reffIdAbsen, idAbsensi)
        bundle.putString(Constant.reffFoto, urlFoto)
        when (request) {
            1 -> {
                bundle.putString(Constant.reffJenis, Constant.jenisMasuk)

            }
            2 -> {
                bundle.putString(Constant.reffJenis, Constant.jenisPulang)
            }
            3 -> {
                bundle.putString(Constant.reffJenis, Constant.jenisApel)
            }
        }
        fragmentTujuan.arguments = bundle
        navController.navigate(R.id.navAbsen, bundle)
    }

    private fun checkGPS() : Boolean{
        val service = activity?.getSystemService(LOCATION_SERVICE) as LocationManager?
        val enabled = service?.isProviderEnabled(LocationManager.GPS_PROVIDER)

        // Check if enabled and if not send user to the GPS settings
        return if (enabled != null && !enabled) {
            Toast.makeText(activity, "Nyalakan GPS terlebih dahulu", Toast.LENGTH_LONG).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            activity?.startActivity(intent)
            false
        } else{
            true
        }
    }
}