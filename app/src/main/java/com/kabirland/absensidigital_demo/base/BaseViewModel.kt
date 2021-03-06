package com.kabirland.absensidigital_demo.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel (){
    val isShowLoading = MutableLiveData<Boolean>()
    val message = MutableLiveData<String>()
    val status = MutableLiveData<String>()
}