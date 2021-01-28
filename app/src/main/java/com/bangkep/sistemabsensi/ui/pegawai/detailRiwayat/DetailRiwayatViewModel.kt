package com.bangkep.sistemabsensi.ui.pegawai.detailRiwayat

import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.bangkep.sistemabsensi.base.BaseViewModel
import com.bangkep.sistemabsensi.model.ModelAbsensi
import com.bangkep.sistemabsensi.model.ModelUser
import com.bangkep.sistemabsensi.utils.DataSave
import com.bangkep.sistemabsensi.utils.onClickFoto

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