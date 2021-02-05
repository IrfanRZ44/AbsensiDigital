package com.bangkep.sistemabsensi.ui.pegawai.riwayat

import android.content.Context
import android.os.Bundle
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.base.BaseViewModel
import com.bangkep.sistemabsensi.model.ModelAbsensi
import com.bangkep.sistemabsensi.model.ModelListAbsensi
import com.bangkep.sistemabsensi.ui.pegawai.detailRiwayat.DetailRiwayatFragment
import com.bangkep.sistemabsensi.utils.Constant
import com.bangkep.sistemabsensi.utils.RetrofitUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RiwayatViewModel(private val rcData: RecyclerView,
                       private val context: Context?,
                       private val navController: NavController
) : BaseViewModel() {
    val listData = ArrayList<ModelAbsensi>()
    val listDataSearch = ArrayList<ModelAbsensi>()
    val listNama = ArrayList<ModelAbsensi>()
    var adapter: AdapterDataRiwayat? = null

    fun initAdapter() {
        adapter = AdapterDataRiwayat(
            listData,
            { dataData: ModelAbsensi -> onClickItem(dataData) },
            navController
        )
        rcData.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rcData.adapter = adapter
        rcData.isNestedScrollingEnabled = true
    }

    fun cekList() {
        isShowLoading.value = false

        if (listData.size == 0) status.value = Constant.noData
        else status.value = ""
    }

    fun getListRiwayat(nip: String){
        isShowLoading.value = true

        val body = HashMap<String, String>()
        body["nip"] = nip

        RetrofitUtils.getRiwayat(body, object : Callback<ModelListAbsensi> {
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

    private fun onClickItem(data: ModelAbsensi) {
        val bundle = Bundle()
        val fragmentTujuan =
            DetailRiwayatFragment()
        bundle.putParcelable(Constant.reffAbsensi, data)
        fragmentTujuan.arguments = bundle
        navController.navigate(R.id.navDetailRiwayat, bundle)
    }
}