package com.bangkep.sistemabsensi.ui.pegawai.beranda

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.base.BaseViewModel
import com.bangkep.sistemabsensi.model.ModelAbsensi
import com.bangkep.sistemabsensi.model.ModelHariKerja
import com.bangkep.sistemabsensi.model.ModelListAbsensi
import com.bangkep.sistemabsensi.ui.pegawai.absenDatang.AbsenDatangFragment
import com.bangkep.sistemabsensi.utils.Constant
import com.bangkep.sistemabsensi.utils.DataSave
import com.bangkep.sistemabsensi.utils.RetrofitUtils
import com.bangkep.sistemabsensi.utils.showLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap

class BerandaViewModel(
    private val context: Context?,
    private val navController: NavController,
    private val btnApel: AppCompatButton,
    private val savedData: DataSave
) : BaseViewModel() {
    val isDatang = MutableLiveData<Boolean>()
    val isPulang = MutableLiveData<Boolean>()
    val isApel = MutableLiveData<Boolean>()
    var id_hari : String? = ""

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
                        val idHari = result.id_hari
                        id_hari = result.id_hari

                        if (!nip.isNullOrEmpty() && idHari.isNotEmpty()){
                            val dataSend = HashMap<String, String>()
                            dataSend["nip"] = nip
                            dataSend["id_hari"] = idHari

                            if (result.keterangan == Constant.hariAbsen){
                                getListAbsensi(dataSend)
                            }
                            else if (result.keterangan == Constant.hariAbsenApel){
                                getAbsensiApel(dataSend, result.masuk_apel, result.pulang_apel)
                                getListAbsensi(dataSend)
                            }
                        }
                        else{
                            message.value = "Terjadi kesalahan penyimpanan, mohon login ulang"
                            isDatang.value = false
                            isPulang.value = false
                            btnApel.visibility = View.GONE
                        }
                    }
                    else{
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
                    isShowLoading.value = false
                    message.value = t.message
                    isDatang.value = false
                    isPulang.value = false
                    btnApel.visibility = View.GONE
                }
            })
    }

    fun getListAbsensi(body : HashMap<String, String>){
        isShowLoading.value = true

        RetrofitUtils.getAbsensi(body,
            object : Callback<ModelListAbsensi> {
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
                            } else if (dataAbsen.jenis == Constant.jenisCuti){
                                if (!izin){
                                    izin = true
                                }
                            } else if (dataAbsen.jenis == Constant.jenisIzin){
                                if (!izin){
                                    izin = true
                                }
                            } else if (dataAbsen.jenis == Constant.jenisSakit){
                                if (!izin){
                                    izin = true
                                }
                            } else if (dataAbsen.jenis == Constant.jenisTLD){
                                if (!izin){
                                    izin = true
                                }
                            } else if (dataAbsen.jenis == Constant.jenisTDD){
                                if (!izin){
                                    izin = true
                                }
                            } else if (dataAbsen.jenis == Constant.jenisMasuk){
                                if (!izin){
                                    if (!masuk){
                                        masuk = true
                                    }
                                }
                            } else if (dataAbsen.jenis == Constant.jenisPulang){
                                if (!izin){
                                    if (!pulang){
                                        pulang = true
                                    }
                                }
                            }
                        }

                        if (izin){
                            isDatang.value = false
                            isPulang.value = false
                        }
                        else if (!izin && !masuk && !pulang){
                            isDatang.value = true
                            isPulang.value = false
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

        RetrofitUtils.getAbsensiByJenis(body,
            object : Callback<ModelAbsensi> {
                override fun onResponse(
                    call: Call<ModelAbsensi>,
                    response: Response<ModelAbsensi>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.response == Constant.reffSuccess){
                        isApel.value = false
                    }
                    else{
                        @SuppressLint("SimpleDateFormat")
                        val timeNow = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constant.timeFormat))
                        } else {
                            SimpleDateFormat(Constant.timeFormat).format(Date())
                        }

                        showLog("$timeStart __ $timeEnd  __ $timeNow")
                        isApel.value = comparingTimes(timeStart, timeEnd, timeNow)
                    }
                }

                override fun onFailure(
                    call: Call<ModelAbsensi>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    isApel.value = true
                }
            })
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
        if (!id_hari.isNullOrEmpty()){
            val bundle = Bundle()
            val fragmentTujuan = AbsenDatangFragment()
            bundle.putString("id_hari", id_hari)
            fragmentTujuan.arguments = bundle
            navController.navigate(R.id.navAbsenDatang, bundle)
        }
        else{
            message.value = "Error, hari ini tidak tersedia untuk melakukan absensi"
        }
    }

    fun onClickPulang(){
        message.value = "Pulang"
    }

    fun onClickApel(){

    }
}