@file:Suppress("DEPRECATION")

package com.bangkep.sistemabsensi.ui.auth.splash

import android.app.Activity
import android.content.Intent
import android.os.CountDownTimer
import androidx.navigation.NavController
import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.base.BaseViewModel
import com.bangkep.sistemabsensi.ui.operator.OperatorActivity
import com.bangkep.sistemabsensi.ui.pegawai.PegawaiActivity
import com.bangkep.sistemabsensi.utils.Constant
import com.bangkep.sistemabsensi.utils.DataSave

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

                if (!username.isNullOrEmpty() || !level.isNullOrEmpty()){
                    isShowLoading.value = false

                    when (level) {
                        Constant.operator -> {
                            val intent = Intent(activity, OperatorActivity::class.java)
                            activity?.startActivity(intent)
                            activity?.finish()
                        }
                        Constant.pegawai -> {
                            val intent = Intent(activity, PegawaiActivity::class.java)
                            activity?.startActivity(intent)
                            activity?.finish()
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
}