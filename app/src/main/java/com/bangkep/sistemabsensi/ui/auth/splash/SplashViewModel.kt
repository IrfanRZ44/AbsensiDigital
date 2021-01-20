@file:Suppress("DEPRECATION")

package com.bangkep.sistemabsensi.ui.auth.splash

import android.app.Activity
import android.content.Intent
import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.base.BaseViewModel
import com.bangkep.sistemabsensi.model.ModelUser
import com.bangkep.sistemabsensi.ui.main.MainActivity
import com.bangkep.sistemabsensi.utils.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.InstanceIdResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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