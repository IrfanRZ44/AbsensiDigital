package com.bangkep.sistemabsensi.ui.operator.detailAbsensi

import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.base.BaseViewModel
import com.bangkep.sistemabsensi.model.ModelAbsensi
import com.bangkep.sistemabsensi.model.ModelResult
import com.bangkep.sistemabsensi.model.ModelUser
import com.bangkep.sistemabsensi.utils.Constant
import com.bangkep.sistemabsensi.utils.RetrofitUtils
import com.bangkep.sistemabsensi.utils.onClickFoto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailAbsensiViewModel(
    private val navController: NavController
) : BaseViewModel() {
    val dataAbsensi = MutableLiveData<ModelAbsensi>()
    val dataUser = MutableLiveData<ModelUser>()
    val dataNipNama = MutableLiveData<String>()
    val jenisAbsen = MutableLiveData<String>()

    fun clickFoto(){
        onClickFoto(dataAbsensi.value?.img?:"", navController)
    }

    fun onClickSend(status: Int){
        val idAbsen = dataAbsensi.value?.id_absensi
        val jenis = dataAbsensi.value?.jenis

        if (!idAbsen.isNullOrEmpty() && !jenis.isNullOrEmpty() && (status == 1 || status == 2)){
            val body = HashMap<String, String>()
            body[Constant.reffIdAbsen] = idAbsen
            body[Constant.status] = status.toString()
            body[Constant.jenis] = jenis

            validateData(body, status.toString())
        }
        else{
            message.value = "Error, terjadi kesalahan database"
        }
    }

    private fun validateData(body : HashMap<String, String>, status: String){
        RetrofitUtils.updateAbsensiStatus(body,
            object : Callback<ModelResult> {
                override fun onResponse(
                    call: Call<ModelResult>,
                    response: Response<ModelResult>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.response == Constant.reffSuccess){
                        navController.navigate(R.id.navBeranda)
                        if (status == "2"){
                            message.value = "Absensi ditolak"
                        }
                        else{
                            message.value = "Berhasil mengkonfirmasi absen"
                        }
                    }
                    else{
                        message.value = result?.response
                    }
                }

                override fun onFailure(
                    call: Call<ModelResult>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    message.value = t.message
                }
            })
    }
}