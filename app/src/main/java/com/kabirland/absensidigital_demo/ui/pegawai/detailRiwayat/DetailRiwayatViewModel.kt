package com.kabirland.absensidigital_demo.ui.pegawai.detailRiwayat

import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.kabirland.absensidigital_demo.base.BaseViewModel
import com.kabirland.absensidigital_demo.model.ModelAbsensi
import com.kabirland.absensidigital_demo.model.ModelUser
import com.kabirland.absensidigital_demo.utils.DataSave
import com.kabirland.absensidigital_demo.utils.onClickFoto

class DetailRiwayatViewModel(
    private val navController: NavController,
    private val savedData: DataSave
) : BaseViewModel() {
    val dataAbsensi = MutableLiveData<ModelAbsensi>()
    val dataUser = MutableLiveData<ModelUser>()
    val dataNipNama = MutableLiveData<String>()
    val jenisAbsen = MutableLiveData<String>()
    val statusAbsen = MutableLiveData<String>()

    fun clickFoto(){
        onClickFoto(dataAbsensi.value?.img?:"", navController)
    }
}