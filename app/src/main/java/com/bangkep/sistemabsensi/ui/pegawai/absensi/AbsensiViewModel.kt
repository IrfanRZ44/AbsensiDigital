package com.bangkep.sistemabsensi.ui.pegawai.absensi

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.base.BaseViewModel
import com.bangkep.sistemabsensi.model.ModelResultAbsen
import com.bangkep.sistemabsensi.utils.Constant
import com.bangkep.sistemabsensi.utils.DataSave
import com.bangkep.sistemabsensi.utils.RetrofitUtils
import com.google.android.gms.location.LocationServices
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap

class AbsensiViewModel(
    private val activity: Activity?,
    private val navController: NavController,
    val savedData: DataSave) : BaseViewModel() {
    val foto = MutableLiveData<Uri>()
    var idHari : String? = ""
    var idAbsensi : String? = ""
    var jenisAbsensi : String? = ""

    fun uploadMultipart(id_absensi: String, nameFile: String, filePath: String, activity: Activity){
        val uploadRequest = MultipartUploadRequest(activity, Constant.reffFotoAbsen)
        uploadRequest.setMethod("POST")
        uploadRequest.addFileToUpload(
                filePath = filePath,
                parameterName = "image"
            )
        uploadRequest.addParameter(Constant.reffIdAbsen, id_absensi)
        uploadRequest.addParameter(Constant.reffNameFile, nameFile)
        uploadRequest.startUpload()
    }

    private fun getCurrentLocation(act: Activity){
        if (ContextCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                act, Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            message.value = "Anda belum mengizinkan akses lokasi aplikasi ini"

            ActivityCompat.requestPermissions(
                act,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                Constant.codeRequestLocation
            )
        } else {
            isShowLoading.value = true
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(act)

            fusedLocationClient?.lastLocation?.addOnSuccessListener { location: Location? ->
                isShowLoading.value = false
                val lat = location?.latitude
                val longit = location?.longitude

                if (lat != null && longit != null){
                    validateData(lat.toString(), longit.toString())
                }
                else{
                    message.value = "Error, gagal mengambil lokasi terkini"
                }
            }

            fusedLocationClient.lastLocation.addOnFailureListener {
                isShowLoading.value = false
                message.value = it.message
            }
        }
    }

    private fun validateData(latitude: String, longitude: String){
        isShowLoading.value = true

        @SuppressLint("SimpleDateFormat")
        val dateCreated = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constant.dateFormat2))
        } else {
            SimpleDateFormat(Constant.dateFormat2).format(Date())
        }

        val nip = savedData.getDataUser()?.username
        val hariId = idHari
        val pathFoto = foto.value?.path
        val act = activity
        val jenis = jenisAbsensi

        if (!nip.isNullOrEmpty() && !hariId.isNullOrEmpty() && !pathFoto.isNullOrEmpty() && !jenis.isNullOrEmpty() && act != null){
            val body = HashMap<String, String>()
            body[Constant.reffNip] = nip
            body[Constant.reffIdHari] = hariId
            body["latitude"] = latitude
            body["longitude"] = longitude
            body["jenis"] = jenis
            body["date_created"] = dateCreated
            val idAbsen = idAbsensi

            if (!idAbsen.isNullOrEmpty()){
                body[Constant.reffIdAbsen] = idAbsen
                updateAbsensi(body, pathFoto, act)
            }
            else{
                sendData(body, pathFoto, act)
            }
        }
        else{
            when {
                pathFoto.isNullOrEmpty() -> {
                    isShowLoading.value = false
                    message.value = "Error, Anda harus mengupload foto"
                }
                jenis.isNullOrEmpty() -> {
                    isShowLoading.value = false
                    message.value = "Error, terjadi kesalahan sistem database"
                }
                else -> {
                    isShowLoading.value = false
                    message.value = "Error, mohon login ulang"
                }
            }
        }
    }

    fun onClickAbsen(){
        isShowLoading.value = true
        activity?.let { getCurrentLocation(it) }
    }

    private fun sendData(body: HashMap<String, String>, pathFoto: String, activity: Activity){
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
                        val idAbsen = result.id_absensi
                        val nameFile = "${System.currentTimeMillis()}__${savedData.getDataUser()?.username}"

                        if (idAbsen.isNotEmpty() && nameFile.isNotEmpty()){
                            uploadMultipart(idAbsen, nameFile, pathFoto, activity)
                            navController.navigate(R.id.navBeranda)
                            message.value = "Berhasil absensi"
                        }
                        else{
                            message.value = "Gagal mengupload foto"
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

    private fun updateAbsensi(body: HashMap<String, String>, pathFoto: String, activity: Activity){
        isShowLoading.value = true

        RetrofitUtils.updateAbsensi(body,
            object : Callback<ModelResultAbsen> {
                override fun onResponse(
                    call: Call<ModelResultAbsen>,
                    response: Response<ModelResultAbsen>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.response == Constant.reffSuccess){
                        val idAbsen = result.id_absensi
                        val nameFile = "${System.currentTimeMillis()}__${savedData.getDataUser()?.username}"

                        if (idAbsen.isNotEmpty() && nameFile.isNotEmpty()){
                            uploadMultipart(idAbsen, nameFile, pathFoto, activity)
                            navController.navigate(R.id.navBeranda)
                            message.value = "Berhasil melakukan absensi ulang"
                        }
                        else{
                            message.value = "Gagal mengupload foto"
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