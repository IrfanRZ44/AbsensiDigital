package com.bangkep.sistemabsensi.ui.auth.splash

import android.app.Activity
import android.content.Intent
import android.os.CountDownTimer
import android.widget.Toast
import androidx.navigation.NavController
import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.base.BaseViewModel
import com.bangkep.sistemabsensi.model.ModelResult
import com.bangkep.sistemabsensi.model.ModelUser
import com.bangkep.sistemabsensi.ui.operator.OperatorActivity
import com.bangkep.sistemabsensi.ui.pegawai.PegawaiActivity
import com.bangkep.sistemabsensi.utils.Constant
import com.bangkep.sistemabsensi.utils.DataSave
import com.bangkep.sistemabsensi.utils.RetrofitUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashViewModel(
    private val navController: NavController,
    private val savedData: DataSave?,
    private val activity: Activity?) : BaseViewModel() {

    fun checkUser(){
        isShowLoading.value = true
        object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                val level = savedData?.getDataUser()?.level
                val username = savedData?.getDataUser()?.username
                val token = savedData?.getDataUser()?.token

                if (!username.isNullOrEmpty() && !level.isNullOrEmpty() && !token.isNullOrEmpty()){
                    isShowLoading.value = false

                    when (level) {
                        Constant.operator -> {
                            val body = HashMap<String, String>()
                            body["username"] = username
                            body["token"] = token
                            checkToken(body, level)
                        }
                        Constant.pegawai -> {
                            val body = HashMap<String, String>()
                            body["username"] = username
                            body["token"] = token
                            checkToken(body, level)
                        }
                        else -> {
                            message.value = "Error, level user tidak ditemukan"
                        }
                    }
                }
                else{
                    navController.navigate(R.id.loginFragment)
                }
            }
        }.start()
    }

    private fun checkToken(body: HashMap<String, String>, level: String){
        RetrofitUtils.checkToken(body,
            object : Callback<ModelResult> {
                override fun onResponse(
                    call: Call<ModelResult>,
                    response: Response<ModelResult>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.response == Constant.reffSuccess){
                        if (level == Constant.pegawai){
                            val intent = Intent(activity, PegawaiActivity::class.java)
                            activity?.startActivity(intent)
                            activity?.finish()
                        }
                        else{
                            val intent = Intent(activity, OperatorActivity::class.java)
                            activity?.startActivity(intent)
                            activity?.finish()
                        }
                    }
                    else{
                        Toast.makeText(activity, result?.response, Toast.LENGTH_LONG).show()
                        savedData?.setDataObject(ModelUser(), Constant.reffUser)
                        navController.navigate(R.id.loginFragment)
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