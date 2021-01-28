@file:Suppress("DEPRECATION")

package com.bangkep.sistemabsensi.ui.auth.login

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.base.BaseViewModel
import com.bangkep.sistemabsensi.model.ModelUser
import com.bangkep.sistemabsensi.utils.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.InstanceIdResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginViewModel(
    private val navController: NavController,
    private val savedData: DataSave?,
    private val activity: Activity?) : BaseViewModel() {
    val etUsername = MutableLiveData<String>()
    val etPassword = MutableLiveData<String>()

    fun onClickBack(){
        activity?.let { dismissKeyboard(it) }
        navController.navigate(R.id.splashFragment)
    }

    fun onClickLogin(){
        val username = etUsername.value
        val password = etPassword.value

        if (!username.isNullOrEmpty() && !password.isNullOrEmpty()){
            val body = HashMap<String, String>()
            body["username"] = username
            body["password"] = password

            getUserToken(body)
        }
        else{
            if (username.isNullOrEmpty()){
                message.value = "Username tidak boleh kosong"
            }
            else if (password.isNullOrEmpty()){
                message.value = "Password tidak boleh kosong"
            }
        }
    }

    private fun getUserToken(body: HashMap<String, String>) {
        isShowLoading.value = true

        val onCompleteListener =
            OnCompleteListener<InstanceIdResult> { result ->
                if (result.isSuccessful) {
                    try {
                        val tkn = result.result?.token
                            ?: throw Exception("Error, kesalahan saat menyimpan token")

                        body["token"] = tkn
                        loginUser(body)
                    } catch (e: Exception) {
                        isShowLoading.value = false
                        message.value = e.message
                    }
                } else {
                    isShowLoading.value = false
                    message.value = "Gagal mendapatkan token"
                }
            }

        FirebaseUtils.getUserToken(
            onCompleteListener
        )
    }

    private fun loginUser(body: HashMap<String, String>){
        RetrofitUtils.loginUser(body,
            object : Callback<ModelUser> {
                override fun onResponse(
                    call: Call<ModelUser>,
                    response: Response<ModelUser>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.response == Constant.reffSuccess){
                        savedData?.setDataObject(result, Constant.reffUser)
                        navController.navigate(R.id.splashFragment)
                    }
                    else{
                        message.value = result?.response
                    }
                }

                override fun onFailure(
                    call: Call<ModelUser>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    message.value = t.message
                }
            })
    }
}