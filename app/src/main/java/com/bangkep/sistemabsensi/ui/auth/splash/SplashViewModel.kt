@file:Suppress("DEPRECATION")

package com.bangkep.sistemabsensi.ui.auth.splash

import android.app.Activity
import android.content.Intent
import android.os.CountDownTimer
import androidx.navigation.NavController
import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.base.BaseViewModel
import com.bangkep.sistemabsensi.ui.main.MainActivity
import com.bangkep.sistemabsensi.utils.DataSave

class SplashViewModel(
    private val navController: NavController,
    private val savedData: DataSave?,
    private val activity: Activity?) : BaseViewModel() {

    fun checkUser(){
        object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                if (savedData?.getDataUser()?.username.isNullOrEmpty() || savedData?.getDataUser()?.phone.isNullOrEmpty()){
                    navController.navigate(R.id.loginFragment)
                }
                else{
                    isShowLoading.value = false
                    val intent = Intent(activity, MainActivity::class.java)
                    activity?.startActivity(intent)
                    activity?.finish()
                }
            }
        }.start()
    }
}