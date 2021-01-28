package com.bangkep.sistemabsensi.ui.operator.sudahAbsen

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.base.BaseViewModel
import com.bangkep.sistemabsensi.model.ModelAbsensi
import com.bangkep.sistemabsensi.model.ModelHariKerja
import com.bangkep.sistemabsensi.model.ModelListAbsensi
import com.bangkep.sistemabsensi.model.ModelUser
import com.bangkep.sistemabsensi.ui.operator.detailAbsensi.DetailAbsensiFragment
import com.bangkep.sistemabsensi.utils.Constant
import com.bangkep.sistemabsensi.utils.RetrofitUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class SudahAbsenViewModel(private val rcData: RecyclerView,
                          private val context: Context?,
                          private val navController: NavController
) : BaseViewModel() {
    val listData = ArrayList<ModelAbsensi>()
    val listDataSearch = ArrayList<ModelAbsensi>()
    val listNama = ArrayList<ModelAbsensi>()
    var adapter: AdapterSudahAbsen? = null

    fun initAdapter() {
        adapter = AdapterSudahAbsen(
            listData,
            { textName: AppCompatTextView, username: String, item: ModelAbsensi, btnKonfirmasi ->
                getDataPegawai(textName, username, item, btnKonfirmasi) },
            navController
        )
        rcData.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rcData.adapter = adapter
        rcData.isNestedScrollingEnabled = false
    }

    fun cekList() {
        isShowLoading.value = false

        if (listData.size == 0) status.value = Constant.noData
        else status.value = ""
    }

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
                        getAbsensiByDate(result.id_hari)
                    }
                    else{
                        if (result?.response == Constant.tanggalTidakTersedia){
                            message.value = "Sekarang bukan hari kerja"
                        }
                        else {
                            message.value = result?.response
                        }
                    }
                }

                override fun onFailure(
                    call: Call<ModelHariKerja>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    message.value = t.message
                }
            })
    }

    private fun getAbsensiByDate(idHari: String){
        isShowLoading.value = true

        val body = HashMap<String, String>()
        body["id_hari"] = idHari

        RetrofitUtils.getAbsensiByDate(body, object : Callback<ModelListAbsensi> {
            override fun onResponse(
                call: Call<ModelListAbsensi>,
                response: Response<ModelListAbsensi>
            ) {
                isShowLoading.value = false
                val result = response.body()

                if (result?.response == Constant.reffSuccess){
                    listData.clear()
                    for (i in result.list_absen.indices){
                        listData.add(result.list_absen[i])
                        listDataSearch.add(result.list_absen[i])
                        listNama.add(result.list_absen[i])
                        adapter?.notifyDataSetChanged()
                    }

                    cekList()
                }
                else{
                    message.value = result?.response
                    cekList()
                }
            }

            override fun onFailure(
                call: Call<ModelListAbsensi>,
                t: Throwable
            ) {
                isShowLoading.value = false
                message.value = t.message
            }
        })
    }

    private fun getDataPegawai(textNama: AppCompatTextView, username: String,
                               dataItem: ModelAbsensi, btnKonfirmasi: AppCompatButton){
        isShowLoading.value = true

        val body = HashMap<String, String>()
        body["username"] = username

        RetrofitUtils.getDataPegawai(body, object : Callback<ModelUser> {
            override fun onResponse(
                call: Call<ModelUser>,
                response: Response<ModelUser>
            ) {
                isShowLoading.value = false
                val result = response.body()

                if (result?.response == Constant.reffSuccess){
                    textNama.text = result.nama

                    btnKonfirmasi.setOnClickListener {
                        onClickItem(dataItem, result)
                    }
                }
                else{
                    message.value = result?.response

                    btnKonfirmasi.setOnClickListener {
                        onClickItem(dataItem, null)
                    }
                }
            }

            override fun onFailure(
                call: Call<ModelUser>,
                t: Throwable
            ) {
                isShowLoading.value = false
                message.value = t.message

                btnKonfirmasi.setOnClickListener {
                    onClickItem(dataItem, null)
                }
            }
        })
    }

    private fun onClickItem(data: ModelAbsensi, dataPegawai: ModelUser?) {
        val bundle = Bundle()
        val fragmentTujuan = DetailAbsensiFragment()
        bundle.putParcelable(Constant.reffUser, dataPegawai)
        bundle.putParcelable(Constant.reffAbsensi, data)
        fragmentTujuan.arguments = bundle
        navController.navigate(R.id.navDetailAbsensi, bundle)
    }
}