package com.kabirland.absensidigital_demo.ui.auth.splash

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.CountDownTimer
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.kabirland.absensidigital_demo.R
import com.kabirland.absensidigital_demo.base.BaseViewModel
import com.kabirland.absensidigital_demo.model.ModelResult
import com.kabirland.absensidigital_demo.model.ModelResultInfoApps
import com.kabirland.absensidigital_demo.model.ModelUser
import com.kabirland.absensidigital_demo.ui.operator.OperatorActivity
import com.kabirland.absensidigital_demo.ui.pegawai.PegawaiActivity
import com.kabirland.absensidigital_demo.utils.Constant
import com.kabirland.absensidigital_demo.utils.DataSave
import com.kabirland.absensidigital_demo.utils.RetrofitUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("StaticFieldLeak")
class SplashViewModel(
    private val navController: NavController,
    private val savedData: DataSave?,
    private val activity: Activity?) : BaseViewModel() {
    val isShowUpdate = MutableLiveData<Boolean>()
    var linkDownload = ""

    fun onClickUpdate(){
        val defaultBrowser = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_BROWSER)
        defaultBrowser.data = Uri.parse(linkDownload)
        activity?.startActivity(defaultBrowser)
    }

    private fun checkUser(){
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

    fun getInfoApps(){
        isShowLoading.value = false

        RetrofitUtils.getInfoApps(
            object : Callback<ModelResultInfoApps> {
                override fun onResponse(
                    call: Call<ModelResultInfoApps>,
                    response: Response<ModelResultInfoApps>
                ) {
                    val result = response.body()
                    isShowLoading.value = false
                    val act = activity

                    if (result?.response == Constant.reffSuccess && act != null){
                        val manager = act.packageManager
                        val info = manager?.getPackageInfo(
                            act.packageName, 0
                        )
                        val versionApps = info?.versionName

                        if (result.version_apps == versionApps){
                            savedData?.setDataString(result.message, Constant.reffMessageApps)
                            checkUser()
                        }
                        else{
                            message.value = "Mohon update versi aplikasi ke $versionApps"
                            linkDownload = result.link
                            isShowUpdate.value = true
                        }
                    }
                    else{
                        message.value = "Error, tidak dapat terhubung ke server"
                        isShowUpdate.value = false
                    }
                }

                override fun onFailure(
                    call: Call<ModelResultInfoApps>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    val msg = t.message
                    if (msg != null && msg.contains("Unable to resolve host")){
                        message.value = "Error, tidak dapat terhubung ke server"
                    }
                    else{
                        message.value = t.message
                    }
                    isShowUpdate.value = false
                }
            })
    }
}