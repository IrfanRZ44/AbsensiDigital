package com.bangkep.sistemabsensi.ui.auth.register

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.bangkep.sistemabsensi.R
import com.bangkep.sistemabsensi.base.BaseViewModel
import com.bangkep.sistemabsensi.ui.main.MainActivity
import com.bangkep.sistemabsensi.utils.DataSave
import com.bangkep.sistemabsensi.utils.dismissKeyboard

class RegisterViewModel(
    private val activity: Activity?,
    private val dataSave: DataSave?,
    private val navController: NavController
) : BaseViewModel() {
    val userName = MutableLiveData<String>()
    val noHp = MutableLiveData<String>()

    fun onClickBack(){
        activity?.let { dismissKeyboard(it) }
        navController.navigate(R.id.splashFragment)
    }

    fun onClickLogin(){
        activity?.let { dismissKeyboard(it) }
        navController.navigate(R.id.loginFragment)
    }

    fun onClickRegister(){
        activity?.let { dismissKeyboard(it) }
        val intent = Intent(activity, MainActivity::class.java)
        activity?.startActivity(intent)
        activity?.finish()
    }
}