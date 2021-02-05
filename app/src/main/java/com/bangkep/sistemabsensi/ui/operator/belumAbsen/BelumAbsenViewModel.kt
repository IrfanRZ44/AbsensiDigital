package com.bangkep.sistemabsensi.ui.operator.belumAbsen

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.base.BaseViewModel
import com.bangkep.sistemabsensi.model.*
import com.bangkep.sistemabsensi.ui.operator.detailPegawai.DetailPegawaiFragment
import com.bangkep.sistemabsensi.utils.Constant
import com.bangkep.sistemabsensi.utils.DataSave
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

class BelumAbsenViewModel(private val rcData: RecyclerView,
                          private val context: Context?,
                          private val savedData: DataSave,
                          private val navController: NavController
) : BaseViewModel() {
    val listData = ArrayList<ModelUser>()
    var adapter: AdapterBelumAbsen? = null

    fun initAdapter() {
        adapter = AdapterBelumAbsen(
            listData,
            { dataData: ModelUser, idHari: String -> onClickItem(dataData, idHari) },
            navController
        )
        rcData.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rcData.adapter = adapter
    }

    fun getHariKerja(search: String?){
        isShowLoading.value = true
        listData.clear()

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
                        adapter?.idHari = result.id_hari
                        savedData.getDataUser()?.idDinas?.let {
                            getUserNotAbsensi(result.id_hari,
                                it, search
                            )
                        }
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

    private fun getUserNotAbsensi(idHari: String, idDinas: String, search: String?){
        isShowLoading.value = true

        val body = HashMap<String, String>()
        body["id_hari"] = idHari
        body["id_dinas"] = idDinas

        if (!search.isNullOrEmpty()){
            body["search"] = search
        }

        RetrofitUtils.getUserNotAbsensi(body, object : Callback<ModelListBelumAbsensi> {
            override fun onResponse(
                call: Call<ModelListBelumAbsensi>,
                response: Response<ModelListBelumAbsensi>
            ) {
                isShowLoading.value = false
                val result = response.body()

                if (result?.response == Constant.reffSuccess){
                    for (i in result.list_absen.indices){
                        listData.add(result.list_absen[i])
                        adapter?.notifyDataSetChanged()
                    }
                }
                else{
                    message.value = result?.response
                }
            }

            override fun onFailure(
                call: Call<ModelListBelumAbsensi>,
                t: Throwable
            ) {
                isShowLoading.value = false
                message.value = t.message
            }
        })
    }

    private fun onClickItem(data: ModelUser, idHari: String) {
        val bundle = Bundle()
        val fragmentTujuan = DetailPegawaiFragment()
        bundle.putParcelable(Constant.reffPegawai, data)
        bundle.putString(Constant.reffIdHari, idHari)
        fragmentTujuan.arguments = bundle
        navController.navigate(R.id.navDetailPegawai, bundle)
    }
}