package com.kabirland.absensidigital_demo.ui.operator.detailAbsensi

import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.kabirland.absensidigital_demo.R
import com.kabirland.absensidigital_demo.base.BaseViewModel
import com.kabirland.absensidigital_demo.model.ModelResult
import com.kabirland.absensidigital_demo.model.ModelSudahAbsensi
import com.kabirland.absensidigital_demo.services.notification.model.Notification
import com.kabirland.absensidigital_demo.services.notification.model.Sender
import com.kabirland.absensidigital_demo.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailAbsensiViewModel(
    private val navController: NavController,
    private val savedData: DataSave
) : BaseViewModel() {
    val dataAbsensi = MutableLiveData<ModelSudahAbsensi>()
    val dataNipNama = MutableLiveData<String>()
    val jenisAbsen = MutableLiveData<String>()

    fun clickFoto(){
        onClickFoto(dataAbsensi.value?.img?:"", navController)
    }

    fun onClickSend(status: Int){
        val idAbsen = dataAbsensi.value?.id_absensi
        val jenis = dataAbsensi.value?.jenis
        val idOperator = savedData.getDataUser()?.idUser

        if (!idAbsen.isNullOrEmpty() && !jenis.isNullOrEmpty() && !idOperator.isNullOrEmpty() && (status == 1 || status == 2)){
            val body = HashMap<String, String>()
            body[Constant.reffIdAbsen] = idAbsen
            body[Constant.status] = status.toString()
            body[Constant.jenis] = jenis
            body[Constant.confirmed_by] = idOperator

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

                            val notification = Notification(
                                "Absensi Anda ditolak",
                                "Absensi Masker"
                                , "com.kabirland.absensidigital_demo.fcm_TARGET_NOTIFICATION_PEGAWAI"
                            )

                            val sender = Sender(notification, dataAbsensi.value?.token)
                            FirebaseUtils.sendNotif(sender)
                        }
                        else{
                            message.value = "Berhasil mengkonfirmasi absen"

                            val notification = Notification(
                                "Absensi Anda sudah dikonfirmasi",
                                "Absensi Masker"
                                , "com.kabirland.absensidigital_demo.fcm_TARGET_NOTIFICATION_PEGAWAI"
                            )

                            val sender = Sender(notification, dataAbsensi.value?.token)
                            FirebaseUtils.sendNotif(sender)
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