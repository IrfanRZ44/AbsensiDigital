package com.bangkep.sistemabsensi.ui.pegawai.profil

import android.app.Activity
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.bangkep.sistemabsensi.base.BaseViewModel
import com.bangkep.sistemabsensi.model.ModelResult
import com.bangkep.sistemabsensi.utils.Constant
import com.bangkep.sistemabsensi.utils.DataSave
import com.bangkep.sistemabsensi.utils.RetrofitUtils
import com.bangkep.sistemabsensi.utils.dismissKeyboard
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfilViewModel(
    private val activity: Activity?,
    private val savedData: DataSave
) : BaseViewModel(){
    val foto = MutableLiveData<Uri>()
    val etNama = MutableLiveData<String>()
    val etPassword = MutableLiveData<String>()
    val etConfirmPassword = MutableLiveData<String>()
    val etEmail = MutableLiveData<String>()

    fun setData(){
        etNama.value = savedData.getDataUser()?.nama
        etEmail.value = savedData.getDataUser()?.email
        foto.value = Uri.parse(savedData.getDataUser()?.foto)
    }

    fun onClickSave(){
        val nama = etNama.value
        val email = etEmail.value
        val password = etPassword.value
        val confirmPassword = etConfirmPassword.value
        val idUser = savedData.getDataUser()?.idUser

        if (!email.isNullOrEmpty() && !idUser.isNullOrEmpty()){
            val body = HashMap<String, String>()
            body["mail"] = email
            body["id_users"] = idUser
            body["nama"] = nama?:""

            if (!password.isNullOrEmpty() && !confirmPassword.isNullOrEmpty() && password == confirmPassword){
                body["password"] = password
            }
            else if (!password.isNullOrEmpty() && !confirmPassword.isNullOrEmpty()){
                if (password != confirmPassword){
                    Toast.makeText(activity, "Password yang berbeda tidak tersimpan", Toast.LENGTH_LONG).show()
                }
            }
            else if (!password.isNullOrEmpty()){
                if (password != confirmPassword){
                    Toast.makeText(activity, "Password yang berbeda tidak tersimpan", Toast.LENGTH_LONG).show()
                }
            }
            else if (!confirmPassword.isNullOrEmpty()){
                if (password != confirmPassword){
                    Toast.makeText(activity, "Password yang berbeda tidak tersimpan", Toast.LENGTH_LONG).show()
                }
            }

            updateProfil(body)
        }
        else{
            if (email.isNullOrEmpty() && idUser.isNullOrEmpty()){
                message.value = "Error, mohon mengisi form email"
            }
        }
    }

    private fun updateProfil(body: HashMap<String, String>){
        activity?.let { dismissKeyboard(it) }
        isShowLoading.value = true

        RetrofitUtils.updateProfil(body,
            object : Callback<ModelResult> {
                override fun onResponse(
                    call: Call<ModelResult>,
                    response: Response<ModelResult>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.response == Constant.reffSuccess){
                        val dataUser = savedData.getDataUser()
                        dataUser?.nama = body["nama"]?:""
                        dataUser?.email = body["mail"]?:""

                        savedData.setDataObject(dataUser, Constant.reffUser)
                        message.value = "Berhasil menyimpan profil"
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

    fun uploadMultipart(id_users: String, nameFile: String, filePath: String, activity: Activity){
        val uploadRequest = MultipartUploadRequest(activity, Constant.reffFotoProfil)
        uploadRequest.setMethod("POST")
        uploadRequest.addFileToUpload(
            filePath = filePath,
            parameterName = "image"
        )
        uploadRequest.addParameter(Constant.reffIdUser, id_users)
        uploadRequest.addParameter(Constant.reffNameFile, nameFile)
        uploadRequest.startUpload()
    }
}