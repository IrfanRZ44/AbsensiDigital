package com.bangkep.sistemabsensi.ui.pegawai.absenDatang

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.bangkep.sistemabsensi.base.BaseViewModel
import com.bangkep.sistemabsensi.model.ModelResultAbsen
import com.bangkep.sistemabsensi.utils.Constant
import com.bangkep.sistemabsensi.utils.DataSave
import com.bangkep.sistemabsensi.utils.RetrofitUtils
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap

class AbsenDatangViewModel(
    private val activity: Activity?,
    private val navController: NavController,
    val savedData: DataSave
) : BaseViewModel() {
    val foto = MutableLiveData<Uri>()
    var idHari : String? = ""

    fun uploadMultipart(id_absensi: String, filePath: String, activity: Activity){
        val uploadRequest = MultipartUploadRequest(activity, Constant.reffFotoAbsen)
        uploadRequest.setMethod("POST")
        uploadRequest.addFileToUpload(
                filePath = filePath,
                parameterName = "image"
            )
        uploadRequest.addParameter(Constant.reffIdAbsen, id_absensi)
        uploadRequest.startUpload()
    }


    fun onClickAbsen(){
        isShowLoading.value = true
        @SuppressLint("SimpleDateFormat")
        val dateCreated = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constant.dateFormat2))
        } else {
            SimpleDateFormat(Constant.dateFormat2).format(Date())
        }

        val nip = savedData.getDataUser()?.username
        val id_hari = idHari
        val pathFoto = foto.value?.path
        val act = activity

        if (!nip.isNullOrEmpty() && !id_hari.isNullOrEmpty() && !pathFoto.isNullOrEmpty() && act != null){
            val body = HashMap<String, String>()
            body["nip"] = nip
            body["id_hari"] = id_hari
            body["latitude"] = ""
            body["longitude"] = ""
            body["jenis"] = Constant.jenisMasuk
            body["date_created"] = dateCreated

            sendData(body, pathFoto, act)
        }
        else{
            if (pathFoto.isNullOrEmpty()){
                isShowLoading.value = true
                message.value = "Error, Anda harus mengupload foto"
            }
            else{
                isShowLoading.value = true
                message.value = "Error, mohon login ulang"
            }
        }
    }

    private fun sendData(body: HashMap<String, String>, pathFoto: String, activity: Activity){
        RetrofitUtils.createAbsensi(body,
            object : Callback<ModelResultAbsen> {
                override fun onResponse(
                    call: Call<ModelResultAbsen>,
                    response: Response<ModelResultAbsen>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.response == Constant.reffSuccess){
                        val idHari = result.id_hari

                        if (idHari.isNotEmpty()){
                            uploadMultipart(idHari, pathFoto, activity)
                        }
                        else{
                            message.value = "Terjadi kesalahan penyimpanan, mohon login ulang"
                        }
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