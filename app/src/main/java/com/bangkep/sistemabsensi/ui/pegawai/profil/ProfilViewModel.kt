package com.bangkep.sistemabsensi.ui.pegawai.profil

import android.app.Activity
import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.base.BaseViewModel
import com.bangkep.sistemabsensi.model.ModelResultAbsen
import com.bangkep.sistemabsensi.utils.Constant
import com.bangkep.sistemabsensi.utils.DataSave
import com.bangkep.sistemabsensi.utils.RetrofitUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfilViewModel(private val navController: NavController,
                      private val context: Context?,
                      private val savedData: DataSave) : BaseViewModel(){
    val imgFoto = MutableLiveData<Uri>()
    val etNama = MutableLiveData<String>()
    val etPassword = MutableLiveData<String>()
    val etConfirmPassword = MutableLiveData<String>()
    val etEmail = MutableLiveData<String>()

    fun setData(){
        etNama.value = savedData.getDataUser()?.nama
        etEmail.value = savedData.getDataUser()?.email
        imgFoto.value = Uri.parse(savedData.getDataUser()?.foto)
    }

    fun onClickSave(){
        val nama = etNama.value
        val email = etEmail.value
        val password = etPassword.value
        val confirmPassword = etConfirmPassword.value
        val idUser = savedData.getDataUser()?.idUser

        if (!email.isNullOrEmpty() && !idUser.isNullOrEmpty()){
            val body = HashMap<String, String>()
            body["nama"] = nama?:""
            if (!password.isNullOrEmpty() && !confirmPassword.isNullOrEmpty() && password == confirmPassword){
                body["password"] = password
            }
            body["latitude"] = idUser
        }
        else{
            if (email.isNullOrEmpty() && idUser.isNullOrEmpty()){
                message.value = "Error, mohon mengisi form email"
            }
        }
    }

//    private fun updateAbsensi(body: HashMap<String, String>, pathFoto: String, activity: Activity){
//        isShowLoading.value = true
//
//        RetrofitUtils.updateAbsensi(body,
//            object : Callback<ModelResultAbsen> {
//                override fun onResponse(
//                    call: Call<ModelResultAbsen>,
//                    response: Response<ModelResultAbsen>
//                ) {
//                    isShowLoading.value = false
//                    val result = response.body()
//
//                    if (result?.response == Constant.reffSuccess){
//                        val idAbsen = result.id_absensi
//                        val nameFile = "${System.currentTimeMillis()}__${savedData.getDataUser()?.username}"
//
//                        if (idAbsen.isNotEmpty() && nameFile.isNotEmpty()){
//                            uploadMultipart(idAbsen, nameFile, pathFoto, activity)
//                            navController.navigate(R.id.navBeranda)
//                            message.value = "Berhasil melakukan absensi ulang"
//                        }
//                        else{
//                            message.value = "Gagal mengupload foto"
//                        }
//                    }
//                    else{
//                        message.value = result?.response
//                    }
//                }
//
//                override fun onFailure(
//                    call: Call<ModelResultAbsen>,
//                    t: Throwable
//                ) {
//                    isShowLoading.value = false
//                    message.value = t.message
//                }
//            })
//    }
}